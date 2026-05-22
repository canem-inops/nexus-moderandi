package dev.canem.nexusmoderandi.ui.pause

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.canem.nexusmoderandi.service.PauseManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PauseViewModel @Inject constructor(
    private val pauseManager: PauseManager
) : ViewModel() {

    val pauseUntil: StateFlow<Long> = pauseManager.pauseUntil
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    fun pause(hours: Int) {
        viewModelScope.launch {
            pauseManager.pauseFor(hours * 60 * 60 * 1000L)
        }
    }

    fun resume() {
        viewModelScope.launch {
            pauseManager.resume()
        }
    }
}
