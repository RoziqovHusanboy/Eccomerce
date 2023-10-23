package com.example.eccomerce.presintation.main

import SingleLiveEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eccomerce.domain.model.Destination
import com.example.eccomerce.domain.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    val events = SingleLiveEvent<Event>()

    init {
        getDestination()
    }

    private fun getDestination() = viewModelScope.launch(Dispatchers.IO){
        authRepository.destinationFlow().collectLatest {
                events.postValue(Event.NavigateTo(it))

        }
    }

    sealed class Event{
        data class NavigateTo(val destination: Destination) :Event()

    }

}