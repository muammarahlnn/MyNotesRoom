package com.ardnn.mynotesroom.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ardnn.mynotesroom.database.Note
import com.ardnn.mynotesroom.databinding.ItemNoteBinding
import com.ardnn.mynotesroom.helper.NoteDiffCallback
import com.ardnn.mynotesroom.ui.insert.NoteAddUpdateActivity

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    private val noteList = ArrayList<Note>()
    fun setNoteList(noteList: List<Note>) {
        val diffCallback = NoteDiffCallback(this.noteList, noteList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.noteList.clear()
        this.noteList.addAll(noteList)

        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(noteList[position])
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    inner class ViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            with (binding) {
                tvItemTitle.text = note.title
                tvItemDate.text = note.date
                tvItemDescription.text = note.description
                cvItemNote.setOnClickListener { view ->
                    val intent = Intent(view.context, NoteAddUpdateActivity::class.java)
                    intent.putExtra(NoteAddUpdateActivity.EXTRA_NOTE, note)
                    view.context.startActivity(intent)
                }
            }
        }
    }
}