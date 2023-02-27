package com.demo.example.authenticator.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.demo.example.R;

import com.demo.example.authenticator.fragments.IntroFragment;
import java.util.ArrayList;
import java.util.List;


public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private List<Integer> imageList;

    public SectionsPagerAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        ArrayList arrayList = new ArrayList();
        this.imageList = arrayList;
        arrayList.add(Integer.valueOf((int) R.drawable.d1));
        this.imageList.add(Integer.valueOf((int) R.drawable.d2));
        this.imageList.add(Integer.valueOf((int) R.drawable.d3));
        this.imageList.add(Integer.valueOf((int) R.drawable.d4));
        this.imageList.add(Integer.valueOf((int) R.drawable.d5));
        this.imageList.add(Integer.valueOf((int) R.drawable.d6));
        this.imageList.add(Integer.valueOf((int) R.drawable.d7));
        this.imageList.add(Integer.valueOf((int) R.drawable.d8));
    }

    @Override 
    public Fragment getItem(int i) {
        return IntroFragment.newInstance(this.imageList.get(i).intValue());
    }

    @Override 
    public int getCount() {
        return this.imageList.size();
    }
}
