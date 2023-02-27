package com.demo.example.authenticator.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


public abstract class BaseAlertDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
    AlertDialog create;
    private final int mLayout;
    private final int mNegative;
    private final int mNeutral;
    private final int mPositive;
    private final int mTitle;

    public abstract void onViewInflated(View view);

    public BaseAlertDialogFragment(int i, int i2, int i3, int i4, int i5) {
        this.mTitle = i;
        this.mLayout = i2;
        this.mNegative = i3;
        this.mNeutral = i4;
        this.mPositive = i5;
    }

    @Override 
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        int i = this.mTitle;
        if (i != 0) {
            builder.setTitle(i);
        }
        int i2 = this.mNegative;
        if (i2 != 0) {
            builder.setNegativeButton(i2, this);
        }
        int i3 = this.mNeutral;
        if (i3 != 0) {
            builder.setNeutralButton(i3, this);
        }
        int i4 = this.mPositive;
        if (i4 != 0) {
            builder.setPositiveButton(i4, this);
        }
        View inflate = getActivity().getLayoutInflater().inflate(this.mLayout, (ViewGroup) null, false);
        onViewInflated(inflate);
        builder.setView(inflate);
        AlertDialog create = builder.create();
        this.create = create;
        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        return this.create;
    }
}
