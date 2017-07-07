package com.example.pictures;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ListOfPicturesFrag extends Fragment {

    OnPictureSelectedListener mCallback;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.listofpictures, container, false);
    }

    public interface OnPictureSelectedListener {
        public void onPictureSelected(int position);

    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

        try {
            mCallback = (OnPictureSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "must implement OnPictureSelectedListener");
        }
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        mCallback.onPictureSelected(position);
    }
}
