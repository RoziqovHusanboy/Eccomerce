package com.example.eccomerce.presintation.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.eccomerce.data.api.products.dto.Product
import com.example.eccomerce.databinding.FragmentProductsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductsFragment:Fragment() {
    private lateinit var binding:FragmentProductsBinding
    private val viewModel by viewModels<ProductsViewModel>()
    private val args by navArgs<ProductsFragmentArgs>()


    private val adapter by lazy { ProductsAdapter(this::onClick,this::wishlist) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setCategory(args.category)
        adapter.addLoadStateListener {
            viewModel.setLoadStates(it)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
           binding = FragmentProductsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() = with(binding) {
        viewModel.loading.observe(viewLifecycleOwner){
            loading.root.isVisible = it
        }
        viewModel.error.observe(viewLifecycleOwner){
            error.root.isVisible =it
        }

        viewModel.category.observe(viewLifecycleOwner){
            title.text = it.title

        }

        viewModel.products.observe(viewLifecycleOwner){
            viewLifecycleOwner.lifecycleScope.launch {
                adapter.submitData(it)
            }
        }

    }

    private fun initUI() = with(binding) {
            back.setOnClickListener{
                findNavController().popBackStack()
            }
        error.retry.setOnClickListener {
            viewModel.getProducts()
        }
        products.adapter =adapter
    }

    private fun onClick(product: Product){
        findNavController().navigate(ProductsFragmentDirections.toDetailFragment(product.id))
    }

    private fun wishlist(product: Product){
        viewModel.toggleWishlist(product)
    }
}