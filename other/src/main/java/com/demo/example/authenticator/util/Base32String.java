package com.demo.example.authenticator.util;

import java.util.HashMap;
import java.util.Locale;


public class Base32String {
    private static final Base32String INSTANCE = new Base32String("ABCDEFGHIJKLMNOPQRSTUVWXYZ234567");
    private String ALPHABET;
    private HashMap<Character, Integer> CHAR_MAP = new HashMap<>();
    private char[] DIGITS;
    private int MASK;
    private int SHIFT;

    
    public static class DecodingException extends Exception {
        public DecodingException(String str) {
            super(str);
        }
    }

    protected Base32String(String str) {
        this.ALPHABET = str;
        char[] charArray = str.toCharArray();
        this.DIGITS = charArray;
        this.MASK = charArray.length - 1;
        this.SHIFT = Integer.numberOfTrailingZeros(charArray.length);
        int i = 0;
        while (true) {
            char[] cArr = this.DIGITS;
            if (i < cArr.length) {
                this.CHAR_MAP.put(Character.valueOf(cArr[i]), Integer.valueOf(i));
                i++;
            } else {
                return;
            }
        }
    }

    static Base32String getInstance() {
        return INSTANCE;
    }

    public static byte[] decode(String str) throws DecodingException {
        return getInstance().decodeInternal(str);
    }

    public static String encode(byte[] bArr) {
        return getInstance().encodeInternal(bArr);
    }

    public byte[] decodeInternal(String str) throws DecodingException {
        String upperCase = str.trim().replaceAll("-", "").replaceAll(" ", "").replaceFirst("[=]*$", "").toUpperCase(Locale.US);
        if (upperCase.length() == 0) {
            return new byte[0];
        }
        byte[] bArr = new byte[(upperCase.length() * this.SHIFT) / 8];
        char[] charArray = upperCase.toCharArray();
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        for (char c : charArray) {
            if (this.CHAR_MAP.containsKey(Character.valueOf(c))) {
                i = (i << this.SHIFT) | (this.CHAR_MAP.get(Character.valueOf(c)).intValue() & this.MASK);
                i2 += this.SHIFT;
                if (i2 >= 8) {
                    bArr[i3] = (byte) (i >> (i2 - 8));
                    i2 -= 8;
                    i3++;
                }
            } else {
                throw new DecodingException("Illegal character: " + c);
            }
        }
        return bArr;
    }

    public String encodeInternal(byte[] bArr) {
        if (bArr.length == 0) {
            return "";
        }
        if (bArr.length < 268435456) {
            int i = 8;
            int i2 = this.SHIFT;
            int i3 = 1;
            StringBuilder sb = new StringBuilder((((bArr.length * 8) + i2) - 1) / i2);
            int i4 = bArr[0];
            while (true) {
                if (i <= 0 && i3 >= bArr.length) {
                    return sb.toString();
                }
                int i5 = this.SHIFT;
                if (i < i5) {
                    if (i3 < bArr.length) {
                        i4 = (i4 << 8) | (bArr[i3] & 255);
                        i += 8;
                        i3++;
                    } else {
                        int i6 = i5 - i;
                        i4 <<= i6;
                        i += i6;
                    }
                }
                int i7 = this.MASK;
                i -= this.SHIFT;
                sb.append(this.DIGITS[i7 & (i4 >> i)]);
                i4 = i4;
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
