package com.example.workerapp.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.workerapp.data.source.local.datastore.PrefKeys

val Context.dataStore by preferencesDataStore(name = PrefKeys.PREFS_NAME)