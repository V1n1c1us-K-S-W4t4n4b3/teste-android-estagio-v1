package com.kzdev.sptransaiko.domain.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.kzdev.sptransaiko.R
import com.kzdev.sptransaiko.databinding.ActivityMainBinding
import com.kzdev.sptransaiko.domain.authentication.viewmodel.AuthenticationViewModel
import com.kzdev.sptransaiko.domain.stopbuss.viewmodels.StopBussListViewModel

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var mMap: GoogleMap

    private val token = "b4cbfc4b77f36931b7e2ee1bcaf5d165927ec6ef5ff9e667fc7a926426125d82"

    private val termosBusca = "Afo"

    private val viewModelStopBuss: StopBussListViewModel by viewModels { StopBussListViewModel.Factory }

    private val viewModelAuthentication: AuthenticationViewModel by viewModels { AuthenticationViewModel.Factory }

    private var stopBussListData: List<LatLng>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val map = supportFragmentManager.findFragmentById(R.id.myMap) as SupportMapFragment
        map.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()

        viewModelAuthentication.postAuthentication(token)
        observerViews()
    }

    private fun observerViews() {

        viewModelAuthentication.authentication.observe(this) {
            Log.d("API Request", "Requisição de paradas de ônibus foi feita.")
            viewModelStopBuss.getStopBuss(token, termosBusca)
            Log.d("getStopBuss", "Token: $token")
            Log.d("getStopBuss", "Termos de Busca: $termosBusca")
        }

        viewModelStopBuss.stopBussList.observe(this) { stopBussList ->
            Log.d("resposta ok", "StopBussList: ${stopBussList}")

            if (stopBussList.isEmpty()) {
                Toast.makeText(this, "Não encontramos nenhuma parada de ônibus", Toast.LENGTH_SHORT)
                    .show()

            } else {
                stopBussListData = stopBussList.map { LatLng(it.py, it.px) }
                stopBussListData?.let { updateMapWithStops(it) }
                Log.d("lista stop buss", "StopBussList: ${stopBussList}")

            }
        }

        viewModelStopBuss.errorException.observe(this) {
            Log.d("resposta get", "StopBussList: $it")
        }
    }

    private fun updateMapWithStops(stops: List<LatLng>) {
        Log.d("resposta lista", "Updating map with stops: $stops")

        if (::mMap.isInitialized) {
            mMap.clear()

            val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.icon_stop_buss)
            val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 100, 100, false)
            val customIcon = BitmapDescriptorFactory.fromBitmap(scaledBitmap)

            for (stop in stops) {
                mMap.addMarker(MarkerOptions().position(stop).title("Parada").icon(customIcon))
            }

            if (stops.isNotEmpty()) {
                val boundsBuilder = LatLngBounds.Builder()
                stops.forEach { boundsBuilder.include(it) }
                val bounds = boundsBuilder.build()
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        val saoPauloLatLng = LatLng(-23.5505, -46.6333)
        val zoomLevel = 12f

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(saoPauloLatLng, zoomLevel))

        stopBussListData?.let { updateMapWithStops(it) }
    }
}