package com.example.eccomerce.presintation.main

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.forEachIndexed
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.example.eccomerce.MainDirections
import com.example.eccomerce.R
import com.example.eccomerce.databinding.ActivityMainBinding
import com.example.eccomerce.domain.model.Destination
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var binding:ActivityMainBinding
    private val navController get() = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        subscribeToLiveData()
        initUI()
    }
    private fun initUI() = with(binding) {

        navigation.setupWithNavController(navController)
        navigation.setOnItemSelectedListener {
            var index:Int = 0
            navigation.menu.forEachIndexed { itemIndex, item ->
                if (it.itemId != item.itemId) return@forEachIndexed
                index = itemIndex
            }
            ConstraintSet().apply {
                clone(indicatorContainer)
                setHorizontalBias(indicator.id,index * 0.25F)

                val transition: Transition = ChangeBounds()
                transition.setInterpolator(DecelerateInterpolator(1.0f))
                transition.duration = 500

                TransitionManager.beginDelayedTransition(indicatorContainer, transition)
                applyTo(indicatorContainer)
            }
            NavigationUI.onNavDestinationSelected(it,navController)

            return@setOnItemSelectedListener true
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->

            navigation.isVisible = listOf(
                R.id.onboardingFragment,
                R.id.sign_In_Fragment,
                R.id.signUpFragment,
                R.id.detailFragment,
                R.id.mapFragment
            ).all { it !=destination.id }
        }

    }

    private fun subscribeToLiveData() {
        viewModel.events.observe(this){
            when(it){
                is MainViewModel.Event.NavigateTo ->navigateTo(it.destination)
            }
        }
    }

    private fun navigateTo(destination: Destination) {
        if (navController.currentDestination?.id==R.id.detailFragment)return
        when(destination){
            Destination.Auth -> navController.navigate(MainDirections.toSignInFragment())
            Destination.Home -> navController.navigate(MainDirections.toHomeFragment())
            Destination.Onboarding -> navController.navigate(MainDirections.toOnboardingFragment())
        }
    }

}