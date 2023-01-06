package com.example.binchecker

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class HelperAdapter(
    private var arr: Array<Array<String>>,
    private var context: Context
) :
    RecyclerView.Adapter<HelperAdapter.MyViewClass>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewClass {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.table_row, parent, false)
        return MyViewClass(view)
    }

    override fun onBindViewHolder(holder: MyViewClass, position: Int) {
        for (i in holder.allView.indices) {
            holder.allView[i].text = arr[position][i]
        }
        if (holder.url.text != "N/A") {
            holder.url.setTextColor(context.getColorStateList(R.color.blue))
            holder.url.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            holder.url.isEnabled = true
        }
        if (holder.phone.text != "N/A") {
            holder.phone.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.phone, 0)
            holder.phone.isEnabled = true
        }
        if (!holder.coordinates.text.toString().contains("N/A")) {
            holder.coordinates.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.placeholder, 0)
            holder.coordinates.isEnabled = true
        }

        holder.coordinates.setOnClickListener {
            val crd = holder.coordinates.text.toString()
            val lat = crd.substring(11, crd.indexOf(','))
            val long = crd.substringAfterLast(": ").let { it.substring(0, it.lastIndex) }
            val location = Uri.parse("geo:$lat,$long?z=14")
            val mapIntent = Intent(Intent.ACTION_VIEW, location)
            startActivity(context, mapIntent, null)
        }

        holder.phone.setOnClickListener {
            val phone = holder.phone.text.toString().replace("[^0-9 +-]".toRegex(), "")
            val phoneIntent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
            startActivity(context, phoneIntent, null)
        }

        holder.url.setOnClickListener {
            var url = holder.url.text.toString()
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://$url"
            }
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(context, webIntent, null)
        }


    }

    override fun getItemCount(): Int {
        return arr.size
    }

    inner class MyViewClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
            val allView = arrayListOf(length, luhn, scheme, type, brand, prepaid, country, coordinates, bank, url, phone, bin)
    }
}