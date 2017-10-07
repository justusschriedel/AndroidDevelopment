package com.example.fragmentsexample;

import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static java.lang.reflect.Array.getLength;

public class TitlesFragment extends ListFragment {
    boolean mDualPane;
    int mCurCheckPosition = 0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //puts play titles from Shakespeare class into list
        setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_activated_1, Shakespeare.TITLES));

        //checks to see if there's another frame to that the details fragment can
        //be displayed in
        View detailsFrame = getActivity().findViewById(R.id.details); //checks activity for id
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            //if savedInstanceState is not null, there's an existing state waiting to be
            //restored; that's what this does
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }

        if (mDualPane) { //checks if there's multiple frames (or in landscape mode)
            //listview highlights selected item in dual-pane mode
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            //make sure UI is in correct state
            showDetails(mCurCheckPosition);
        }
    }

    //saves current state before app is killed so it can come back
    //to current state when user returns
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("curChoice", mCurCheckPosition); //saving current selecting in bundle
    }

    //what happens when an item in the list is clicked
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showDetails(position);
    }

    //function shows details of selected item either by displaying a fragment with the details in
    // current UI (portrait) or starting new activity where it's displayed (landscape)
    void showDetails(int index) {
        mCurCheckPosition = index;
        int lastEl = getLength(Shakespeare.TITLES) - 1;

        if (mDualPane) {
            //everything can be displayed in-place (because device is in landscape), so this
            //updates the list to highlight selected title and display it in other fragment
            getListView().setItemChecked(index, true);

            //check which fragment is currently being displayed and replace if needed
            DetailsFragment details = (DetailsFragment)
                    getFragmentManager().findFragmentById(R.id.details);
            if (details == null || details.getShownIndex() != index) {
                //details shown doesn't match selection; new fragment is made to reflect new selection
                details = DetailsFragment.newInstance(index);

                //initiates fragment transaction to change any existing fragment into this
                //fragment inside frame
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                if (index == 0) {
                    ft.replace(R.id.details, details);
                }
                else if (index == lastEl) {
                    Uri bib = Uri.parse(Shakespeare.DIALOGUE[lastEl]);
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, bib);
                    startActivity(webIntent);
                }
                else {
                    ft.replace(R.id.details, details);
                }
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        }
        else {
            //device is in portrait mode, which means a new activity must be started to display
            //title details; that's what's happening here
            if (index == lastEl) {
                Uri bib = Uri.parse(Shakespeare.DIALOGUE[lastEl]);
                Intent webIntent = new Intent(Intent.ACTION_VIEW, bib);
                startActivity(webIntent);
            }
            else {
                Intent intent = new Intent();
                intent.setClass(getActivity(), DetailsActivity.class);
                intent.putExtra("index", index);
                startActivity(intent);
            }
        }
    }
}
