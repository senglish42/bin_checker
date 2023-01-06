package com.example.binchecker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryActivity: AppCompatActivity() {
    var binPref: SharedPreferences? = null
    var pref: SharedPreferences? = null
    var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        recyclerView = findViewById(R.id.recyclerView2)
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView?.layoutManager = linearLayoutManager
        val backButton = findViewById<Button>(R.id.button_back2)
        val arrOfArr = mutableListOf<Array<String>>()
        binPref = getSharedPreferences("data", Context.MODE_PRIVATE)
        binPref?.let {
            val arr = it.all.values.toTypedArray()
            arr.sort().also { arr.reverse() }
            if (arr.isEmpty()) return
            for (i in arr.indices) {
               val bin = it.all.entries.find { t -> t.value!! == arr[i] }!!.key.toString()
                pref = getSharedPreferences("$bin.data", Context.MODE_PRIVATE)
                val arrToCard = Array(12){"N/A"}
                for (num in arrToCard.indices) {
                    arrToCard[num] = pref?.getString(num.toString(), "N/A")?:"N/A"
                }
                arrOfArr.add(arrToCard)
            }
        }
        val helperAdapter = HelperAdapter(arrOfArr.toTypedArray(), this@HistoryActivity)
        recyclerView?.adapter = helperAdapter
        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}