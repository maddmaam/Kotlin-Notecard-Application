package com.seng303.assignment1.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seng303.assignment1.data.PlayedGame
import com.seng303.assignment1.viewmodels.PlayerDataViewModel
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

//private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "player_data")

@FlowPreview
val playerScoreAccessModule = module {
    single<Storage<PlayedGame>> {
        PermanentStorage(
            gson = get(),
            type = object : TypeToken<List<PlayedGame>>(){}.type,
            preferenceKey = stringPreferencesKey("playerData"),
            dataStore = androidContext().dataStore
        )
    }


    single { Gson() }

    viewModel {
        PlayerDataViewModel(playedGameStorage = get())
    }
}