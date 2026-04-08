package dev.canem.nexusmoderandi.ui.rejectedcalls

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.canem.nexusmoderandi.data.entity.RejectedCall
import dev.canem.nexusmoderandi.data.repository.RejectedCallRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RejectedCallsViewModel @Inject constructor(
    private val repository: RejectedCallRepository
) : ViewModel() {

    val rejectedCalls: StateFlow<List<RejectedCall>> = repository.getRejectedCalls()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun clearAll() {
        viewModelScope.launch { repository.clearAll() }
    }
}
