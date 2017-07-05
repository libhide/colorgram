package com.ratik.colorgram;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements OnColorChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

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
            fragmentTransaction.add(R.id.slidersContainer, colorPickerFragment);
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
}
