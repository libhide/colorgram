package com.ratik.colorgram.ui.main

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.ratik.colorgram.*
import kotlinx.android.synthetic.main.fragment_color_select.*

class ColorPickerFragment : Fragment() {
    lateinit var colorChangeListener: OnColorChangeListener

    private val onSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                when (seekBar.tag) {
                    "red" -> colorChangeListener.redChanged(i)
                    "green" -> colorChangeListener.greenChanged(i)
                    "blue" -> colorChangeListener.blueChanged(i)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            colorChangeListener = (context as OnColorChangeListener?)!!
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement ${ColorPickerFragment::class.java.simpleName}")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_color_select, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val red = arguments?.getInt(PREF_RED) ?: APP_RED
        val green = arguments?.getInt(PREF_GREEN) ?: APP_GREEN
        val blue = arguments?.getInt(PREF_BLUE) ?: APP_BLUE

        redSlider.progress = red
        greenSlider.progress = green
        blueSlider.progress = blue

        mutableListOf(redSlider, greenSlider, blueSlider).forEach {
            it.setOnSeekBarChangeListener(onSeekBarChangeListener)
        }
    }

    companion object {
        fun newInstance(args: Bundle): ColorPickerFragment {
            val fragment = ColorPickerFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
