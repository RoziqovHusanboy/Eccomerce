package com.example.eccomerce.presintation.sign_up

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eccomerce.R
import com.example.eccomerce.databinding.FragmentSignUpBinding
import com.example.eccomerce.utils.toast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val viewModel by viewModels<SIgnUpViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.loading.observe(viewLifecycleOwner) {
            binding.progress.isVisible = it
            binding.register.text = if (it) null else getString(R.string.fragmnet_sign_up_register)
        }
        viewModel.events.observe(viewLifecycleOwner) {
            when (it) {
                SIgnUpViewModel.Event.Error -> toast(R.string.error)
                SIgnUpViewModel.Event.ConnectionError -> toast(R.string.connection_error)
                SIgnUpViewModel.Event.AlreadyRegistered -> toast(R.string.already_registered)
                SIgnUpViewModel.Event.NavigateToHome -> toast(R.string.app_name)
            }
        }
    }


    private fun initUI() = with(binding) {
        register.setOnClickListener {
            viewModel.signUp(
                username.text.toString(),
                email.text.toString(),
                password.text.toString()
            )
        }
        signIn.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
        }
        val termsOfUse = getString(R.string.termsof_use)
        val privacyPolicy = getString(R.string.privacy_policy)
        val string =
            getString(R.string.fragment_sign_up_terms_and_privacy, termsOfUse, privacyPolicy)
        val spannable = SpannableString(string)

        val termsOfUseSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))
                startActivity(browserIntent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color =ContextCompat.getColor(requireContext(),R.color.orange)
            }
        }

        val privacyPolicySpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com"))
                startActivity(browserIntent)
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color =ContextCompat.getColor(requireContext(),R.color.orange)
            }
        }
        spannable.setSpan(
            termsOfUseSpan,
            string.indexOf(termsOfUse),
            string.indexOf(termsOfUse) + termsOfUse.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            privacyPolicySpan,
            string.indexOf(privacyPolicy),
            string.indexOf(privacyPolicy) + privacyPolicy.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        terms.text = spannable
        terms.movementMethod = LinkMovementMethod.getInstance()



    }


}