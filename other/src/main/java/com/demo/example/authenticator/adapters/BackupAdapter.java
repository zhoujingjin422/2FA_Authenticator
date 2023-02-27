package com.demo.example.authenticator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.demo.example.R;

import com.demo.example.authenticator.inter.BackupActionListener;


public class BackupAdapter extends ArrayAdapter<String> {
    private final LayoutInflater mLayoutInflater;
    private final BackupActionListener mListener;

    public BackupAdapter(Context context, BackupActionListener backupActionListener) {
        super(context, R.layout.li_restore);
        this.mLayoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.mListener = backupActionListener;
    }

    @Override 
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = this.mLayoutInflater.inflate(R.layout.li_restore, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.bind(i, getItem(i), this.mListener);
        return view;
    }

    
    private static class ViewHolder {
        ImageButton mBtOverflow;
        View mRootView;
        TextView mTextView;

        ViewHolder(View view) {
            this.mRootView = view;
            this.mTextView = (TextView) view.findViewById(16908308);
            this.mBtOverflow = (ImageButton) view.findViewById(R.id.bt_overflow);
        }

        public void bind(final int i, final String str, final BackupActionListener backupActionListener) {
            this.mTextView.setText(str);
            this.mRootView.setOnClickListener(new View.OnClickListener() { 
                @Override 
                public void onClick(View view) {
                    backupActionListener.onBackupSelected(i, str);
                }
            });
            this.mBtOverflow.setOnClickListener(new View.OnClickListener() { 
                @Override 
                public void onClick(View view) {
                    backupActionListener.onDeleteBackupRequested(i, str);
                }
            });
            this.mBtOverflow.setFocusable(false);
            this.mBtOverflow.setFocusableInTouchMode(false);
        }
    }
}
