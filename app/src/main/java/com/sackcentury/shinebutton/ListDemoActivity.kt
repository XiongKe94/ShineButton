package com.sackcentury.shinebutton

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.sackcentury.ShineButton

class ListDemoActivity : Activity() {

    private lateinit var listView: ListView
    private val dataList = mutableListOf<Data>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_demo)

        listView = findViewById(R.id.list)
        listView.adapter = ListAdapter()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    inner class ListAdapter : BaseAdapter() {
        init {
            for (i in 0 until 20) {
                dataList.add(Data())
            }
        }

        override fun getCount(): Int {
            return dataList.size
        }

        override fun getItem(i: Int): Any {
            return dataList[i]
        }

        override fun getItemId(i: Int): Long {
            return 0
        }

        override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View {
            val itemView = view ?: LayoutInflater.from(this@ListDemoActivity)
                .inflate(R.layout.list_item, null)
            val button = itemView.findViewById<ShineButton>(R.id.po_image)
            val textView = itemView.findViewById<TextView>(R.id.text_item_id)
            textView.text = "ShineButton Position $i"

            return itemView
        }
    }

    data class Data(
        var position: Int = 0,
        var checked: Boolean = false
    )
}
