package com.example.eccomerce.presintation.wishlist

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.eccomerce.data.api.products.dto.Product
import com.example.eccomerce.databinding.FragmentWishlistBinding
import com.example.eccomerce.presintation.products.ProductsAdapter
import com.example.eccomerce.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WishlistFragment:BaseFragment<FragmentWishlistBinding>(FragmentWishlistBinding::inflate) {
    private val adapter by lazy { ProductsAdapter(this::onClick,this::like) }
    private val viewModel by viewModels<WishlistViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter.addLoadStateListener {
            viewModel.setLoadStates(it)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        subscribeToLiveData()
    }

    private fun initUI() = with(binding) {
    search.setOnClickListener {
        findNavController().navigate(WishlistFragmentDirections.toSearchFragment())

    }
        error.retry.setOnClickListener {
            viewModel.getProducts()
        }
        products.adapter =adapter

        swipe.setOnRefreshListener {
            viewModel.getProducts()
        }

    }

    private fun subscribeToLiveData() = with(binding) {

        viewModel.loading.observe(viewLifecycleOwner){
            loading.root.isVisible = it && swipe.isRefreshing.not()
            if (it.not()) swipe.isRefreshing = false
        }

        viewModel.error.observe(viewLifecycleOwner){
            error.root.isVisible =it
        }

        viewModel.products.observe(viewLifecycleOwner){
            viewLifecycleOwner.lifecycleScope.launch {
                adapter.submitData(it)
            }
        }

    }

    private fun onClick(product: Product){
    findNavController().navigate(WishlistFragmentDirections.toDetailFragment(product.id))
    }

    private fun like(product: Product){

    }
}

