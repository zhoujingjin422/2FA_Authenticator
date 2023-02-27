package com.demo.example.authenticator.tokens;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.demo.example.authenticator.util.Base32String;
import com.demo.example.authenticator.util.CommonUtil;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Locale;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import nl.qbusict.cupboard.annotation.Column;


public class Token implements Parcelable {
    public static final Creator<Token> CREATOR = new Creator<Token>() { 
        
        @Override 
        public Token createFromParcel(Parcel parcel) {
            return new Token(parcel);
        }

        
        @Override 
        public Token[] newArray(int i) {
            return new Token[i];
        }
    };
    public static final String FIELD_HIDDEN = "hidden";
    public static final String FIELD_ISSUER_EXT = "issuer_ext";
    public static final String FIELD_LABEL = "label";
    public static final String FIELD_SORT_ORDER = "sortOrder";
    @Column("_id")
    private Long _id;
    @Column("algorithm")
    private String mAlgorithm;
    private transient String mCacheCode;
    private transient long mCacheCounter;
    @Column("counter")
    private long mCounter;
    @Column("digits")
    private int mDigits;
    @Column(FIELD_HIDDEN)
    private boolean mHidden;
    @Column(FIELD_ISSUER_EXT)
    private String mIssuerExt;
    @Column("issuer_int")
    private String mIssuerInt;
    @Column(FIELD_LABEL)
    private String mLabel;
    private transient long mLastCode;
    @Column("period")
    private int mPeriod;
    @Column("secret")
    private byte[] mSecret;
    @Column(FIELD_SORT_ORDER)
    private int mSortOrder;
    @Column("tokentype")
    private TokenType mType;

    @Override 
    public int describeContents() {
        return 0;
    }

    
    
    public static class AnonymousClass2 {
        static final int[] $SwitchMap$com$pixplicity$authenticator$tokens$TokenType;

        AnonymousClass2() {
        }

        static {
            int[] iArr = new int[TokenType.values().length];
            $SwitchMap$com$pixplicity$authenticator$tokens$TokenType = iArr;
            iArr[TokenType.HOTP.ordinal()] = 1;
            $SwitchMap$com$pixplicity$authenticator$tokens$TokenType[TokenType.TOTP.ordinal()] = 2;
        }
    }

    public Token() {
        this.mCacheCounter = -1;
    }

    public Token(String str) throws MalformedTokenException {
        this(Uri.parse(str));
    }

    public Token(Uri uri) throws MalformedTokenException {
        String str;
        this.mCacheCounter = -1;
        if (uri.getScheme() == null || !uri.getScheme().equals("otpauth")) {
            throw new MalformedTokenException("Invalid URI scheme (must be otpauth)");
        }
        String authority = uri.getAuthority();
        if ("totp".equals(authority)) {
            this.mType = TokenType.TOTP;
        } else if ("hotp".equals(authority)) {
            this.mType = TokenType.HOTP;
        } else {
            throw new MalformedTokenException("Invalid authority " + authority + " (must be hotp or totp)");
        }
        String path = uri.getPath();
        if (path != null) {
            boolean z = false;
            int i = 0;
            while (path.length() > 0 && path.charAt(i) == '/') {
                path = path.substring(1);
                i++;
            }
            if (path.length() != 0) {
                int indexOf = path.indexOf(58);
                if (indexOf < 0) {
                    str = "";
                } else {
                    str = path.substring(0, indexOf);
                }
                this.mIssuerExt = str;
                this.mIssuerInt = uri.getQueryParameter("issuer");
                this.mLabel = path.substring(indexOf >= 0 ? indexOf + 1 : 0);
                String queryParameter = uri.getQueryParameter("algorithm");
                this.mAlgorithm = queryParameter;
                if (queryParameter == null) {
                    this.mAlgorithm = "sha1";
                }
                this.mAlgorithm = this.mAlgorithm.toUpperCase(Locale.US);
                try {
                    Mac.getInstance("Hmac" + this.mAlgorithm);
                    try {
                        String queryParameter2 = uri.getQueryParameter("digits");
                        int parseInt = Integer.parseInt(queryParameter2 == null ? "6" : queryParameter2);
                        this.mDigits = parseInt;
                        if (!(parseInt == 6 || parseInt == 8)) {
                            throw new MalformedTokenException("Invalid value for parameter 'digits' (must be 6 or 8)");
                        }
                        int i2 = AnonymousClass2.$SwitchMap$com$pixplicity$authenticator$tokens$TokenType[this.mType.ordinal()];
                        if (i2 == 1) {
                            try {
                                String queryParameter3 = uri.getQueryParameter("counter");
                                this.mCounter = Long.parseLong(queryParameter3 == null ? "0" : queryParameter3);
                            } catch (NumberFormatException e) {
                                throw new MalformedTokenException("Unable to parse valid number for parameter 'counter'", e);
                            }
                        } else if (i2 == 2) {
                            try {
                                String queryParameter4 = uri.getQueryParameter("period");
                                this.mPeriod = Integer.parseInt(queryParameter4 == null ? "30" : queryParameter4);
                            } catch (NumberFormatException e2) {
                                throw new MalformedTokenException("Unable to parse valid number for parameter 'period'", e2);
                            }
                        }
                        try {
                            this.mSecret = Base32String.decode(uri.getQueryParameter("secret"));
                            String queryParameter5 = uri.getQueryParameter(FIELD_HIDDEN);
                            if (queryParameter5 != null && Boolean.parseBoolean(queryParameter5)) {
                                z = true;
                            }
                            this.mHidden = z;
                        } catch (Base32String.DecodingException e3) {
                            throw new MalformedTokenException("Unable to decode value for parameter 'secret'", e3);
                        }
                    } catch (NumberFormatException e4) {
                        throw new MalformedTokenException("Unable to parse valid number for parameter 'digits'", e4);
                    }
                } catch (NoSuchAlgorithmException e5) {
                    throw new MalformedTokenException("No such algorithm: 'Hmac" + this.mAlgorithm + "'", e5);
                }
            } else {
                throw new MalformedTokenException("0-length path");
            }
        } else {
            throw new MalformedTokenException("Path is null");
        }
    }

    private String getHOTP(long j) {
        String str;
        if (this.mCacheCounter == j && (str = this.mCacheCode) != null) {
            return str;
        }
        ByteBuffer allocate = ByteBuffer.allocate(8);
        allocate.putLong(j);
        int i = 1;
        for (int i2 = this.mDigits; i2 > 0; i2--) {
            i *= 10;
        }
        try {
            String concat = "Hmac".concat(this.mAlgorithm);
            Mac instance = Mac.getInstance(concat);
            instance.init(new SecretKeySpec(this.mSecret, concat));
            byte[] doFinal = instance.doFinal(allocate.array());
            byte b = (byte) (doFinal[doFinal.length - 1] & 15);
            String num = Integer.toString(((((doFinal[b + 2] & 255) << 8) | (((doFinal[b] & Byte.MAX_VALUE) << 24) | ((doFinal[b + 1] & 255) << 16))) | (doFinal[b + 3] & 255)) % i);
            while (num.length() != this.mDigits) {
                num = "0" + num;
            }
            this.mCacheCounter = j;
            this.mCacheCode = num;
            return num;
        } catch (ArrayIndexOutOfBoundsException | InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void increment() {
        if (this.mType == TokenType.HOTP) {
            this.mCounter++;
            this.mLastCode = System.currentTimeMillis();
        }
    }

    public long getDatabaseId() {
        return this._id.longValue();
    }

    public void setDatabaseId(long j) {
        this._id = Long.valueOf(j);
    }

    public long getId() {
        return this._id.longValue();
    }

    public String getIdentifier() {
        String str = this.mIssuerInt;
        if (str == null || str.equals("")) {
            String str2 = this.mIssuerExt;
            if (str2 == null || str2.equals("")) {
                return this.mLabel;
            }
            return this.mIssuerExt + ":" + this.mLabel;
        }
        return this.mIssuerInt + ":" + this.mLabel;
    }

    public String getSecret() {
        return Base32String.encode(this.mSecret);
    }

    public String getIssuer() {
        String str = this.mIssuerExt;
        return str != null ? str : "";
    }

    public String getLabel() {
        String str = this.mLabel;
        return str != null ? str : "";
    }

    public String getNextCode() {
        return getCodeForTime(System.currentTimeMillis() + ((long) (this.mPeriod * 1000)));
    }

    public String getCode() {
        return getCodeForTime(System.currentTimeMillis());
    }

    private String getCodeForTime(long j) {
        return getHOTP(this.mType == TokenType.TOTP ? (j / 1000) / ((long) this.mPeriod) : this.mCounter);
    }

    public TokenType getType() {
        return this.mType;
    }

    public String getAlgorithm() {
        return this.mAlgorithm;
    }

    public int getIntervalSec() {
        return this.mPeriod;
    }

    public int getDigits() {
        return this.mDigits;
    }

    public int getProgress() {
        long currentTimeMillis = System.currentTimeMillis();
        long j = 1000;
        if (this.mType == TokenType.TOTP) {
            int i = this.mPeriod;
            j = (currentTimeMillis % ((long) (i * 1000))) / ((long) i);
        } else {
            long j2 = (currentTimeMillis - this.mLastCode) / 60;
            if (j2 <= 1000) {
                j = j2;
            }
        }
        return 1000 - ((int) j);
    }

    public int getTimeRemaining() {
        if (this.mType != TokenType.TOTP) {
            return 0;
        }
        long currentTimeMillis = System.currentTimeMillis();
        int i = this.mPeriod * 1000;
        return i - ((int) (currentTimeMillis % ((long) i)));
    }

    public boolean isHidden() {
        return this.mHidden;
    }

    public void setHidden(boolean z) {
        this.mHidden = z;
    }

    public Uri toUri(boolean z) {
        String str;
        if (!this.mIssuerExt.equals("")) {
            str = this.mIssuerExt + ":" + this.mLabel;
        } else {
            str = this.mLabel;
        }
        Uri.Builder appendQueryParameter = new Uri.Builder().scheme("otpauth").path(str).appendQueryParameter("secret", Base32String.encode(this.mSecret));
        String str2 = this.mIssuerInt;
        if (str2 == null) {
            str2 = this.mIssuerExt;
        }
        Uri.Builder appendQueryParameter2 = appendQueryParameter.appendQueryParameter("issuer", str2).appendQueryParameter("algorithm", this.mAlgorithm).appendQueryParameter("digits", Integer.toString(this.mDigits));
        if (z) {
            appendQueryParameter2.appendQueryParameter(FIELD_HIDDEN, this.mHidden ? "true" : "false");
        }
        int i = AnonymousClass2.$SwitchMap$com$pixplicity$authenticator$tokens$TokenType[this.mType.ordinal()];
        if (i == 1) {
            appendQueryParameter2.authority("hotp");
            appendQueryParameter2.appendQueryParameter("counter", Long.toString(this.mCounter + 1));
        } else if (i == 2) {
            appendQueryParameter2.authority("totp");
            appendQueryParameter2.appendQueryParameter("period", Integer.toString(this.mPeriod));
        }
        return appendQueryParameter2.build();
    }

    @Override 
    public String toString() {
        return toUri(false).toString();
    }

    public long getCount() {
        return this.mCounter;
    }

    public int getSortOrder() {
        return this.mSortOrder;
    }

    public void setSortOrder(int i) {
        this.mSortOrder = i;
    }

    @Override 
    public boolean equals(Object obj) {
        if (!(obj instanceof Token)) {
            return super.equals(obj);
        }
        Token token = (Token) obj;
        return token.mAlgorithm.equals(this.mAlgorithm) && token.mDigits == this.mDigits && CommonUtil.equals(token.mIssuerExt, this.mIssuerExt) && CommonUtil.equals(token.mIssuerInt, this.mIssuerInt) && CommonUtil.equals(token.mLabel, this.mLabel) && token.mPeriod == this.mPeriod && token.mType == this.mType && Arrays.equals(token.mSecret, this.mSecret);
    }

    @Override 
    public void writeToParcel(Parcel parcel, int i) {
        int i2;
        parcel.writeValue(this._id);
        parcel.writeString(this.mIssuerInt);
        parcel.writeString(this.mIssuerExt);
        parcel.writeString(this.mLabel);
        TokenType tokenType = this.mType;
        if (tokenType == null) {
            i2 = -1;
        } else {
            i2 = tokenType.ordinal();
        }
        parcel.writeInt(i2);
        parcel.writeString(this.mAlgorithm);
        parcel.writeByteArray(this.mSecret);
        parcel.writeInt(this.mDigits);
        parcel.writeLong(this.mCounter);
        parcel.writeInt(this.mPeriod);
        parcel.writeByte(this.mHidden ? (byte) 1 : 0);
        parcel.writeInt(this.mSortOrder);
    }

    protected Token(Parcel parcel) {
        TokenType tokenType;
        this.mCacheCounter = -1;
        this._id = (Long) parcel.readValue(Long.class.getClassLoader());
        this.mIssuerInt = parcel.readString();
        this.mIssuerExt = parcel.readString();
        this.mLabel = parcel.readString();
        int readInt = parcel.readInt();
        if (readInt == -1) {
            tokenType = null;
        } else {
            tokenType = TokenType.values()[readInt];
        }
        this.mType = tokenType;
        this.mAlgorithm = parcel.readString();
        this.mSecret = parcel.createByteArray();
        this.mDigits = parcel.readInt();
        this.mCounter = parcel.readLong();
        this.mPeriod = parcel.readInt();
        this.mHidden = parcel.readByte() != 0;
        this.mSortOrder = parcel.readInt();
    }
}
