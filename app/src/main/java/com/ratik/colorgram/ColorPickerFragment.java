package com.ratik.colorgram;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ratik on 05/07/17.
 */

public class ColorPickerFragment extends Fragment {

    @BindView(R.id.redSlider)
    AppCompatSeekBar redSlider;
    @BindView(R.id.greenSlider)
    AppCompatSeekBar greenSlider;
    @BindView(R.id.blueSlider)
    AppCompatSeekBar blueSlider;

    public ColorPickerFragment() {
        // Empty constructor
    }

    public static ColorPickerFragment getInstance() {
        return new ColorPickerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_color_select, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
