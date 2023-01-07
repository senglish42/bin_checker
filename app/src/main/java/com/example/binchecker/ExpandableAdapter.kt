package com.example.binchecker

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat

class ExpandableAdapter(private var arr: Array<Array<String>>, private val parent: RecyclerView):
    RecyclerView.Adapter<ExpandableAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpandableAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            for (i in allView.indices) {
                allView[i].text = arr[position][i]
            }
            bin.text = "${bin.text} ${country.text.toString().substring(0,4)}"
            if (url.text.toString() != "N/A") {
                url.setTextColor(parent.context.getColorStateList(R.color.blue))
                url.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                url.isEnabled = true
            }
            if (phone.text.toString() != "N/A") {
                phone.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.phone, 0)
                phone.isEnabled = true
            }
            if (!coordinates.text.toString().contains("N/A")) {
                coordinates.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.placeholder, 0)
                coordinates.isEnabled = true
            }
            coordinates.setOnClickListener {
                val crd = coordinates.text.toString()
                val lat = crd.substring(11, crd.indexOf(','))
                val long = crd.substringAfterLast(": ").let { it.substring(0, it.lastIndex) }
                val location = Uri.parse("geo:$lat,$long?z=14")
                val mapIntent = Intent(Intent.ACTION_VIEW, location)
                ContextCompat.startActivity(parent.context, mapIntent, null)
            }

            phone.setOnClickListener {
                val phone = phone.text.toString().replace("[^0-9 +-]".toRegex(), "")
                val phoneIntent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
                ContextCompat.startActivity(parent.context, phoneIntent, null)
            }

            url.setOnClickListener {
                var url = url.text.toString()
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://$url"
                }
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                ContextCompat.startActivity(parent.context, webIntent, null)
            }
            expandableLayout.visibility = if (arr[position][allView.lastIndex + 1] == "true") View.VISIBLE else View.GONE
            linearLayout.setOnClickListener{
                if (arr[position][allView.lastIndex + 1] == "true") {
                    arr[position][allView.lastIndex + 1] = "false"
                    bin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_up, 0)
                } else {
                    arr[position][allView.lastIndex + 1] = "true"
                    bin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0)
                }
                notifyItemChanged(position)
            }
        }
    }
    override fun getItemCount(): Int = arr.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val scheme: TextView = itemView.findViewById(R.id.scheme)
        val length: TextView = itemView.findViewById(R.id.length)
        val luhn: TextView = itemView.findViewById(R.id.luhn)
        val type: TextView = itemView.findViewById(R.id.type)
        val brand: TextView = itemView.findViewById(R.id.brand)
        val prepaid: TextView = itemView.findViewById(R.id.prepaid)
        val country: TextView = itemView.findViewById(R.id.country)
        val coordinates: TextView = itemView.findViewById(R.id.coordinates)
        val bank: TextView = itemView.findViewById(R.id.bank)
        val url: TextView = itemView.findViewById(R.id.url)
        val phone: TextView = itemView.findViewById(R.id.phone)
        val bin: TextView = itemView.findViewById(R.id.bin)
        val expandableLayout: RelativeLayout = itemView.findViewById(R.id.expandable_layout)
        val linearLayout: LinearLayout = itemView.findViewById(R.id.linearLayout)
        val allView = arrayListOf(length, luhn, scheme, type, brand, prepaid, country, coordinates, bank, url, phone, bin)
    }
}