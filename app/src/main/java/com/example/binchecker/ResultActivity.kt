package com.example.binchecker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ResultActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val backButton = findViewById<Button>(R.id.button_back)
        var scheme = "N/A"
        var length = "N/A"
        var luhn = "N/A"
        var type = "N/A"
        var brand = "N/A"
        var prepaid = "N/A"
        var country = "N/A"
        var coordinates = "(latitude: N/A, longitude: N/A)"
        var bank = "N/A"
        var url = "N/A"
        var phone = "N/A"
        val bin = intent.getStringExtra("bin")?:"N/A"
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        val titleList = listOf("number", "scheme", "type", "brand", "prepaid", "country", "bank")
        for (i in titleList.indices) {
            val arr = intent.getStringArrayExtra(titleList[i])!!.toMutableList()
            when(i) {
                0 -> if (arr.size > 1) { length = arr[0]; luhn = arr[1] }
                1 -> if (arr[0].length > 1) scheme = arr[0]
                2 -> if (arr[0].length > 1) type = arr[0]
                3 -> if (arr[0].length > 1) brand = arr[0]
                4 -> if (arr[0].length > 1) prepaid = arr[0]
                5 -> {
                    if (arr[2].length > 1) {
                        country = "${arr[3]} ${arr[2]}"
                        coordinates = "(latitude: ${arr[5]}, longitude: ${arr[6]})"
                    }
                }
                6 -> {
                    if (arr[0].length > 1) {
                        bank = "${arr[0]}${if (arr.size == 4) ", ${arr[3]}" else ""}"
                        if (arr.size > 1) url = arr[1]
                        if (arr.size > 2) phone = arr[2]
                    }
                }
            }
        }
        val arrList = arrayListOf(length, luhn, scheme, type, brand, prepaid, country, coordinates, bank, url, phone, bin)
        val helperAdapter = HelperAdapter(arrayListOf(arrList), this@ResultActivity)
        recyclerView.adapter = helperAdapter
        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}