package com.demo.example.authenticator.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;

import com.demo.example.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class IntroFragment extends Fragment {
    private int image;
    ImageView imageView;

    public IntroFragment() {
    }

    public IntroFragment(int i) {
        this.image = i;
    }

    public static IntroFragment newInstance(int i) {
        return new IntroFragment(i);
    }

    @Override 
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_intro, viewGroup, false);
        this.imageView = (ImageView) inflate.findViewById(R.id.image);
        Picasso.with(getContext()).load(Integer.valueOf(this.image).intValue()).into(this.imageView, new Callback() { 
            @Override 
            public void onError() {
            }

            @Override 
            public void onSuccess() {
            }
        });
        return inflate;
    }
}
