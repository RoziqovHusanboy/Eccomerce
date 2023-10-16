package com.example.eccomerce.presintation.sign_in

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
class SIgnInViewModel @Inject constructor (private val authRepository: AuthRepository):ViewModel() {

    val loading = MutableLiveData<Boolean>(false)
    val events = SingleLiveEvent<Event>()

    fun signIn(username:String,password:String){
        viewModelScope.launch(Dispatchers.IO) {
        loading.postValue(true)
           try {
               authRepository.signIn(username,password)
               events.postValue(Event.NavigateToHome)
           }catch (e:Exception){
               when {
                   e is retrofit2.HttpException && e.code() == 404 -> events.postValue(Event.InvalidCreadentials)
                   e is IOException ->events.postValue(Event.ConnectionError)
                   else -> events.postValue(Event.Error)
               }

           }finally {
               loading.postValue(false)
           }
        }
    }

    sealed class Event{
        object InvalidCreadentials:Event()
        object Error:Event()
        object ConnectionError:Event()
        object NavigateToHome:Event()
    }

}