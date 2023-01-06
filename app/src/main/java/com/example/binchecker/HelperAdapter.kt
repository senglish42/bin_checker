package com.example.binchecker

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class HelperAdapter(
    private var arr: ArrayList<ArrayList<String>>,
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
        if (holder.url.text != "?") {
            holder.url.setTextColor(context.getColorStateList(R.color.blue))
            holder.url.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            holder.url.isClickable = true
        }
        if (holder.phone.text != "?") {
            holder.phone.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.phone, 0)
        }
        holder.url.setOnClickListener {
            Toast.makeText(
                context,
                "Item Clicked",
                Toast.LENGTH_LONG
            ).show()
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
            val allView = arrayListOf(length, luhn, scheme, type, brand, prepaid, country, coordinates, bank, url, phone)
    }
}