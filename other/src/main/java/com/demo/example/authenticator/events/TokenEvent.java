package com.demo.example.authenticator.events;


public class TokenEvent {
    public final long id;
    public final String uri;

    public TokenEvent(String str) {
        this.uri = str;
        this.id = -1;
    }

    public TokenEvent(String str, long j) {
        this.uri = str;
        this.id = j;
    }
}
