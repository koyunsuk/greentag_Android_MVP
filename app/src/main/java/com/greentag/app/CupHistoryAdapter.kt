package com.greentag.app.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.greentag.app.CupReturn
import com.greentag.app.R

class CupHistoryAdapter : RecyclerView.Adapter<CupHistoryAdapter.ViewHolder>() {

    private var dataList: List<CupReturn> = emptyList()

    fun submitList(data: List<CupReturn>) {
        dataList = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textAlias: TextView = itemView.findViewById(R.id.textAlias)
        val textCustomer: TextView = itemView.findViewById(R.id.textCustomer)
        val textLocation: TextView = itemView.findViewById(R.id.textLocation)
        val textSize: TextView = itemView.findViewById(R.id.textSize)
        val textTimestamp: TextView = itemView.findViewById(R.id.textTimestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cup_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = dataList[position]
        holder.textAlias.text = entry.alias
        holder.textCustomer.text = entry.customer
        holder.textLocation.text = entry.location
        holder.textSize.text = entry.size
        holder.textTimestamp.text = entry.timestamp.toString()
    }
}
