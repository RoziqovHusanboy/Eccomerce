package com.example.eccomerce.presintation.map
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.eccomerce.R
import com.example.eccomerce.data.api.order.dto.Track
import com.example.eccomerce.databinding.FragmentMapBinding
import com.example.eccomerce.utils.BaseFragment
import com.example.eccomerce.utils.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(FragmentMapBinding::inflate),
    OnMapReadyCallback {
    private val viewModel by viewModels<MapViewModel>()
    private val args by navArgs<MapFragmentArgs>()
    private var map: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getTrack(args.order)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        subscribeToLiveData()
    }

    @SuppressLint("NewApi")
    private fun subscribeToLiveData() = with(binding) {
        viewModel.track.observe(viewLifecycleOwner) {
            driver.root.isVisible = true
            Glide.with(root).load(it.driver.avatar).transform(CenterCrop(), RoundedCorners(12.dp))
                .into(driver.avatar)

            driver.name.text = it.driver.name
            driver.id.text = getString(R.string.bottom_sheet_driver_id, it.driver.id)

            driver.call.setOnClickListener { _ ->
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${it.driver.number}")
                startActivity(intent)
            }

            driver.chat.setOnClickListener { _ ->
                val intent = Intent(Intent.ACTION_SEND)
                intent.data = Uri.parse("sms:+ ${Uri.encode(it.driver.number)}")
                if (intent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(intent)
                }
            }

            map?.let { _ ->
                drawLocations(it)
            }

        }

        viewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(root, R.string.fragment_map_error, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry) {
                    viewModel.getTrack(args.order)
                }.show()
        }

        viewModel.route.observe(viewLifecycleOwner){
            map?.addPolyline(it)
        }
    }

    private fun initUI() = with(binding) {
        back.setOnClickListener {
            findNavController().popBackStack()
        }
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this@MapFragment)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        viewModel.track.value?.let {
            drawLocations(it)
        }
        viewModel.route.value?.let {
            map.addPolyline(it)
        }
    }

    private fun drawLocations(track: Track) {

        val fromBitmap = Bitmap.createBitmap(31.dp, 31.dp, Bitmap.Config.ARGB_8888)
        val fromCanvas = Canvas(fromBitmap)
        val fromShape =
            ContextCompat.getDrawable(requireContext(), R.drawable.marker_from) ?: return
        fromShape.setBounds(0, 0, fromBitmap.width, fromBitmap.height)
        fromShape.draw(fromCanvas)

        map!!.addMarker(
            MarkerOptions().position(track.from.latLng).anchor(.5f, .5f)
                .icon(BitmapDescriptorFactory.fromBitmap(fromBitmap))
        )

        val toBitmap = Bitmap.createBitmap(37.dp, 37.dp, Bitmap.Config.ARGB_8888)
        val toCanvas = Canvas(toBitmap)
        val toShape = ContextCompat.getDrawable(requireContext(), R.drawable.marker_to) ?: return
        toShape.setBounds(0, 0, toBitmap.width, toBitmap.height)
        toShape.draw(toCanvas)

        map!!.addMarker(
            MarkerOptions().position(track.to.latLng).anchor(.5f, .5f)
                .icon(BitmapDescriptorFactory.fromBitmap(toBitmap))
        )

        val mapView = childFragmentManager.findFragmentById(R.id.map)?.view ?: return
        if (mapView.viewTreeObserver.isAlive.not()) return
        mapView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val bounds = LatLngBounds.Builder()
                    .include(track.from.latLng)
                    .include(track.to.latLng)
                    .build()
                map!!.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 64.dp))
                mapView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })


    }
}