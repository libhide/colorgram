package com.ratik.colorgram

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
import androidx.lifecycle.Observer
import com.ratik.colorgram.model.GramColor
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), OnColorChangeListener {

    val PERMISSION_REQUEST_WRITE_STORAGE = 1

    private val mainViewModel: MainViewModel by viewModel()

    private var isFirstRun: Boolean = false
    private var slidersAreVisible = false
    private var colorPickerFragment: ColorPickerFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initLayout()
        mainViewModel.observeSelectedColor()
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
            if (!slidersAreVisible) {
                showSliders()
            }
            true
        }

        mainLayout.setOnClickListener {
            if (slidersAreVisible) {
                hideSliders()
            }
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

    private fun hideSliders() {
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.bottom_up, R.anim.bottom_down)
                .remove(colorPickerFragment!!)
                .commit()
        colorPickerFragment = null
        slidersAreVisible = false
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

        slidersAreVisible = true

        // hide tooltip
        tooltipTextView!!.visibility = View.INVISIBLE
    }

    override fun colorChanged(red: Int, green: Int, blue: Int) {
        mainViewModel.selectedColor.value = GramColor(red, green, blue)
    }

    fun saveColorImage(view: View) {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission
//                        .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                Toast.makeText(this, "Permission denied. Can't save the color image.",
//                        Toast.LENGTH_LONG).show()
//            } else {
//                // Request the permission.
//                ActivityCompat.requestPermissions(this,
//                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                        MY_PERMISSIONS_REQUEST_WRITE_STORAGE)
//            }
//        } else {
//            saveColor()
//        }
        Toast.makeText(this, "Feature coming soon", Toast.LENGTH_LONG).show()
    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
//                                            grantResults: IntArray) {
//        when (requestCode) {
//            MY_PERMISSIONS_REQUEST_WRITE_STORAGE -> {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission was granted, yay!
//                    saveColor()
//                } else {
//                    // permission denied, boo!
//                    Toast.makeText(this, "Permission denied. Can't save the color image.",
//                            Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//    }

//    private fun saveColor() {
//        val bitmap = Bitmap.createBitmap(1080, 1920, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bitmap)
//        canvas.drawARGB(255, red, green, blue)
//
//        val filename = String.format("%d_%d_%d", red, green, blue) + ".jpg"
//        val sdCard = Environment.getExternalStorageDirectory().absolutePath
//        val destDir = File(sdCard + "/" + getString(R.string.app_name))
//        destDir.mkdirs()
//        val file = File(destDir, filename)
//
//        try {
//            val out = FileOutputStream(file)
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
//            out.flush()
//            out.close()
//            Toast.makeText(this, "Saved! You can now continue " + "working on that Instagram story!", Toast.LENGTH_SHORT).show()
//        } catch (e: Exception) {
//            Log.e(TAG, "Error: " + e.message)
//        }
//
//        // make sure the image appears in the gallery
//        val scanFileIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
//                Uri.fromFile(file))
//        sendBroadcast(scanFileIntent)
//    }

    override fun onBackPressed() {
        if (slidersAreVisible) hideSliders()
        else super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        mainViewModel.saveColor()
    }
}
