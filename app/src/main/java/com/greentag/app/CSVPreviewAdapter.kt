// 1번 항목 - CSVPreviewAdapter.kt
package com.greentag.app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.greentag.app.data.CupReturn

class CSVPreviewAdapter(
    private val context: Context,
    private val itemList: List<CupReturn>
) : BaseAdapter() {

    override fun getCount(): Int = itemList.size

    override fun getItem(position: Int): Any = itemList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_csv_row, parent, false)

        val item = itemList[position]

        val uidText = rowView.findViewById<TextView>(R.id.textViewUid)
        val aliasText = rowView.findViewById<TextView>(R.id.textViewAlias)
        val customerText = rowView.findViewById<TextView>(R.id.textViewCustomer)
        val locationText = rowView.findViewById<TextView>(R.id.textViewLocation)
        val timestampText = rowView.findViewById<TextView>(R.id.textViewTimestamp)

        uidText.text = item.uid
        aliasText.text = item.alias ?: "-"
        customerText.text = item.customer ?: "-"
        locationText.text = item.location ?: "-"
        timestampText.text = (item.timestamp ?: "-").toString()

        return rowView
    }
}
