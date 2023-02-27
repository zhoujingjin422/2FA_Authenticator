package com.demo.example.authenticator.ui;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.core.content.ContextCompat;

import com.demo.example.authenticator.util.FontUtil;

import com.demo.example.R;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.navigation.NavigationView;


public class PopupView {
    private final Activity mContext;
    private final NavigationView.OnNavigationItemSelectedListener mListener;
    private int mMeasuredHeight;
    private PopupWindow mPopupWindow;
    private ViewGroup mRootView;

    public PopupView(Activity activity, int i, NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener) {
        this.mContext = activity;
        this.mListener = onNavigationItemSelectedListener;
        ViewGroup viewGroup = (ViewGroup) ((LayoutInflater) activity.getSystemService("layout_inflater")).inflate(i, (ViewGroup) null, false);
        this.mRootView = viewGroup;
        viewGroup.measure(-2, -2);
        this.mMeasuredHeight = this.mRootView.getMeasuredHeight();
        PopupWindow popupWindow = new PopupWindow(this.mRootView, -2, this.mMeasuredHeight);
        this.mPopupWindow = popupWindow;
        popupWindow.setOutsideTouchable(true);
        this.mPopupWindow.setFocusable(true);
        this.mPopupWindow.setWindowLayoutMode(-2, -2);
        if (Build.VERSION.SDK_INT >= 21) {
            this.mPopupWindow.setBackgroundDrawable(new ColorDrawable(-1));
            this.mPopupWindow.setElevation(8.0f);
        } else {
            this.mPopupWindow.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.bg_popup_compat));
        }
        NavigationView navigationView = (NavigationView) this.mRootView.findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() { 
            @Override
            
            public final boolean onNavigationItemSelected(MenuItem menuItem) {
                return PopupView.this.lambda$new$0$PopupView(menuItem);
            }
        });
        FontUtil.setTypefaceOnMenu(navigationView.getMenu());
    }

    public boolean lambda$new$0$PopupView(MenuItem menuItem) {
        this.mPopupWindow.dismiss();
        this.mListener.onNavigationItemSelected(menuItem);
        return true;
    }

    public void show(View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.mContext.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int[] iArr = new int[2];
        view.getLocationOnScreen(iArr);
        iArr[1] = (int) (((float) iArr[1]) - ((displayMetrics.density * 24.0f) + 0.5f));
        Point point = new Point();
        this.mContext.getWindowManager().getDefaultDisplay().getSize(point);
        int height = ((float) iArr[1]) < ((float) point.y) * 0.5f ? 0 : (-this.mMeasuredHeight) - view.getHeight();
        if (Build.VERSION.SDK_INT >= 19) {
            this.mPopupWindow.showAsDropDown(view, 0, height, BadgeDrawable.BOTTOM_END);
        } else {
            this.mPopupWindow.showAsDropDown(view);
        }
    }

    public Menu getMenu() {
        return ((NavigationView) this.mRootView.findViewById(R.id.navigation_view)).getMenu();
    }
}
