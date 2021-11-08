package com.ardnn.mynotesroom.ui.insert

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.ardnn.mynotesroom.R
import com.ardnn.mynotesroom.database.Note
import com.ardnn.mynotesroom.databinding.ActivityMainBinding
import com.ardnn.mynotesroom.databinding.ActivityNoteAddUpdateBinding
import com.ardnn.mynotesroom.helper.DateHelper
import com.ardnn.mynotesroom.helper.ViewModelFactory

class NoteAddUpdateActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_NOTE = "extra_note"
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    private lateinit var viewModel: NoteAddUpdateViewModel
    private var _binding: ActivityNoteAddUpdateBinding? = null
    private val binding get() = _binding

    private var isEdit = false
    private var note: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNoteAddUpdateBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewModel = obtainViewModel(this@NoteAddUpdateActivity)

        note = intent.getParcelableExtra(EXTRA_NOTE)
        if (note != null) {
            isEdit = true
        } else {
            note = Note()
        }

        val actionBarTitle: String
        val btnTitle: String
        if (isEdit) {
            actionBarTitle = resources.getString(R.string.change)
            btnTitle = resources.getString(R.string.update)
            if (note != null) {
                note?.let { note ->
                    binding?.edtTitle?.setText(note.title)
                    binding?.edtDescription?.setText(note.description)
                }
            }
        } else {
            actionBarTitle = resources.getString(R.string.add)
            btnTitle = resources.getString(R.string.save)
        }
        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding?.btnSubmit?.text = btnTitle
        binding?.btnSubmit?.setOnClickListener {
            val title = binding?.edtTitle?.text.toString().trim()
            val description = binding?.edtDescription?.text.toString().trim()
            when {
                title.isEmpty() -> {
                    binding?.edtTitle?.error = resources.getString(R.string.empty)
                }
                description.isEmpty() -> {
                    binding?.edtDescription?.error = resources.getString(R.string.empty)
                }
                else -> {
                    note.let { note ->
                        note?.title = title
                        note?.description = description
                    }
                    if (isEdit) {
                        viewModel.update(note as Note)
                        showToast(resources.getString(R.string.changed))
                    } else {
                        note.let { note ->
                            note?.date = DateHelper.getCurrentDate()
                        }
                        viewModel.insert(note as Note)
                        showToast(resources.getString(R.string.added))
                    }
                    finish()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isEdit) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }

    private fun showAlertDialog(type: Int) {
        val isDialogCLose: Boolean = type == ALERT_DIALOG_CLOSE

        val dialogTitle: String
        val dialogMessage: String
        if (isDialogCLose) {
            dialogTitle = resources.getString(R.string.cancel)
            dialogMessage = resources.getString(R.string.cancel)
        } else {
            dialogTitle = resources.getString(R.string.delete)
            dialogMessage = resources.getString(R.string.delete)
        }

        val alertBuilder = AlertDialog.Builder(this)
        with (alertBuilder) {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setCancelable(true)

            setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                if (!isDialogCLose) {
                    viewModel.delete(note as Note)
                    showToast(resources.getString(R.string.deleted))
                }
                finish()
            }

            setNegativeButton(resources.getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
        }
        val alertDialog = alertBuilder.create()
        alertDialog.show()
    }

    private fun obtainViewModel(activity: AppCompatActivity): NoteAddUpdateViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[NoteAddUpdateViewModel::class.java]
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}