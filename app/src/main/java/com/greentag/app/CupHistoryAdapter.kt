package com.greentag.app.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.greentag.app.R
import com.greentag.app.CupReturn

class CupHistoryAdapter : RecyclerView.Adapter<CupHistoryAdapter.ViewHolder>() {

    private val cupList: MutableList<CupReturn> = mutableListOf()

    fun setData(data: List<CupReturn>) {
        cupList.clear()
        cupList.addAll(data)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textAlias: TextView = itemView.findViewById(R.id.textAlias)
        val textCustomer: TextView = itemView.findViewById(R.id.textCustomer)
        val textLocation: TextView = itemView.findViewById(R.id.textLocation)
        val textSize: TextView = itemView.findViewById(R.id.textSize)
        val textTimestamp: TextView = itemView.findViewById(R.id.textTimestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cup_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = cupList[position]
        holder.textAlias.text = item.alias
        holder.textCustomer.text = item.customer
        holder.textLocation.text = item.location
        holder.textSize.text = item.size
        holder.textTimestamp.text = item.timestamp.toString()
    }

    override fun getItemCount(): Int {
        return cupList.size
    }
}
