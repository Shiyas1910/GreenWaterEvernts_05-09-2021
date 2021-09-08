package com.greenwaterevents.android.greenwaterevents.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.greenwaterevents.android.greenwaterevents.R;


/**
 * Created by Mohamed Shiyas on 17-02-2018.
 */

public class AboutUsFragment extends Fragment {

    public static AboutUsFragment newInstance() {
        return new AboutUsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about_us, container, false);

        TextView textView = v.findViewById(R.id.websiteLink);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
//        String text = "Visit our website <a href=\"http://greenwaterevents.com/\">Green Water Events</a>";
//        textView.setText(Html.fromHtml(text));

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("About Us");
    }

    @Override
    public void onDestroy() {
        Runtime.getRuntime().gc();
        super.onDestroy();
    }

}
