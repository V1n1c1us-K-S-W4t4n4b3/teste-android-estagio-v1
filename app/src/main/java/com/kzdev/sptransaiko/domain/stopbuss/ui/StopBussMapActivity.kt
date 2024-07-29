package com.kzdev.sptransaiko.domain.stopbuss.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.kzdev.sptransaiko.domain.stopbuss.adapter.StopBussSearchAdapter
import com.kzdev.sptransaiko.domain.stopbuss.models.DataStopBussResponse
import com.kzdev.sptransaiko.domain.stopbuss.viewmodels.StopBussListViewModel
import com.kzdev.sptransaiko.domain.stopbussdeatils.ui.StopBussDetailsActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StopBussMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var mMap: GoogleMap

    private var token = "b4cbfc4b77f36931b7e2ee1bcaf5d165927ec6ef5ff9e667fc7a926426125d82"

    private var termosBusca = ""

    private val viewModelStopBuss: StopBussListViewModel by viewModels { StopBussListViewModel.Factory }

    private val viewModelAuthentication: AuthenticationViewModel by viewModels { AuthenticationViewModel.Factory }

    private var stopBussListData: List<LatLng>? = null

    private lateinit var adapter: StopBussSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val map = supportFragmentManager.findFragmentById(R.id.myMap) as SupportMapFragment
        map.getMapAsync(this)

        viewModelAuthentication.postAuthentication(token)
    }

    override fun onStart() {
        super.onStart()

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
            setupSearchRecyclerView(stopBussList)
        }

        viewModelStopBuss.errorException.observe(this) {
            Log.d("resposta get", "StopBussList: $it")
        }
    }

    private fun setupSearchRecyclerView(data: List<DataStopBussResponse>) {
        binding.rvSearch.layoutManager = LinearLayoutManager(this)

        val search = data.map { DataStopBussResponse(it.cp, it.ed, it.np, it.py, it.px) }

        adapter = StopBussSearchAdapter(search) { stopBuss ->
            closeSearchView()
            stopBussListData = listOf(LatLng(stopBuss.py, stopBuss.px))
            stopBussListData?.let { updateMapWithStops(it, listOf(stopBuss)) }
        }

        binding.rvSearch.adapter = adapter

        binding.searchView.editText.addTextChangedListener {
            searchSupplierByText(it.toString())
        }
    }

    private fun closeSearchView() {
        binding.searchView.clearFocusAndHideKeyboard()
        binding.searchView.hide()
    }

    private fun searchSupplierByText(text: String) {

        lifecycleScope.launch {
            binding.layoutProgressSearch.visibility = View.VISIBLE
            delay(500L)
            adapter.filter.filter(text)
            binding.layoutProgressSearch.visibility = View.GONE
        }
    }

    private fun updateMapWithStops(stops: List<LatLng>, data: List<DataStopBussResponse>) {
        Log.d("resposta lista", "Updating map with stops: $stops")

        if (::mMap.isInitialized) {
            mMap.clear()

            val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.icon_stop_buss)
            val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 80, 80, false)
            val customIcon = BitmapDescriptorFactory.fromBitmap(scaledBitmap)

            if (stops.size == data.size) {
                for (i in stops.indices) {
                    val stop = stops[i]
                    val stopBuss = data[i]

                    val marker = mMap.addMarker(
                        MarkerOptions()
                            .position(stop)
                            .title("Parada: ${stopBuss.ed}")
                            .icon(customIcon)
                    )
                    marker?.tag = stopBuss.ed

                    if (stops.isNotEmpty()) {
                        val boundsBuilder = LatLngBounds.Builder()
                        stops.forEach { boundsBuilder.include(it) }
                        val bounds = boundsBuilder.build()
                        val zoomLevel = 14
                        mMap.moveCamera(
                            CameraUpdateFactory.newLatLngBounds(
                                bounds,
                                zoomLevel
                            )
                        )
                    }

                    mMap.setOnMarkerClickListener {
                        val nameMark = it.tag as? String
                        val intent = Intent(this, StopBussDetailsActivity::class.java).apply {
                            if (nameMark != null) {
                                putExtra("name", nameMark)
                            }
                            putExtra("cp", stopBuss.cp)
                            putExtra("token", token)
                        }
                        startActivity(intent)
                        true
                    }
                    viewModelStopBuss.getStopBuss(token, termosBusca)
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        val saoPauloLatLng = LatLng(-23.5505, -46.6333)
        val zoomLevel = 14f
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(saoPauloLatLng, zoomLevel))

    }
}