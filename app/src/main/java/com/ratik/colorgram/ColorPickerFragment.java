package com.ratik.colorgram;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ratik on 05/07/17.
 */

public class ColorPickerFragment extends Fragment {

    // Constants
    private static final String TAG = ColorPickerFragment.class.getSimpleName();

    // Views
    @BindView(R.id.redSlider)
    AppCompatSeekBar redSlider;
    @BindView(R.id.greenSlider)
    AppCompatSeekBar greenSlider;
    @BindView(R.id.blueSlider)
    AppCompatSeekBar blueSlider;

    // Helpers
    private OnColorChangeListener colorChangeListener;
    private int red;
    private int green;
    private int blue;

    public ColorPickerFragment() {
        // Empty constructor
    }

    public static ColorPickerFragment getInstance() {
        return new ColorPickerFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            colorChangeListener = (OnColorChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnColorChangeListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_color_select, container, false);
        ButterKnife.bind(this, rootView);

        redSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                red = i;
                colorChangeListener.colorChanged(red, green, blue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        greenSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                green = i;
                colorChangeListener.colorChanged(red, green, blue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        blueSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                blue = i;
                colorChangeListener.colorChanged(red, green, blue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("red", red);
        editor.putInt("green", green);
        editor.putInt("blue", blue);
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        red = prefs.getInt("red", 66);
        green = prefs.getInt("green", 134);
        blue = prefs.getInt("blue", 245);

        // set default positions for the sliders
        redSlider.setProgress(red);
        greenSlider.setProgress(green);
        blueSlider.setProgress(blue);
    }
}
