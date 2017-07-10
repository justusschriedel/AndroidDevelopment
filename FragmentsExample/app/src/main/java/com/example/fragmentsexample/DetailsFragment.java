package com.example.fragmentsexample;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class DetailsFragment extends android.support.v4.app.Fragment {
    //creates a new instance of DetailsFragment initialized to show text
    //at index
    public static DetailsFragment newInstance(int index) {
        DetailsFragment f = new DetailsFragment();

        //supplies index input as argument to new fragment instance
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }

    //gets index of details currently being shown
    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    //callback method for when UI is being drawn after title selections
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //device is in portrait mode, so null is returned (this frame doesn't exist)
        if (container == null) {
            return null;
        }

        //creates scrolling view of details in a textview object; also sets padding and dimension
        ScrollView scroller = new ScrollView(getActivity());
        TextView text = new TextView(getActivity());
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                getActivity().getResources().getDisplayMetrics());
        text.setPadding(padding, padding, padding, padding);
        scroller.addView(text);
        text.setText(Shakespeare.DIALOGUE[getShownIndex()]);
        return scroller;
    }
}
