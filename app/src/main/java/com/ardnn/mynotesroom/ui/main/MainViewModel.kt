package com.ardnn.mynotesroom.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ardnn.mynotesroom.database.Note
import com.ardnn.mynotesroom.repositories.NoteRepository

class MainViewModel(application: Application) : ViewModel() {
    private val noteRepo: NoteRepository = NoteRepository(application)

    fun getALlNotes(): LiveData<List<Note>> {
        return noteRepo.getAllNotes()
    }
}