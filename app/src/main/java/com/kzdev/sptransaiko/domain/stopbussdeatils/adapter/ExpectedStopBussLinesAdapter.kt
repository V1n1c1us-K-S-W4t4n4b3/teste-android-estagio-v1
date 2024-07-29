package com.kzdev.sptransaiko.domain.stopbussdeatils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kzdev.sptransaiko.databinding.ItemListStopBussLinesBinding
import com.kzdev.sptransaiko.domain.stopbussdeatils.model.DataExpectedTimeStopBussResponse

class ExpectedStopBussLinesAdapter(
    private val dataset: DataExpectedTimeStopBussResponse,
    private val onItemClicked: (Int) -> Unit,
) : RecyclerView.Adapter<ExpectedStopBussLinesAdapter.ViewModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModel {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListStopBussLinesBinding.inflate(inflater, parent, false)
        return ViewModel(binding)
    }

    override fun onBindViewHolder(holder: ViewModel, position: Int) {
        val info = dataset.p?.l?.get(position)

        if (info != null) {
            holder.binding.tvLine.text = "${holder.binding.tvLine.text} ${info.cl}"
        }

        if (info != null) {
            holder.binding.tvStarting.text = "${holder.binding.tvStarting.text} ${info.lt0}"
        }

        if (info != null) {
            holder.binding.tvDestiny.text = "${holder.binding.tvDestiny.text} ${info.lt1}"
        }

        val vsText = info?.vs?.joinToString(separator = ", ") { it.t }
        holder.binding.tvExpectedTime.text = "${holder.binding.tvExpectedTime.text} ${vsText}"

        holder.itemView.setOnClickListener {
            dataset.p?.l?.get(position)?.let { it1 -> onItemClicked(it1.cl) }
        }
    }

    override fun getItemCount(): Int = dataset.p.let { it?.l?.size!! }

    class ViewModel(val binding: ItemListStopBussLinesBinding) :
        RecyclerView.ViewHolder(binding.root)
}