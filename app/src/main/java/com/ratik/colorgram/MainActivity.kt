package com.ratik.colorgram

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.transition.Slide
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity(), OnColorChangeListener {

    private var red: Int = 0
    private var green: Int = 0
    private var blue: Int = 0

    private var isFirstRun: Boolean = false
    private var slidersAreVisible = false

    lateinit var sharedPrefs: SharedPreferences
    var colorPickerFragment: ColorPickerFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initLayout()

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

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
    }

    private fun initLayout() {
        // Add visual treat for L+ devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.enterTransition = Slide(Gravity.END)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else {
            setTheme(R.style.AppTheme_Fullscreen)
        }
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
        colorArgs.putInt(PREF_RED, red)
        colorArgs.putInt(PREF_GREEN, green)
        colorArgs.putInt(PREF_BLUE, blue)
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
        this.red = red
        this.green = green
        this.blue = blue

        // update view
        setViewCorrectly()

        // set background color
        val color = Color.rgb(red, green, blue)
        mainLayout.setBackgroundColor(color)
    }

    private fun setViewCorrectly() {
        val saveDrawable = saveButton.drawable
        if (shouldSaveButtonBeWhite()) {
            saveDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        } else {
            saveDrawable.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP)
        }
        saveButton.setImageDrawable(saveDrawable)
    }


    private fun shouldSaveButtonBeWhite(): Boolean {
        val y = 0.2126 * red + 0.7152 * green + 0.0722 * blue
        return y < 200
    }

    fun saveColorImage(view: View) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission
                        .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "Permission denied. Can't save the color image.",
                        Toast.LENGTH_LONG).show()
            } else {
                // Request the permission.
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_WRITE_STORAGE)
            }
        } else {
            saveColor()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    saveColor()
                } else {
                    // permission denied, boo!
                    Toast.makeText(this, "Permission denied. Can't save the color image.",
                            Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun saveColor() {
        val bitmap = Bitmap.createBitmap(1080, 1920, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawARGB(255, red, green, blue)

        val filename = String.format("%d_%d_%d", red, green, blue) + ".jpg"
        val sdCard = Environment.getExternalStorageDirectory().absolutePath
        val destDir = File(sdCard + "/" + getString(R.string.app_name))
        destDir.mkdirs()
        val file = File(destDir, filename)

        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
            Toast.makeText(this, "Saved! You can now continue " + "working on that Instagram story!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e(TAG, "Error: " + e.message)
        }

        // make sure the image appears in the gallery
        val scanFileIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(file))
        sendBroadcast(scanFileIntent)
    }

    override fun onResume() {
        super.onResume()

        red = sharedPrefs.getInt(PREF_RED, APP_RED)
        green = sharedPrefs.getInt(PREF_GREEN, APP_GREEN)
        blue = sharedPrefs.getInt(PREF_BLUE, APP_BLUE)

        // update view
        setViewCorrectly()

        val color = Color.rgb(red, green, blue)
        mainLayout!!.setBackgroundColor(color)

        isFirstRun = sharedPrefs.getBoolean("first_run", true)
        if (isFirstRun) {
            // tooltip
            tooltipTextView!!.visibility = View.VISIBLE

            isFirstRun = false
            val editor = sharedPrefs.edit()
            editor.putBoolean("first_run", isFirstRun)
            editor.apply()
        } else {
            tooltipTextView!!.visibility = View.INVISIBLE
        }
    }

    override fun onBackPressed() {
        if (slidersAreVisible) {
            hideSliders()
        } else {
            super.onBackPressed()
        }
    }

    override fun onPause() {
        super.onPause()

        val editor = sharedPrefs.edit()
        editor.putInt(PREF_RED, red)
        editor.putInt(PREF_GREEN, green)
        editor.putInt(PREF_BLUE, blue)
        editor.apply()
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private val MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1
    }
}
