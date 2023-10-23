package com.example.eccomerce.domain.model

sealed class Destination {
    object Home :Destination()
    object Onboarding:Destination()
    object Auth:Destination()
}