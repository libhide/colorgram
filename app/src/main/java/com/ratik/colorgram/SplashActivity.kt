package com.ratik.colorgram

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity

private const val SPLASH_DURATION: Long = 1500

class SplashActivity : AppCompatActivity() {
    private lateinit var splashHandler: Handler

    private val splashRunnable: Runnable = Runnable {
        if (!isFinishing) {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashHandler = Handler()
        splashHandler.postDelayed(splashRunnable, SPLASH_DURATION)
    }

    public override fun onDestroy() {
        super.onDestroy()
        splashHandler.removeCallbacks(splashRunnable)
    }
}