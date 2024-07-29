package com.kzdev.sptransaiko.domain.monitoringlines.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Looper
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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.kzdev.sptransaiko.R
import com.kzdev.sptransaiko.databinding.ActivityMonitoringLinesBinding
import com.kzdev.sptransaiko.domain.monitoringlines.model.MonitoringBussResponse
import com.kzdev.sptransaiko.domain.monitoringlines.viewmodel.MonitoringBussViewModel

class MonitoringLinesActivity : AppCompatActivity(), OnMapReadyCallback {

    private val binding by lazy { ActivityMonitoringLinesBinding.inflate(layoutInflater) }

    private val viewModelMonitoringBuss: MonitoringBussViewModel by viewModels { MonitoringBussViewModel.Factory }

    private var token = ""

    private var cl = 0

    private lateinit var mMap: GoogleMap

    private var isMapReady = false

    private var isInitialZoom = false

    private var isInitialUpdate = false

    private var stopBussListData: List<LatLng>? = null

    private val markersMap = mutableMapOf<String, Marker>()

    private val handler = android.os.Handler(Looper.getMainLooper())

    private val updateInterval: Long = 10000

    private val updateRunnable = object : Runnable {
        override fun run() {
            viewModelMonitoringBuss.getMonitoringBuss(token, cl)
            handler.postDelayed(this, updateInterval)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val map = supportFragmentManager.findFragmentById(R.id.mapMonitoring) as SupportMapFragment
        map.getMapAsync(this)

        receiveData()
    }

    override fun onStart() {
        super.onStart()

        handler.post(updateRunnable)
        observerViews()
    }

    override fun onStop() {
        super.onStop()

        handler.removeCallbacks(updateRunnable)
    }

    private fun observerViews() {
        viewModelMonitoringBuss.list.observe(this) { monitoringBuss ->
            if (monitoringBuss.vs.isEmpty()) {
                Toast.makeText(
                    this, "Não encontramos nenhum ônibus nesta linha", Toast.LENGTH_SHORT
                ).show()
            } else {
                stopBussListData = monitoringBuss.vs.map { LatLng(it.py, it.px) }
                stopBussListData?.let { updateMapWithBuss(it, monitoringBuss) }
            }
        }
    }

    private fun receiveData() {

        token = intent.getStringExtra("token") ?: ""

        cl = intent.getIntExtra("cl", 0)
    }

    private fun updateMapWithBuss(buss: List<LatLng>, data: MonitoringBussResponse) {
        Log.d("resposta lista", "Updating map with stops: $buss")

        if (isMapReady) {
            val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.icon_buss_red)
            val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 100, 100, false)
            val customIcon = BitmapDescriptorFactory.fromBitmap(scaledBitmap)

            val specialBitmap = BitmapFactory.decodeResource(resources, R.drawable.icon_buss_blue)
            val specialScaledBitmap = Bitmap.createScaledBitmap(specialBitmap, 100, 100, false)
            val specialIcon = BitmapDescriptorFactory.fromBitmap(specialScaledBitmap)

            if (!isInitialUpdate) {
                val boundsBuilder = LatLngBounds.Builder()
                buss.forEach { boundsBuilder.include(it) }
                val bounds = boundsBuilder.build()
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
                isInitialUpdate = true
            }

            for (i in buss.indices) {
                val stop = buss[i]
                val stopBuss = data.vs[i]

                val icon = if (stopBuss.a) specialIcon else customIcon

                val snippet =
                    if (stopBuss.a) "Acessível para pessoas com deficiência"
                    else "Não acessível para pessoas com deficiência"

                val existingMarker = markersMap[stopBuss.p]
                if (existingMarker != null) {
                    existingMarker.position = stop
                } else {
                    val newMarker = mMap.addMarker(
                        MarkerOptions()
                            .position(stop)
                            .title("Onibus: ${stopBuss.p}")
                            .snippet(snippet)
                            .icon(icon)
                    )
                    newMarker?.tag = stopBuss.p
                    if (newMarker != null) {
                        markersMap[stopBuss.p] = newMarker
                    }
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        isMapReady = true

        if (!isInitialZoom) {
            val saoPauloLatLng = LatLng(-23.5505, -46.6333)
            val zoomLevel = 14f
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(saoPauloLatLng, zoomLevel))
            isInitialZoom = true
        }
    }
}