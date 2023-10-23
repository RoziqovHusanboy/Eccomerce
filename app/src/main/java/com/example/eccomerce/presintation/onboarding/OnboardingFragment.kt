package com.example.eccomerce.presintation.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.eccomerce.R
import com.example.eccomerce.databinding.FragmentOnboardingBinding
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    private lateinit var binding:FragmentOnboardingBinding
    private val adapter = OnboardingAdapter()
    private val viewModel by viewModels<OnboardingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingBinding.inflate(layoutInflater,container,false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    private fun initUI() = with(binding) {
        pager.adapter =adapter
        indecator.apply {

            val normalColor = ContextCompat.getColor(requireContext(),R.color.indicator_uncheked)
            val checkedColor = ContextCompat.getColor(requireContext(),R.color.indicator_cheked)
            setSliderColor(normalColor, checkedColor)
            setSliderWidth(resources.getDimension(R.dimen.dp_15))
            setSliderHeight(resources.getDimension(R.dimen.dp_8))
            setSlideMode(IndicatorSlideMode.WORM)
            setIndicatorStyle(IndicatorStyle.ROUND_RECT)
            setPageSize(adapter.itemCount)
            notifyDataChanged()
        }

        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                indecator.onPageScrolled(position,positionOffset,positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                indecator.onPageSelected(position)
                next.text = if (position==adapter.itemCount-1){
                    getString(R.string.fragment_onboarding_get_string)
                }else{
                    getString(R.string.fragment_onboarding_next)
                }

            }
        })
        next.setOnClickListener {
            if (pager.currentItem == adapter.itemCount-1){
                viewModel.onboarding()
                findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToSignInFragment())

            }else {
                pager.setCurrentItem(pager.currentItem+1,true)

            }
        }
    }

}