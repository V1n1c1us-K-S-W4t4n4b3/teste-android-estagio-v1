package com.kzdev.sptransaiko.domain.stopbuss.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.kzdev.sptransaiko.databinding.ItemSearchTwoLinesBinding
import com.kzdev.sptransaiko.domain.stopbuss.models.DataStopBussResponse

class StopBussSearchAdapter(
    private val dataSet: List<DataStopBussResponse>,
    private val onItemClicked: (DataStopBussResponse) -> Unit,
) : RecyclerView.Adapter<StopBussSearchAdapter.ViewHolder>(), Filterable {

    private var filteredDataSet: MutableList<DataStopBussResponse> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSearchTwoLinesBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val name = filteredDataSet[position].np
        val address = filteredDataSet[position].ed

        viewHolder.binding.tvNp.text = name
        viewHolder.binding.tvEd.text = address

        viewHolder.itemView.setOnClickListener { onItemClicked(filteredDataSet[position]) }
    }

    override fun getItemCount() = filteredDataSet.size

    class ViewHolder(val binding: ItemSearchTwoLinesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterPattern = constraint.toString().trim()

                val filteredList = dataSet.filter {
                    it.np?.contains(filterPattern, true) == true ||
                            it.ed?.contains(filterPattern, true) == true
                }

                filteredDataSet.clear()
                filteredDataSet.addAll(filteredList)

                val filterResults = FilterResults()
                filterResults.values = filteredDataSet

                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                notifyDataSetChanged()
            }
        }
    }
}