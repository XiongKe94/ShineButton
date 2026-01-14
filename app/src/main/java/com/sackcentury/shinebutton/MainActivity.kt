package com.sackcentury.shinebutton

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.sackcentury.ShineButton

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var shineButton: ShineButton
    private lateinit var porterShapeImageView1: ShineButton
    private lateinit var porterShapeImageView2: ShineButton
    private lateinit var porterShapeImageView3: ShineButton

    private lateinit var listDemo: Button
    private lateinit var fragmentDemo: Button
    private lateinit var dialogDemo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.let {
            // it.hide()
        }
        // setFullScreen(this)
        shineButton = findViewById(R.id.po_image0)
        listDemo = findViewById(R.id.btn_list_demo)
        fragmentDemo = findViewById(R.id.btn_fragment_demo)
        dialogDemo = findViewById(R.id.btn_dialog_demo)

        val linearLayout = findViewById<LinearLayout>(R.id.wrapper)

        shineButton.init(this)
        porterShapeImageView1 = findViewById(R.id.po_image1)
        porterShapeImageView1.init(this)
        porterShapeImageView2 = findViewById(R.id.po_image2)
        porterShapeImageView2.init(this)
        porterShapeImageView3 = findViewById(R.id.po_image3)
        porterShapeImageView3.init(this)

        val shineButtonJava = ShineButton(this)

        shineButtonJava.setUnCheckColor(Color.GRAY)
        shineButtonJava.setCheckColor(Color.RED)
        shineButtonJava.setShapeResource(R.raw.heart, R.raw.heart)
        shineButtonJava.setAllowRandomColor(true)
        shineButtonJava.setShineSize(100)
        val layoutParams = LinearLayout.LayoutParams(100, 100)
        shineButtonJava.layoutParams = layoutParams
        linearLayout.addView(shineButtonJava)

        shineButton.setOnClickListener {
            Log.e(TAG, "click")
        }


        porterShapeImageView2.setOnClickListener {
            Log.e(TAG, "click")
        }
        porterShapeImageView3.setOnClickListener {
            Log.e(TAG, "click")
        }

        listDemo.setOnClickListener { v ->
            startActivity(Intent(v.context, ListDemoActivity::class.java))
        }
        fragmentDemo.setOnClickListener {
            showFragmentPage()
        }
        dialogDemo.setOnClickListener {
            val dialog = Dialog(this)
            val view = LayoutInflater.from(this).inflate(R.layout.dialog, null)
            val shineButton = view.findViewById<ShineButton>(R.id.po_image)

            shineButton.setOnClickListener {
                Log.e(TAG, "click")
            }
            dialog.setContentView(view)
            dialog.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fragment_page -> {
                showFragmentPage()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showFragmentPage() {
        FragmentDemo().showFragment(supportFragmentManager)
    }

    /**
     * 设置全屏
     *
     * @param activity
     */
    companion object {
        fun setFullScreen(activity: AppCompatActivity) {
            activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
            // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //     activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //     activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            // }

            /**
             * Issue Test
             * Issue https://github.com/ChadCSong/ShineButton/issues/29
             **/
            // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //     // in Activity's onCreate() for instance
            //     activity.window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            // }
        }
    }
}
