package com.example.eccomerce.presintation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.eccomerce.data.api.products.dto.Product
import com.example.eccomerce.databinding.FragmentSearchBinding
import com.example.eccomerce.domain.model.ProductQuery
import com.example.eccomerce.presintation.filter.FilterFragment
import com.example.eccomerce.presintation.search.adapters.SearchAdapter
import com.example.eccomerce.presintation.search.adapters.SearchProductsAdapter
import com.example.eccomerce.utils.hideKeyboard
import com.example.eccomerce.utils.showKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModels<SearchViewModel>()
    private val adapter by lazy { SearchProductsAdapter(this::onClick, this::like) }
    private val args by navArgs<SearchFragmentArgs>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setInitials(args.category,args.wishlist)
         adapter.addLoadStateListener {
            viewModel.setLoadState(it)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() = with(binding) {
        viewModel.loading.observe(viewLifecycleOwner) {
            loading.root.isVisible = it
        }

        viewModel.products.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch {
                adapter.submitData(it)
            }
        }
        viewModel.searches.observe(viewLifecycleOwner) {
            recents.adapter = SearchAdapter(it, this@SearchFragment::onSearchClick)
           isRecentsVisible(searchContainer.search.hasFocus())
        }
    }

    private fun initUi() = with(binding) {
        searchContainer.search.requestFocus()
        showKeyboard()

        close.setOnClickListener {
            findNavController().popBackStack()
        }

        products.adapter = adapter
        searchContainer.search.setOnEditorActionListener { v, actionId, event ->

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.setSearch(searchContainer.search.text.toString())
                hideKeyboard()
                searchContainer.search.clearFocus()
                return@setOnEditorActionListener true
            }
            false
        }

        searchContainer.search.setOnFocusChangeListener { view, hasFocus ->
            isRecentsVisible(hasFocus)

        }

        clear.setOnClickListener {
            viewModel.clearRecents()
        }

        searchContainer.filter.setOnClickListener {
            val query = viewModel.query.value ?: ProductQuery()
            findNavController().navigate(SearchFragmentDirections.toFilterFragment(query))
        }

            setFragmentResultListener(FilterFragment.REQUEST_KEY){_,result->
                val query  = result.getParcelable<ProductQuery>(FilterFragment.RESULT_KEY)
                viewModel.setQuery(query?:return@setFragmentResultListener)
                searchContainer.search.clearFocus()
                hideKeyboard()
                isRecentsVisible(false)

        }

    }

    private fun FragmentSearchBinding.isRecentsVisible(hasFocus: Boolean) {
        listOf(recents, recentTitle, clear).forEach {
            it.isVisible = viewModel.searches.value.isNullOrEmpty().not() && hasFocus

        }
    }

    private fun onClick(product: Product) {
        findNavController().navigate(SearchFragmentDirections.toDetailFragment(product.id))
    }

    private fun like(product: Product) {

    }

    private fun onSearchClick(search: String) {
        viewModel.setSearch(search)
        binding.searchContainer.search.setText(search)
    }
}