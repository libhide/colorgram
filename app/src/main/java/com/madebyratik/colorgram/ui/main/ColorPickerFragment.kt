package com.madebyratik.colorgram.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.madebyratik.colorgram.APP_BLUE
import com.madebyratik.colorgram.APP_GREEN
import com.madebyratik.colorgram.APP_RED
import com.madebyratik.colorgram.PREF_BLUE
import com.madebyratik.colorgram.PREF_GREEN
import com.madebyratik.colorgram.PREF_RED
import com.madebyratik.colorgram.databinding.FragmentColorSelectBinding

class ColorPickerFragment : Fragment() {
    private var binding: FragmentColorSelectBinding? = null
    private lateinit var colorChangeListener: OnColorChangeListener

    private val onSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            when (seekBar.tag) {
                "red" -> colorChangeListener.redChanged(progress)
                "green" -> colorChangeListener.greenChanged(progress)
                "blue" -> colorChangeListener.blueChanged(progress)
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

        override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        colorChangeListener = context as? OnColorChangeListener
            ?: throw ClassCastException("$context must implement ${ColorPickerFragment::class.java.simpleName}")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentColorSelectBinding.inflate(inflater, container, false)
            .also { binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = requireNotNull(binding)
        binding.redSlider.progress = arguments?.getInt(PREF_RED) ?: APP_RED
        binding.greenSlider.progress = arguments?.getInt(PREF_GREEN) ?: APP_GREEN
        binding.blueSlider.progress = arguments?.getInt(PREF_BLUE) ?: APP_BLUE

        listOf(binding.redSlider, binding.greenSlider, binding.blueSlider).forEach {
            it.setOnSeekBarChangeListener(onSeekBarChangeListener)
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance(args: Bundle) = ColorPickerFragment().apply {
            arguments = args
        }
    }
}
