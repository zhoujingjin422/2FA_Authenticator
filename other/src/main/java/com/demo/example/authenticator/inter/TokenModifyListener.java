package com.demo.example.authenticator.inter;

import com.demo.example.authenticator.tokens.Token;


public interface TokenModifyListener {
    void onTokenDeleteRequested(Token token);

    void onTokenSaveRequested(Token token);

    void onTokenShareRequested(Token token);
}
