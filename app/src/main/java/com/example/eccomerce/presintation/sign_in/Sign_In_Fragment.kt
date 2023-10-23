package com.example.eccomerce.presintation.sign_in

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eccomerce.R
import com.example.eccomerce.databinding.FragmentSignInBinding
import com.example.eccomerce.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Sign_In_Fragment : Fragment() {
    private lateinit var binding:FragmentSignInBinding
    private val viewModel by viewModels<SIgnInViewModel> ()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater,container,false)
        return  binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        subscribeToLiveData()

    }

    private fun subscribeToLiveData() {
        viewModel.loading.observe(viewLifecycleOwner){
        binding.progress.isVisible = it
            binding.signIn.text = if (it) null else getString(R.string.fragmnet_sign_in_button)
        }
        viewModel.events.observe(viewLifecycleOwner){
            when(it){
                SIgnInViewModel.Event.Error ->toast(R.string.connection_error)
                SIgnInViewModel.Event.InvalidCreadentials -> toast(R.string.invalid_credentils)
                SIgnInViewModel.Event.ConnectionError -> toast(R.string.error)
                SIgnInViewModel.Event.NavigateToHome -> toast(R.string.app_name)
            }
        }
    }

    fun initUI(){
        binding.signIn.setOnClickListener {
            viewModel.signIn(binding.username.toString(),binding.password.toString())
        }
        binding.signUp.setOnClickListener {
            findNavController().navigate(Sign_In_FragmentDirections.actionSignInFragmentToSignUpFragment())
        }
    }


}