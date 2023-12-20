package com.example.eccomerce.presintation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.eccomerce.R
import com.example.eccomerce.data.api.products.dto.Banner
import com.example.eccomerce.data.api.products.dto.Category
import com.example.eccomerce.data.api.products.dto.Product
import com.example.eccomerce.data.api.products.dto.Section
import com.example.eccomerce.databinding.FragmentHomeBinding
import com.example.eccomerce.presintation.home.adapters.BannerAdapter
import com.example.eccomerce.presintation.home.adapters.HomeCategoryAdapter
import com.example.eccomerce.presintation.home.adapters.HomeSectionAdapter
import com.example.eccomerce.utils.setLightStatusBar
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToLiveData()
        initUI()
    }

    private fun initUI() = with(binding) {
        setLightStatusBar()
       error.retry.setOnClickListener {
            viewModel.getHome()
        }
        indecator.apply {

            val normalColor = ContextCompat.getColor(requireContext(), R.color.indicator_uncheked)
            val checkedColor = ContextCompat.getColor(requireContext(), R.color.indicator_cheked)
            setSliderColor(normalColor, checkedColor)
            setSliderWidth(resources.getDimension(R.dimen.dp_20))
            setSliderHeight(resources.getDimension(R.dimen.dp_4))
            setSlideMode(IndicatorSlideMode.WORM)
            setIndicatorStyle(IndicatorStyle.ROUND_RECT)
            notifyDataChanged()
        }
        banners.offscreenPageLimit =1

        showAll.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.toCategoriesFragment())
        }

        searchContainer.search.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus.not()) return@setOnFocusChangeListener
            findNavController().navigate(HomeFragmentDirections.toSearchFragment())

        }


    }

    private fun subscribeToLiveData() = with(binding) {
        viewModel.loading.observe(viewLifecycleOwner) {
            loading.root.isVisible = it
        }

        viewModel.error.observe(viewLifecycleOwner) {
            error.root.isVisible = it
        }
        viewModel.home.observe(viewLifecycleOwner) {
            home.isVisible = it != null
            it ?: return@observe

            val name = it.user.firstName ?: it.user.username
            greeting.text = getString(R.string.fragment_home_greeting, name)

            Glide.with(root).load(it.user.avatar).into(avatar)

            banners.adapter = BannerAdapter(it.banners, this@HomeFragment::onBannerClick)
            indecator.setupWithViewPager(banners)
            indecator.apply {
                setPageSize(it.banners.size)
                notifyDataChanged()
            }

            categories.adapter =
                HomeCategoryAdapter(it.categories, this@HomeFragment::onCategoryClick)
            sections.adapter = HomeSectionAdapter(
                it.sections,
                this@HomeFragment::showAll,
                this@HomeFragment::onClickProduct,
                this@HomeFragment::wishlist
            )
            count.text =it.notification_count.toString()

        }
    }

    private fun onBannerClick(banner: Banner) {

    }

    private fun onCategoryClick(category: Category) {
        findNavController().navigate(HomeFragmentDirections.toProductsFragment(category))
    }

    private fun showAll(section: Section) {

    }

    private fun onClickProduct(product: Product) {
        findNavController().navigate(HomeFragmentDirections.toDetailFragment(product.id))
    }

    private fun wishlist(product: Product) {
        viewModel.toggleWishlist(product)
    }
}