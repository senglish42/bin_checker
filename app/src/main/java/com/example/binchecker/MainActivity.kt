package com.example.binchecker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import android.util.Log
import android.widget.*
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val check = findViewById<Button>(R.id.check)
        val history = findViewById<ImageView>(R.id.history)
        val editBin = findViewById<EditText>(R.id.edit_bin)
        val dtArray: Array<MutableList<String?>> = Array(7) { mutableListOf() }
        editBin.doAfterTextChanged {
            if (editBin.text.length > 8) {
                editBin.setText(editBin.text.substring(0, 8))
                editBin.setSelection(editBin.length())
            }
            if (editBin.text.length in listOf(6, 8)) {
                check.isEnabled = true
                check.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.red))
            } else {
                check.isEnabled = false
                check.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.gray))
            }
        }

        history.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        check.setOnClickListener {
            val url ="https://lookup.binlist.net/${editBin.text}"
            val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
            val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
                try {
                    val titleList = listOf("number", "scheme", "type", "brand", "prepaid", "country", "bank")
                    for (i in titleList.indices) {
                        val parent = response.optJSONObject(titleList[i])
                        parent?.let {
                            if (it.length() > 0) {
                                it.keys().forEach{ ch ->
                                    val child = it.optJSONObject(ch)
                                    child?.let { cit ->
                                        if (cit.length() > 0) {
                                            child.keys().forEach { ck ->
                                                dtArray[i].add(it.optString(ck)?:"N/A")
                                            }
                                        } else dtArray[i].add("N/A")
                                    } ?: dtArray[i].add(it.optString(ch)?:"N/A")
                                }
                            } else dtArray[i].add("N/A")
                        }?: dtArray[i].add(response.optString(titleList[i], "N/A"))
                    }
                    val intent = Intent(this, ResultActivity::class.java)
                    for (i in titleList.indices) {
                        intent.putExtra(titleList[i], dtArray[i].toTypedArray())
                    }
                    intent.putExtra("bin", editBin.text.toString())
                    startActivity(intent)

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }, { error ->
                Log.e("TAG", "RESPONSE IS $error")
                Toast.makeText(this@MainActivity, "BIN number is not found", Toast.LENGTH_SHORT).show()
            })
            queue.add(request)
        }
    }
}