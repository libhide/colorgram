package com.ratik.colorgram;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Ratik on 05/07/17.
 */

public class ColorPickerFragment extends Fragment {

    public ColorPickerFragment() {
        // Empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_color_select, container, false);
        return rootView;
    }
}
