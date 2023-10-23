package com.example.eccomerce.presintation.sign_up

import SingleLiveEvent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eccomerce.domain.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SIgnUpViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    val loading = MutableLiveData<Boolean>(false)
    val events = SingleLiveEvent<Event>()


    fun signUp(username: String, email: String, password: String) =
        viewModelScope.launch(Dispatchers.IO) {
            loading.postValue(true)
            try {
                authRepository.signUp(username, email, password)
                events.postValue(Event.NavigateToHome)
            } catch (e: Exception) {
                when {
                    e is retrofit2.HttpException && e.code() == 401 -> events.postValue(
                        Event.AlreadyRegistered
                    )

                    e is IOException -> events.postValue(Event.ConnectionError)
                    else -> events.postValue(Event.Error)
                }
            }finally {
                loading.postValue(false)
            }
        }


    sealed class Event {
        object AlreadyRegistered : Event()
        object ConnectionError : Event()
        object Error : Event()
        object NavigateToHome : Event()

    }
}