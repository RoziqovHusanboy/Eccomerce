package com.example.eccomerce.presintation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eccomerce.domain.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val authRepository: AuthRepository
):ViewModel() {
    fun onboarding() =viewModelScope.launch {
        authRepository.onboarded()
    }


}

