package com.madebyratik.colorgram.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.madebyratik.colorgram.PREF_BLUE
import com.madebyratik.colorgram.PREF_GREEN
import com.madebyratik.colorgram.PREF_RED
import com.madebyratik.colorgram.R
import com.madebyratik.colorgram.data.PrefRepository
import com.madebyratik.colorgram.databinding.ActivityMainBinding
import com.madebyratik.colorgram.model.GramColor
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val PERMISSION_REQUEST_WRITE_STORAGE = 1

class MainActivity : AppCompatActivity(), OnColorChangeListener {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModel()
    private var colorPickerFragment: ColorPickerFragment? = null
    private val prefRepository: PrefRepository by inject()
    private val slidersBackCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() = hideSliders()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBackPressedDispatcher.addCallback(this, slidersBackCallback)
        initLayout()
        mainViewModel.selectedColor.observe(this, ::updateView)
    }

    private fun initLayout() {
        window.enterTransition = Slide(Gravity.END)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val initialPaddingTop = binding.mainLayout.paddingTop
        ViewCompat.setOnApplyWindowInsetsListener(binding.mainLayout) { view, windowInsets ->
            val topInset = windowInsets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout(),
            ).top
            view.setPadding(
                view.paddingLeft,
                initialPaddingTop + topInset,
                view.paddingRight,
                view.paddingBottom,
            )
            windowInsets
        }
        ViewCompat.requestApplyInsets(binding.mainLayout)

        binding.mainLayout.setOnLongClickListener {
            if (!mainViewModel.slidersAreVisible) {
                showSliders()
            }
            true
        }

        binding.mainLayout.setOnClickListener {
            if (mainViewModel.slidersAreVisible) {
                hideSliders()
            }
        }
        binding.saveButton.setOnClickListener { saveColorImage() }
    }

    override fun onResume() {
        super.onResume()
        if (prefRepository.isFirstRun()) {
            binding.tooltipTextView.visibility = View.VISIBLE
            prefRepository.firstRunDone()
        }
    }

    private fun updateView(color: GramColor) {
        binding.mainLayout.setBackgroundColor(Color.rgb(color.red, color.green, color.blue))
        val saveDrawable = binding.saveButton.drawable
        if (color.shouldOverlayColorBeWhite()) {
            saveDrawable.setTint(Color.WHITE)
        } else {
            saveDrawable.setTint(Color.BLACK)
        }
        binding.saveButton.setImageDrawable(saveDrawable)
    }

    override fun redChanged(red: Int) = mainViewModel.setRed(red)
    override fun greenChanged(green: Int) = mainViewModel.setGreen(green)
    override fun blueChanged(blue: Int) = mainViewModel.setBlue(blue)

    private fun hideSliders() {
        val fragment = colorPickerFragment ?: return
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.bottom_up, R.anim.bottom_down)
            .remove(fragment)
            .commit()
        colorPickerFragment = null
        mainViewModel.slidersAreVisible = false
        slidersBackCallback.isEnabled = false
    }

    private fun showSliders() {
        val color = mainViewModel.selectedColor.value ?: return
        val colorArgs = Bundle().apply {
            putInt(PREF_RED, color.red)
            putInt(PREF_GREEN, color.green)
            putInt(PREF_BLUE, color.blue)
        }
        val fragment = ColorPickerFragment.newInstance(colorArgs)
        colorPickerFragment = fragment

        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.bottom_up, R.anim.bottom_down)
            .replace(R.id.slidersContainer, fragment)
            .commit()

        mainViewModel.slidersAreVisible = true
        slidersBackCallback.isEnabled = true
        binding.tooltipTextView.visibility = View.INVISIBLE
    }

    private fun saveColorImage() {
        if (
            Build.VERSION.SDK_INT <= Build.VERSION_CODES.P &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showPermissionDeniedToast()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_WRITE_STORAGE,
                )
            }
        } else {
            downloadColor()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_WRITE_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadColor()
            } else {
                showPermissionDeniedToast()
            }
        }
    }

    private fun showPermissionDeniedToast() {
        Toast.makeText(this, "Permission denied. Can't save the color image.", Toast.LENGTH_LONG).show()
    }

    private fun downloadColor() {
        mainViewModel.downloadColor()
        Toast.makeText(this, "Saved! You can now continue working on that Instagram story!", Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        mainViewModel.saveColor()
    }

}
