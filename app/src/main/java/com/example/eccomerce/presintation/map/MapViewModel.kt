package com.example.eccomerce.presintation.map

import SingleLiveEvent
import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eccomerce.data.api.order.dto.Track
import com.example.eccomerce.domain.repo.OrderRepository
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    val track = MutableLiveData<Track>()
    val error = SingleLiveEvent<Unit>()
    val route = MutableLiveData<PolylineOptions>()

    fun getTrack(id: Int) = viewModelScope.launch {
        try {
            val result = orderRepository.getTrack(id)
            getDirections(result)
            track.postValue(result)
        } catch (e: Exception) {
            error.call()
        }
    }

    private fun getDirections(track: Track) = viewModelScope.launch {
        try {
            val routes = orderRepository.getDirections(track)

            val points = ArrayList<LatLng>()
            val lineOptions = PolylineOptions()

            val path = routes.first()

            for (j in path.indices) {
                val point = path[j]
                val lat = point["lat"]?.toDouble() ?: continue
                val lng = point["lng"]?.toDouble() ?: continue
                val position = LatLng(lat,lng)
                points.add(position)
            }

            lineOptions.addAll(points)
            lineOptions.width(12f)
            lineOptions.color(Color.RED)
            lineOptions.geodesic(true)

            route.postValue(lineOptions)

        } catch (e: Exception) {
            e.printStackTrace()
            error.call()
        }

    }

}