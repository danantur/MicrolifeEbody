package com.dantesting.microlifeebody.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dantesting.microlifeebody.R
import com.polidea.rxandroidble2.RxBleDevice

class SearchAdapter(private val itemsArray: ArrayList<RxBleDevice>, private val callback: OnSearchItemClick) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val mac: TextView = itemView.findViewById(R.id.mac)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder =
        SearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false))

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.name.text = itemsArray[position].name
        holder.mac.text = itemsArray[position].macAddress
        holder.itemView.setOnClickListener {
            callback.onClick(itemsArray[position])
        }
    }

    override fun getItemCount(): Int = itemsArray.size

    interface OnSearchItemClick {
        fun onClick(device: RxBleDevice)
    }
}