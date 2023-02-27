package com.demo.example.authenticator.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.demo.example.R;

import com.demo.example.authenticator.util.FontUtil;


public class FontSpinnerAdapter extends ArrayAdapter<String> {
    public FontSpinnerAdapter(Context context, int i) {
        super(context, (int) R.layout.li_spinner, context.getResources().getStringArray(i));
    }

    @Override 
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView textView = (TextView) super.getView(i, view, viewGroup);
        textView.setTextColor(getContext().getResources().getColor(R.color.text_black));
        FontUtil.setTypeface(textView);
        return textView;
    }

    @Override
    
    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        TextView textView = (TextView) super.getDropDownView(i, view, viewGroup);
        FontUtil.setTypeface(textView);
        return textView;
    }
}
