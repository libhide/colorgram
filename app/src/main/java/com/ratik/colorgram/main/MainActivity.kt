package com.ratik.colorgram.main

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.ratik.colorgram.PREF_BLUE
import com.ratik.colorgram.PREF_GREEN
import com.ratik.colorgram.PREF_RED
import com.ratik.colorgram.R
import com.ratik.colorgram.data.PrefRepository
import com.ratik.colorgram.model.GramColor
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val PERMISSION_REQUEST_WRITE_STORAGE = 1

class MainActivity : AppCompatActivity(), OnColorChangeListener {
    private val mainViewModel: MainViewModel by viewModel()
    private var colorPickerFragment: ColorPickerFragment? = null
    private val downloadHelper: DownloadHelper by inject()
    private val prefRepository: PrefRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initLayout()
        mainViewModel.selectedColor()
                .observe(this, Observer { updateView(it) })
    }

    private fun initLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.enterTransition = Slide(Gravity.END)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else {
            setTheme(R.style.AppTheme_Fullscreen)
        }

        mainLayout.setOnLongClickListener {
            if (!mainViewModel.slidersAreVisible)
                showSliders()
            true
        }

        mainLayout.setOnClickListener {
            if (mainViewModel.slidersAreVisible)
                hideSliders()
        }
    }

    override fun onResume() {
        super.onResume()
        if (prefRepository.isFirstRun()) {
            tooltipTextView.visibility = View.VISIBLE
            prefRepository.firstRunDone()
        }
    }

    private fun updateView(color: GramColor) {
        mainLayout.setBackgroundColor(Color.rgb(color.red, color.green, color.blue))
        val saveDrawable = saveButton.drawable
        if (color.shouldOverlayColorBeWhite()) {
            saveDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        } else {
            saveDrawable.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP)
        }
        saveButton.setImageDrawable(saveDrawable)
    }

    override fun redChanged(red: Int) = mainViewModel.setRed(red)
    override fun greenChanged(green: Int) = mainViewModel.setGreen(green)
    override fun blueChanged(blue: Int) = mainViewModel.setBlue(blue)

    private fun hideSliders() {
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.bottom_up, R.anim.bottom_down)
                .remove(colorPickerFragment!!)
                .commit()
        colorPickerFragment = null
        mainViewModel.slidersAreVisible = false
    }

    private fun showSliders() {
        val colorArgs = Bundle()
        colorArgs.putInt(PREF_RED, mainViewModel.selectedColor.value!!.red)
        colorArgs.putInt(PREF_GREEN, mainViewModel.selectedColor.value!!.green)
        colorArgs.putInt(PREF_BLUE, mainViewModel.selectedColor.value!!.blue)
        colorPickerFragment = ColorPickerFragment.newInstance(colorArgs)

        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.bottom_up, R.anim.bottom_down)
                .replace(R.id.slidersContainer, colorPickerFragment!!)
                .commit()

        mainViewModel.slidersAreVisible = true

        // hide tooltip
        tooltipTextView!!.visibility = View.INVISIBLE
    }

    fun saveColorImage(view: View) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showPermissionDeniedToast()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_WRITE_STORAGE)
            }
        } else {
            downloadColor()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_WRITE_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadColor()
                } else {
                    showPermissionDeniedToast()
                }
            }
        }
    }

    private fun showPermissionDeniedToast() {
        Toast.makeText(this, "Permission denied. Can't save the color image.", Toast.LENGTH_LONG).show()
    }

    private fun downloadColor() {
        GlobalScope.launch { downloadHelper.downloadColor(mainViewModel.selectedColor.value!!) }
        Toast.makeText(this, "Saved! You can now continue working on that Instagram story!", Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        mainViewModel.saveColor()
    }

    override fun onBackPressed() {
        if (mainViewModel.slidersAreVisible) hideSliders()
        else super.onBackPressed()
    }
}
