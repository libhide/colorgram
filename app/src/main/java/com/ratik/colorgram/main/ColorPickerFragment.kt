package com.ratik.colorgram.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.ratik.colorgram.*
import kotlinx.android.synthetic.main.fragment_color_select.*

/**
 * Created by Ratik on 05/07/17.
 */

class ColorPickerFragment : Fragment() {
    lateinit var colorChangeListener: OnColorChangeListener

    private val onSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                when (seekBar.tag) {
                    "red" -> red = i
                    "green" -> green = i
                    "blue" -> blue = i
                    else -> {
                        red = APP_RED
                        green = APP_GREEN
                        blue = APP_BLUE
                    }
                }
                colorChangeListener.colorChanged(red, green, blue)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        }

    private var red: Int = 0
    private var green: Int = 0
    private var blue: Int = 0

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            colorChangeListener = (context as OnColorChangeListener?)!!
        } catch (e: ClassCastException) {
            throw ClassCastException("${context!!} must implement ${ColorPickerFragment::class.java.simpleName}")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_color_select, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mutableListOf(redSlider, greenSlider, blueSlider).forEach {
            it.setOnSeekBarChangeListener(onSeekBarChangeListener)
        }
    }

    override fun onResume() {
        super.onResume()

        red = arguments?.getInt(PREF_RED) ?: APP_RED
        green = arguments?.getInt(PREF_GREEN) ?: APP_GREEN
        blue = arguments?.getInt(PREF_BLUE) ?: APP_BLUE

        redSlider.progress = red
        greenSlider.progress = green
        blueSlider.progress = blue
    }

    companion object {
        fun newInstance(args: Bundle): ColorPickerFragment {
            val fragment = ColorPickerFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
