package com.demo.example.authenticator.inter;

import android.content.Context;
import com.demo.example.authenticator.tokens.Token;


public interface TokenActionListener {
    void confirmDelete(Context context, Token token, int i);

    void copyToClipboard(Token token, int i);

    void onNewHOTPRequested(Token token);

    void setHiddenState(Token token, int i, boolean z);

    void share(Context context, Token token, int i);
}
