package com.ardnn.mynotesroom.ui.insert

import android.app.Application
import androidx.lifecycle.ViewModel
import com.ardnn.mynotesroom.database.Note
import com.ardnn.mynotesroom.repositories.NoteRepository

class NoteAddUpdateViewModel(application: Application) : ViewModel() {
    private val noteRepo: NoteRepository = NoteRepository(application)

    fun insert(note: Note) {
        noteRepo.insert(note)
    }

    fun update(note: Note) {
        noteRepo.update(note)
    }

    fun delete(note: Note) {
        noteRepo.delete(note)
    }
}