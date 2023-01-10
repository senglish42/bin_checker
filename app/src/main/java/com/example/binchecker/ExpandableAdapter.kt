package com.example.binchecker

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TableLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView


class ExpandableAdapter(private var arr: Array<Array<String>>, private var context: Context):
    RecyclerView.Adapter<ExpandableAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpandableAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val value = TypedValue()
        context.theme.resolveAttribute(com.google.android.material.R.attr.colorOnPrimary, value, true)
        holder.apply {
            url.setPaintFlags(url.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv())
            url.setTextColor(value.data)
            coordinates.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            for (elem in phoneList) {
                elem.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                elem.isEnabled = false
                elem.isVisible = true
            }
            for (i in 1..phoneList.lastIndex) {
                phoneTable.removeView(phoneList[i])
            }
            url.isEnabled = false
            bank.isEnabled = false
            coordinates.isEnabled = false
            for (i in allView.indices) {
                allView[i].text = arr[holder.bindingAdapterPosition][i]
            }
            bin.text = "${bin.text} ${country.text.subSequence(0, 4)}"
            if (url.text != "N/A") {
                url.setTextColor(context.getColorStateList(R.color.blue))
                url.paintFlags = Paint.UNDERLINE_TEXT_FLAG
                url.isEnabled = true
            }
            if (phone.text.toString().contains("OR")) {
                val split = phone.text.toString().split("OR")
                phone.text = split[0].replace("\\s".toRegex(), "")
                for (i in 1..split.lastIndex) {
                    if (split[i] == "") continue
                    val newPhone = TextView(context)
                    newPhone.layoutParams = phone.layoutParams
                    newPhone.typeface = phone.typeface
                    newPhone.gravity = phone.gravity
                    newPhone.setTextSize(TypedValue.COMPLEX_UNIT_SP,20f)
                    newPhone.setTextColor(phone.textColors)
                    newPhone.setPadding(0, 0, 0, 10)
                    newPhone.text = split[i]
                    phoneTable.addView(newPhone)
                    phoneList.add(newPhone)
                }
            }
            for (phoneTV in phoneList) {
                if (phoneTV.text != "N/A") {
                    phoneTV.setCompoundDrawablesWithIntrinsicBounds(R.drawable.phone, 0, 0, 0)
                    phoneTV.isVisible = true
                    phoneTV.isEnabled = true
                }
            }
            if (bank.text != "N/A") bank.isEnabled = true
            if (!arr[position][7].contains("N/A")) {
                coordinates.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.placeholder, 0)
                coordinates.isEnabled = true
            }
            val isExpanded = arr[position][allView.lastIndex + 1] == "true"
            if (isExpanded) {
                expandableLayout.visibility = View.VISIBLE
                bin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_up, 0)
            } else {
                expandableLayout.visibility = View.GONE
                bin.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0)
            }
            linearLayout.setOnClickListener{
                arr[position][allView.lastIndex + 1] = if (isExpanded) "false" else "true"
                notifyItemChanged(position)
            }
            listOf(country, coordinates).forEach { map->
                map.setOnClickListener {
                    val crd = coordinates.text.toString()
                    val lat = crd.substring(11, crd.indexOf(','))
                    val long = crd.substringAfterLast(": ").let { it.substring(0, it.lastIndex) }
                    val location = Uri.parse("geo:0,0?q=$lat,$long(${country.text})")
                    val mapIntent = Intent(Intent.ACTION_VIEW, location)
                    ContextCompat.startActivity(context, mapIntent, null)
                }
            }
            bank.setOnClickListener {
                val crd = coordinates.text.toString()
                val lat = crd.substring(11, crd.indexOf(','))
                val long = crd.substringAfterLast(": ").let { it.substring(0, it.lastIndex) }
                val location =  Uri.parse("geo:$lat,$long?q=${bank.text}")
                val mapIntent = Intent(Intent.ACTION_VIEW, location)
                ContextCompat.startActivity(context, mapIntent, null)
            }
            phoneList.forEach { elem->
                if (elem.isVisible) {
                    elem.setOnClickListener {
                        val phone = elem.text.toString().replace("[^0-9 +-]".toRegex(), "")
                        val phoneIntent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
                        ContextCompat.startActivity(context, phoneIntent, null)
                    }
                }
            }
            url.setOnClickListener {
                var url = url.text.toString()
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://$url"
                }
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                ContextCompat.startActivity(context, webIntent, null)
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
        val phoneList: MutableList<TextView> = mutableListOf(phone)
        val phoneTable: TableLayout = itemView.findViewById(R.id.phoneTable)
        val allView = arrayListOf(length, luhn, scheme, type, brand, prepaid, country, coordinates, bank, url, phone, bin)
    }
}