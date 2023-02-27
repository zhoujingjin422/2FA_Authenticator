package com.demo.example.authenticator.tokens;


public class MalformedTokenException extends Exception {
    public MalformedTokenException(String str) {
        super(str);
    }

    public MalformedTokenException(String str, Throwable th) {
        super(str, th);
    }
}
