package com.ratik.colorgram;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements OnColorChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 001;

    // Constants
    private boolean slidersAreVisible = false;

    // Views
    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;

    // Helpers
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ColorPickerFragment colorPickerFragment;
    private int red, green, blue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        fragmentManager = getSupportFragmentManager();
        colorPickerFragment = ColorPickerFragment.getInstance();

        // Add visual treat for L+ devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Slide(Gravity.END));
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            setTheme(R.style.AppTheme_Fullscreen);
        }
    }

    public void toggleSliders(View view) {
        if (slidersAreVisible) {
            // hide slider fragment
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.bottom_up,
                    R.anim.bottom_down);
            fragmentTransaction.remove(colorPickerFragment);
            fragmentTransaction.commit();
            slidersAreVisible = false;
        } else {
            // show slider fragment
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.bottom_up,
                    R.anim.bottom_down);
            fragmentTransaction.replace(R.id.slidersContainer, colorPickerFragment);
            fragmentTransaction.commit();
            slidersAreVisible = true;
        }
    }

    @Override
    public void colorChanged(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        int color = Color.rgb(red, green, blue);
        mainLayout.setBackgroundColor(color);
    }

    public void saveColorImage(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission
                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "Permission denied. Can't save the color image.",
                        Toast.LENGTH_LONG).show();
            } else {
                // Request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
            }
        } else {
            saveColor();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    saveColor();
                } else {
                    // permission denied, boo!
                    Toast.makeText(this, "Permission denied. Can't save the color image.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void saveColor() {
        Bitmap bitmap = Bitmap.createBitmap(1080, 1920, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawARGB(255, red, green, blue);

        String filename = String.format("%d_%d_%d", red, green, blue) + ".jpg";
        String sdCard = Environment.getExternalStorageDirectory().getAbsolutePath();
        File destDir = new File(sdCard + "/" + getString(R.string.app_name));
        destDir.mkdirs();
        File file = new File(destDir, filename);

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Toast.makeText(this, "Saved! You can now continue " +
                    "working on that Instagram story!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
        }

        // make sure the image appears in the gallery
        Intent scanFileIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(file));
        sendBroadcast(scanFileIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        red = prefs.getInt("red", 66);
        green = prefs.getInt("green", 134);
        blue = prefs.getInt("blue", 245);
        int color = Color.rgb(red, green, blue);
        mainLayout.setBackgroundColor(color);
    }

    @Override
    public void onBackPressed() {
        if (slidersAreVisible) {
            // hide slider fragment
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.bottom_up,
                    R.anim.bottom_down);
            fragmentTransaction.remove(colorPickerFragment);
            fragmentTransaction.commit();
            slidersAreVisible = false;
        } else {
            super.onBackPressed();
        }
    }
}
