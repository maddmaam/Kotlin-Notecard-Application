package com.seng303.assignment1.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seng303.assignment1.data.NoteCard
import com.seng303.assignment1.viewmodels.NoteCardViewModel
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "notes_data")

@FlowPreview
val dataAccessModule = module {
    single<Storage<NoteCard>> {
        PermanentStorage(
            gson = get(),
            type = object : TypeToken<List<NoteCard>>(){}.type,
            preferenceKey = stringPreferencesKey("noteCards"),
            dataStore = androidContext().dataStore
        )
    }

    single { Gson() }

    viewModel {
        NoteCardViewModel(flashCardStorage = get())
    }
}