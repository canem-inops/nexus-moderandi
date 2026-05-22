package dev.canem.nexusmoderandi.service

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "pause_settings")

@Singleton
class PauseManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val PAUSE_UNTIL = longPreferencesKey("pause_until")
    }

    val pauseUntil: Flow<Long> = context.dataStore.data.map { prefs ->
        prefs[PAUSE_UNTIL] ?: 0L
    }

    suspend fun isPaused(): Boolean {
        val until = context.dataStore.data.first()[PAUSE_UNTIL] ?: 0L
        return until > System.currentTimeMillis()
    }

    suspend fun pauseFor(durationMillis: Long) {
        context.dataStore.edit { prefs ->
            prefs[PAUSE_UNTIL] = System.currentTimeMillis() + durationMillis
        }
    }

    suspend fun resume() {
        context.dataStore.edit { prefs ->
            prefs[PAUSE_UNTIL] = 0L
        }
    }
}
