package com.example.eccomerce.presintation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
 import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.eccomerce.R
import com.example.eccomerce.common.Constants
import com.example.eccomerce.data.api.products.dto.Product
import com.example.eccomerce.databinding.FragmentDetailBinding
import com.google.android.material.snackbar.Snackbar
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment:Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel by viewModels<DetailViewModel>()
    private val args by navArgs<DetailFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getProduct(args.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() = with(binding) {
        viewModel.loading.observe(viewLifecycleOwner){
            loading.root.isVisible =it
            setButtonVisibility()
        }

        viewModel.error.observe(viewLifecycleOwner){
            error.root.isVisible = it
            setButtonVisibility()
        }

        error.retry.setOnClickListener {
            viewModel.getProduct(args.id)
        }

        viewModel.detail.observe(viewLifecycleOwner){
            val color = if (it.wishlist) R.color.orange else R.color.dark
            val resolved =  ResourcesCompat.getColor(resources,color,null)
            favorite.setColorFilter(resolved)

            images.adapter = ImageAdapter(it.images)
            indecator.setupWithViewPager(images)
            indecator.apply {
                setPageSize(it.images.size)
                notifyDataChanged()
            }

            category.text = it.category
            title.text = it.title
            price.text = getString(R.string.item_product_price,(it.price-(it.discount?:0.0)))
            oldPrice.isVisible = it!=null
            oldPrice.text = getString(R.string.item_product_price_striked,it.discount?:0.0)

            reviews.text = getString(R.string.fragment_product_reviews,it.rating,it.reviews)
           description.text = it.description

            products.adapter = RelatedAdapter(it.related,::onClick)

            viewModel.wishlist.observe(viewLifecycleOwner){
                setWishlist(it)
            }

        }

        viewModel.count.observe(viewLifecycleOwner){
            count.text =it.toString()
        }


    }

    private fun FragmentDetailBinding.setButtonVisibility() {
        add.isVisible = viewModel.loading.value ==false && viewModel.error.value==false
        buttonDivider.isVisible =  viewModel.loading.value ==false && viewModel.error.value==false
    }

    private fun initUI() = with(binding) {
             back.setOnClickListener {
                 findNavController().popBackStack()
             }

        indecator.apply {

            val normalColor = ContextCompat.getColor(requireContext(), R.color.indicator_uncheked)
            val checkedColor = ContextCompat.getColor(requireContext(), R.color.indicator_cheked)
            setSliderColor(normalColor, checkedColor)
            setSliderWidth(resources.getDimension(R.dimen.dp_12))
            setSliderHeight(resources.getDimension(R.dimen.dp_6))
            setSlideMode(IndicatorSlideMode.WORM)
            setIndicatorStyle(IndicatorStyle.ROUND_RECT)
            notifyDataChanged()
        }
        plus.setOnClickListener {
            viewModel.increment()
        }
        minus.setOnClickListener {
            viewModel.decrement()
        }
        share.setOnClickListener {
            ShareCompat.IntentBuilder(requireContext())
                .setType("text/plain")
                .setChooserTitle(R.string.fragment_detail_share_title)
                .setText(Constants.HOST +"products/${args.id}")
                .startChooser();
        }

        favorite.setOnClickListener {
            viewModel.toggleWishlist()
        }

        add.setOnClickListener {
            viewModel.set()
            Snackbar.make(root,R.string.fragment_detail_cart_added,Snackbar.LENGTH_SHORT).show()
            findNavController().popBackStack()

        }


    }

    private fun onClick(product: Product){
        findNavController().navigate(DetailFragmentDirections.toDetailFragment(product.id))
    }
    fun setWishlist(wishlist:Boolean) = with(binding) {
        val color = if (wishlist)R.color.orange else R.color.dark
        val resolved = ContextCompat.getColor(requireContext(),color)
        favorite.setColorFilter(resolved)
    }
}