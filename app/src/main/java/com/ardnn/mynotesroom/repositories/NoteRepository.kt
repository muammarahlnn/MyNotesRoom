package com.ardnn.mynotesroom.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import com.ardnn.mynotesroom.database.Note
import com.ardnn.mynotesroom.database.NoteDao
import com.ardnn.mynotesroom.database.NoteDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class NoteRepository(application: Application) {
    private val noteDao: NoteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = NoteDatabase.getDatabase(application)
        noteDao = db.noteDao()
    }

    fun getAllNotes(): LiveData<List<Note>> {
        return noteDao.getALlNotes()
    }

    fun insert(note: Note) {
        executorService.execute {
            noteDao.insert(note)
        }
    }

    fun delete(note: Note) {
        executorService.execute {
            noteDao.delete(note)
        }
    }

    fun update(note: Note) {
        executorService.execute {
            noteDao.update(note)
        }
    }
}