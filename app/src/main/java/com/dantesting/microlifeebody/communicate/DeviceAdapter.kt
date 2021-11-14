package com.dantesting.microlifeebody.communicate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dantesting.microlifeebody.R

class LogAdapter(private val itemsArray: ArrayList<Log>) : RecyclerView.Adapter<LogAdapter.LogViewHolder>() {

    class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tag: TextView = itemView.findViewById(R.id.tag)
        val data: TextView = itemView.findViewById(R.id.data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder =
        LogViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_log, parent, false))

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        holder.tag.text = itemsArray[position].tag
        holder.data.text = itemsArray[position].data
    }

    override fun getItemCount(): Int = itemsArray.size

    data class Log(val tag: String, val data: String)
}