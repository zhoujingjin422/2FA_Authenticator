package com.demo.example.authenticator.adapters;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.example.authenticator.AddSetupKeyActivity;
import com.demo.example.authenticator.inter.TokenActionListener;
import com.demo.example.authenticator.tokens.Token;
import com.demo.example.authenticator.tokens.TokenType;
import com.demo.example.authenticator.ui.PopupView;
import com.demo.example.authenticator.ui.ProgressTextView;
import com.demo.example.authenticator.util.color.ColorIcon;
import com.demo.example.authenticator.util.icon.AppIconRequestHandler;

import com.demo.example.R;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.TreeSet;


public class TokenViewHolder extends RecyclerView.ViewHolder {
    private ImageView linCopy;
    private ImageView mBtRefresh;
    private ProgressTextView mCode;
    private TextView mIssuer;
    private ImageView mIvIcon;
    private TextView mLabel;
    private ImageView mOverflow;
    private ViewGroup mRoot;
    private Token mToken;
    private TextView mTvIcon;
    LinearLayout mcvClick;

    
    
    public static class Ticker extends Handler {
        private final TokenViewHolder mHolder;
        private final WeakReference<View> mReference;

        public Ticker(TokenViewHolder tokenViewHolder, View view) {
            this.mReference = new WeakReference<>(view);
            this.mHolder = tokenViewHolder;
        }

        @Override 
        public void handleMessage(Message message) {
            if (this.mReference.get() != null) {
                this.mHolder.updateProgress();
                start();
            }
        }

        public void start() {
            stop();
            sendEmptyMessageDelayed(0, 100);
        }

        public void stop() {
            removeMessages(0);
        }
    }

    public TokenViewHolder(View view) {
        super(view);
        this.mRoot = (ViewGroup) view;
        this.mCode = (ProgressTextView) view.findViewById(R.id.code);
        this.mLabel = (TextView) view.findViewById(R.id.label);
        this.mIssuer = (TextView) view.findViewById(R.id.issuer);
        this.mBtRefresh = (ImageView) view.findViewById(R.id.bt_refresh);
        this.linCopy = (ImageView) view.findViewById(R.id.img_copy);
        this.mIvIcon = (ImageView) view.findViewById(R.id.app_icon);
        this.mTvIcon = (TextView) view.findViewById(R.id.tv_icon);
        this.mOverflow = (ImageView) view.findViewById(R.id.bt_overflow);
        this.mcvClick = (LinearLayout) view.findViewById(R.id.token_content);
    }

    public void bind(final Token token, final TokenActionListener tokenActionListener, Picasso picasso, TreeSet<Long> treeSet) {
        String str;
        final Context context = this.mOverflow.getContext();
        this.mToken = token;
        if (token.isHidden()) {
            this.mRoot.setAlpha(0.5f);
        } else {
            this.mRoot.setAlpha(1.0f);
        }
        this.mCode.setText(token.getCode());
        if (!TextUtils.isEmpty(token.getLabel())) {
            TextView textView = this.mLabel;
            textView.setText(" - " + token.getLabel());
        } else {
            this.mLabel.setText("");
        }
        this.mCode.setProgress(((float) token.getProgress()) * 0.1f);
        if (TextUtils.isEmpty(token.getIssuer())) {
            str = token.getLabel();
            this.mLabel.setVisibility(View.GONE);
        } else {
            str = token.getIssuer();
            this.mLabel.setVisibility(View.VISIBLE);
        }
        this.mIssuer.setText(str);
        if (treeSet.contains(Long.valueOf(token.getId()))) {
            bindTextIcon(str);
        } else {
            bindAppIcon(picasso, token, str, treeSet);
        }
        if (token.getType() == TokenType.HOTP) {
            this.mCode.setmColorBg(context.getResources().getColor(R.color.colorTextBlue));
            this.mBtRefresh.setOnClickListener(new View.OnClickListener() { 
                @Override 
                public void onClick(View view) {
                    tokenActionListener.onNewHOTPRequested(token);
                    TokenViewHolder.this.showNewHOTP(token);
                    ObjectAnimator.ofFloat(view, "rotation", 360.0f, 0.0f).setDuration(500L).start();
                }
            });
        } else {
            this.mBtRefresh.setOnClickListener(null);
        }
        this.mcvClick.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                Intent intent = new Intent(context, AddSetupKeyActivity.class);
                intent.putExtra("edit_token", token);
                context.startActivity(intent);
            }
        });
        this.mOverflow.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                PopupView popupView = new PopupView((Activity) context, R.layout.popup, new NavigationView.OnNavigationItemSelectedListener() { 
                    private final TokenActionListener f$1;
                    private final Token f$2;

                    {
                        this.f$1 = tokenActionListener;
                        this.f$2 = token;
                    }

                    @Override
                    
                    public final boolean onNavigationItemSelected(MenuItem menuItem) {
                        return TokenViewHolder.this.lambda$showPopup$2$TokenViewHolder(this.f$1, this.f$2, menuItem);
                    }
                });
                token.isHidden();
                popupView.show(TokenViewHolder.this.mOverflow);
            }
        });
        this.linCopy.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                tokenActionListener.copyToClipboard(token, TokenViewHolder.this.getAdapterPosition());
            }
        });
        if (token.getType() == TokenType.TOTP) {
            this.mBtRefresh.setVisibility(View.GONE);
        } else {
            this.mBtRefresh.setVisibility(View.VISIBLE);
        }
        new Ticker(this, this.mRoot).start();
    }

    public boolean lambda$showPopup$2$TokenViewHolder(TokenActionListener tokenActionListener, Token token, MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.action_delete) {
            tokenActionListener.confirmDelete(this.mRoot.getContext(), token, getAdapterPosition());
            return true;
        } else if (itemId != R.id.action_show_qr) {
            return false;
        } else {
            tokenActionListener.share(this.mRoot.getContext(), token, getAdapterPosition());
            return false;
        }
    }

    private void bindAppIcon(Picasso picasso, final Token token, final String str, final TreeSet<Long> treeSet) {
        String str2;
        if (TextUtils.isEmpty(token.getIssuer())) {
            str2 = token.getLabel();
        } else {
            str2 = token.getIssuer();
        }
        String createUriString = AppIconRequestHandler.createUriString(str2);
        this.mTvIcon.setVisibility(View.GONE);
        picasso.load(createUriString).into(this.mIvIcon, new Callback() { 
            @Override 
            public void onSuccess() {
                treeSet.remove(Long.valueOf(token.getId()));
            }

            @Override 
            public void onError() {
                treeSet.add(Long.valueOf(token.getId()));
                TokenViewHolder.this.bindTextIcon(str);
            }
        });
    }

    public void bindTextIcon(String str) {
        new ColorIcon(str.trim()).apply(this.mTvIcon);
        this.mTvIcon.setVisibility(View.VISIBLE);
    }

    public void updateProgress() {
        Token token = this.mToken;
        if (token != null) {
            this.mCode.setText(token.getCode());
            int progress = this.mToken.getProgress();
            this.mCode.setProgress(((float) progress) * 0.1f);
            if (progress > 0 && progress < 950) {
                this.mRoot.setEnabled(true);
            }
            if (this.mToken.getType() == TokenType.HOTP) {
                this.mBtRefresh.setVisibility(View.VISIBLE);
            }
        }
    }

    
    public void showNewHOTP(Token token) {
        this.mCode.setText(token.getCode());
    }
}
