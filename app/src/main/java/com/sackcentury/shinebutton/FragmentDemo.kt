package com.sackcentury.shinebutton

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.sackcentury.ShineButton

/**
 * @author xiongke
 * @since 2026/1/14
 **/
class FragmentDemo : Fragment() {
    private var rootView: View? = null
    private var fragmentManager: FragmentManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.common_fragment, null)
        return rootView
    }

    override fun onResume() {
        initData()
        super.onResume()
    }

    private fun initData() {
        val shineButton1 = rootView?.findViewById<ShineButton>(R.id.po_image1)
        shineButton1?.init(activity as MainActivity)

        val shineButton2 = rootView?.findViewById<ShineButton>(R.id.po_image2)
        val shineButton3 = rootView?.findViewById<ShineButton>(R.id.po_image3)
        val hideBtn = rootView?.findViewById<Button>(R.id.hide_button)
        hideBtn?.setOnClickListener {
            hideFragment()
        }
        rootView?.setOnClickListener {
            hideFragment()
        }
    }

    fun showFragment(fragmentManager: FragmentManager) {
        this.fragmentManager = fragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.fragmentv_slide_bottom_enter,
            0,
            0,
            R.anim.fragmentv_slide_top_exit
        )
        transaction.add(Window.ID_ANDROID_CONTENT, this, "FragmentDemo")
        transaction.addToBackStack(null)
        transaction.commitAllowingStateLoss()
    }

    private fun hideFragment() {
        fragmentManager?.popBackStack()
    }
}
