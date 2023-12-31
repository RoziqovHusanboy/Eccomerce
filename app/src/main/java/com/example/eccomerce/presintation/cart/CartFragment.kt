package com.example.eccomerce.presintation.cart

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eccomerce.R
import com.example.eccomerce.databinding.FragmentCartBinding
import com.example.eccomerce.domain.model.Cart
import com.example.eccomerce.utils.BaseFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>(FragmentCartBinding::inflate) {

    val viewModel by viewModels<CartViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() = with(binding) {
        viewModel.carts.observe(viewLifecycleOwner) {
            products.adapter = CartAdapter(it, this@CartFragment::onClick, this@CartFragment::set)
            empty.isVisible = it.isEmpty()
            content.isVisible = it.isNotEmpty()
        }
        viewModel.event.observe(viewLifecycleOwner) {
            val massage = when (it) {
                CartViewModel.Event.BillingError -> R.string.fragment_cart_billing_error
                CartViewModel.Event.orderError -> R.string.fragment_cart_order_error
                CartViewModel.Event.orderCreated ->{
                    findNavController().popBackStack()
                R.string.fragment_cart_order_created
                }
            }
            Snackbar.make(root, massage, Snackbar.LENGTH_SHORT).show()


        }
        viewModel.billingLoading.observe(viewLifecycleOwner) {
            promoProgress.isVisible = it
            apply.text = if (it) null else getString(R.string.fragment_cart_apply)
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            progress.isVisible = it
            checkout.text = if (it) null else getString(R.string.fragment_cart_checkout)
        }

        viewModel.billing.observe(viewLifecycleOwner) { billing ->
            itemTotal.text = getString(R.string.price_2, billing.items)
            deliveryFee.text = getString(R.string.price_plus, billing.delivery)
            tax.text = getString(R.string.price_plus, billing.tax)
            binding.discount.text = getString(R.string.price_minus, billing.discount ?: 0.0)
            listOf(binding.discount, discountText).forEach {
                it.isVisible = billing.discount != null
            }

            orderTotal.text =getString(R.string.price_2,billing.total)

        }

    }

    private fun initUI() = with(binding) {
        back.setOnClickListener {
            findNavController().popBackStack()
        }

        delete.setOnClickListener {
            viewModel.clear()
        }

        apply.setOnClickListener {
            viewModel.getBilling(promoCode.text.toString())
        }
        checkout.setOnClickListener {
            viewModel.createOrder(promoCode.text.toString())
        }


    }

    fun onClick(cart: Cart) {
        findNavController().navigate(CartFragmentDirections.toDetailFragment(cart.id))
    }

    private fun set(cart: Cart) {
        viewModel.set(cart)
    }

}