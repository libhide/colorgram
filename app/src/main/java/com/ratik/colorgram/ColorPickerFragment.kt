package com.ratik.colorgram

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import kotlinx.android.synthetic.main.fragment_color_select.view.*

/**
 * Created by Ratik on 05/07/17.
 */

const val PREF_RED = "red"
const val PREF_BLUE = "blue"
const val PREF_GREEN = "green"
const val APP_RED = 66
const val APP_GREEN = 134
const val APP_BLUE = 245

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

    lateinit var redSeekBar: SeekBar
    lateinit var greenSeekBar: SeekBar
    lateinit var blueSeekBar: SeekBar

    lateinit var sharedPrefs: SharedPreferences

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            colorChangeListener = (context as OnColorChangeListener?)!!
        } catch (e: ClassCastException) {
            throw ClassCastException("${context!!} must implement ${ColorPickerFragment::class.java.simpleName}")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_color_select, container, false)
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(activity)

        redSeekBar = rootView.redSlider
        greenSeekBar = rootView.greenSlider
        blueSeekBar = rootView.blueSlider

        mutableListOf(redSeekBar, greenSeekBar, blueSeekBar).forEach {
            it.setOnSeekBarChangeListener(onSeekBarChangeListener)
        }

        return rootView
    }

    override fun onPause() {
        super.onPause()

        Thread {
            val editor = sharedPrefs.edit()
            editor.putInt(PREF_RED, red)
            editor.putInt(PREF_GREEN, green)
            editor.putInt(PREF_BLUE, blue)
            editor.apply()
        }
    }

    override fun onResume() {
        super.onResume()

        Thread {
            red = sharedPrefs.getInt(PREF_RED, APP_RED)
            green = sharedPrefs.getInt(PREF_GREEN, APP_GREEN)
            blue = sharedPrefs.getInt(PREF_BLUE, APP_BLUE)
            redSeekBar.progress = red
            greenSeekBar.progress = green
            blueSeekBar.progress = blue
        }
    }

    companion object {
        fun newInstance(): ColorPickerFragment {
            return ColorPickerFragment()
        }
    }
}
