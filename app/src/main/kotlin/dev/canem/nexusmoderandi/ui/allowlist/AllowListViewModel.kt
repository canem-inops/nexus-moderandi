package dev.canem.nexusmoderandi.ui.allowlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.canem.nexusmoderandi.data.entity.AllowedNumber
import dev.canem.nexusmoderandi.data.repository.AllowListRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllowListViewModel @Inject constructor(
    private val repository: AllowListRepository
) : ViewModel() {

    val allowedNumbers: StateFlow<List<AllowedNumber>> = repository.getAllNumbers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    var inputNumber by mutableStateOf("")
        private set

    fun onInputChange(value: String) {
        inputNumber = value
    }

    fun addNumber() {
        val number = inputNumber.trim()
        if (number.isNotBlank()) {
            viewModelScope.launch {
                repository.addNumber(number)
                inputNumber = ""
            }
        }
    }

    fun removeNumber(phoneNumber: String) {
        viewModelScope.launch {
            repository.removeNumber(phoneNumber)
        }
    }
}
