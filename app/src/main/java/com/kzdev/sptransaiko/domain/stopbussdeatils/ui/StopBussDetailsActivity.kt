package com.kzdev.sptransaiko.domain.stopbussdeatils.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kzdev.sptransaiko.databinding.ActivityStopBussDetailsBinding
import com.kzdev.sptransaiko.domain.monitoringlines.ui.MonitoringLinesActivity
import com.kzdev.sptransaiko.domain.stopbussdeatils.adapter.ExpectedStopBussLinesAdapter
import com.kzdev.sptransaiko.domain.stopbussdeatils.model.DataExpectedTimeStopBussResponse
import com.kzdev.sptransaiko.domain.stopbussdeatils.viewmodel.ExpectedStopBussLinesViewModel

class StopBussDetailsActivity : AppCompatActivity() {

    private val binding by lazy { ActivityStopBussDetailsBinding.inflate(layoutInflater) }

    private val viewModelExpectedStopBussLines: ExpectedStopBussLinesViewModel by viewModels { ExpectedStopBussLinesViewModel.Factory }

    private var name = ""

    private var token = ""

    private var cp = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        receiveData()

        binding.tvName.text = name

        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    override fun onStart() {
        super.onStart()

        viewModelExpectedStopBussLines.getExpectedStopBussLines(token, cp)

        observerViews()
    }

    private fun observerViews() {
        viewModelExpectedStopBussLines.list.observe(this) {
            if (it.p != null) {
                setupRecyclerView(it)
                binding.tvWithoutPrediction.visibility = View.GONE
            } else {
                binding.tvWithoutPrediction.visibility = View.VISIBLE
            }
        }
    }

    private fun setupRecyclerView(data: DataExpectedTimeStopBussResponse) {
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = ExpectedStopBussLinesAdapter(data) {
            open(it)
        }
    }

    private fun open(cl: Int) {
        val intent = Intent(this, MonitoringLinesActivity::class.java)
        intent.putExtra("cl", cl)
        intent.putExtra("token", token)
        startActivity(intent)
    }


    private fun receiveData() {

        name = intent.getStringExtra("name") ?: ""

        token = intent.getStringExtra("token") ?: ""

        cp = intent.getIntExtra("cp", 0)

    }
}