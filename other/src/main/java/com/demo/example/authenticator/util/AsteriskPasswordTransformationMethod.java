package com.demo.example.authenticator.util;

import android.text.method.PasswordTransformationMethod;
import android.view.View;


public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
    @Override 
    public CharSequence getTransformation(CharSequence charSequence, View view) {
        return new PasswordCharSequence(charSequence);
    }

    
    private class PasswordCharSequence implements CharSequence {
        private CharSequence mSource;

        @Override 
        public char charAt(int i) {
            return '*';
        }

        public PasswordCharSequence(CharSequence charSequence) {
            this.mSource = charSequence;
        }

        @Override 
        public int length() {
            return this.mSource.length();
        }

        @Override 
        public CharSequence subSequence(int i, int i2) {
            return this.mSource.subSequence(i, i2);
        }
    }
}
