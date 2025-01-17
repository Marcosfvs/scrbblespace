package com.devspace.scribblespace.common.remote

import android.provider.ContactsContract.CommonDataKinds.Note
import com.devspace.scribblespace.common.model.NoteData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlin.Exception

private const val NOTE_COLLECTION = "notes"

class RemoteDataSource private constructor(
    private val dataBase: FirebaseFirestore

) {

    suspend fun addNote(title: String, description: String): Result<String> {
        return TODO()
    }

    // retorno de função pode apenas ter um tipo de retorno
    // Com o result pode ter dois tipos, sucesso ou falha
    suspend fun getNotes(): Result<List<NoteData>> {

        return try {
// Snapshot query
            val querySnapshot = dataBase
                .collection(NOTE_COLLECTION) //nome da base de dados
                .get()
                .await() // não deixa passar desse codigo ate conseguir o resultado

            // Get Notes
            val notesFromRemote = querySnapshot.documents.mapNotNull { noteFromDb ->
                noteFromDb.toObject(NoteRemoteData::class.java)
                    ?.copy(id = noteFromDb.id)
            }

            val notesData: MutableList<NoteData> = mutableListOf()
            notesFromRemote.forEach { note ->
                if (note.id != null && note.title != null && note.description != null) {
                    notesData.add(
                        NoteData(
                            key = note.id,
                            title = note.title,
                            description = note.description
                        )
                    )
                }
            }

            return Result.success(notesData)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    suspend fun deleteNote(id: String): Result<Unit> {
        return TODO()
    }

    companion object {
        fun create(): RemoteDataSource {
            return RemoteDataSource(dataBase = FirebaseFirestore.getInstance())
        }
    }
}