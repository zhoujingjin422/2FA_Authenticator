package com.demo.example.authenticator.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;
import android.util.Base64;
import androidx.multidex.BuildConfig;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


public final class CryptoUtil {
    public static final String CHARSET_UTF8 = "UTF-8";
    private static byte[] SALT = "ROYALEWITHCHEESEROYALEWITHCHEESE".getBytes();
    private static final Charset UTF8 = Charset.forName("UTF-8");

    private CryptoUtil() {
    }

    public static String getAppSignatureKey(Context context) {
        String str = null;
        try {
            Signature[] signatureArr = context.getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES).signatures;
            if (signatureArr.length > 1) {
                return null;
            }
            for (Signature signature : signatureArr) {
                try {
                    str = new String(((X509Certificate) CertificateFactory.getInstance("X509").generateCertificate(new ByteArrayInputStream(signature.toByteArray()))).getSignature(), UTF8);
                } catch (CertificateException e) {
                    e.printStackTrace();
                }
            }
            return str;
        } catch (PackageManager.NameNotFoundException e2) {
            e2.printStackTrace();
            return str;
        }
    }

    public static String getRandomPassphrase(Context context) {
        int nextInt;
        try {
            SecureRandom secureRandom = new SecureRandom();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open("dictionary.txt")));
            int[] iArr = new int[4];




            for (int i = 0; i < 4; i++) {
                boolean z = false;
                do {
                    nextInt = secureRandom.nextInt(6565);
                    int i2 = 0;
                    while (i2 < i) {
                        if (iArr[i2] == nextInt) {
                            z = true;
                        } else {
                            i2++;
                        }
                    }
                } while (z);
                iArr[i] = nextInt;
            }
            String[] randomWords = getRandomWords(bufferedReader, iArr);
            bufferedReader.close();
            return TextUtils.join(" ", randomWords);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String[] getRandomWords(BufferedReader bufferedReader, int[] iArr) throws IOException {
        String[] strArr = new String[iArr.length];
        int i = 0;
        int i2 = 0;
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null || i >= iArr.length) {
                break;
            }
            int i3 = 0;
            while (true) {
                if (i3 < iArr.length) {
                    if (iArr[i3] == i2) {
                        strArr[i3] = readLine;
                        i++;
                        break;
                    }
                    i3++;
                }
            }
            i2++;
        }
        return strArr;
    }

    public static String encrypt(String str, String str2) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        byte[] bArr = new byte[16];
        new SecureRandom().nextBytes(bArr);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr);
        Key createKeyFromPassphrase = createKeyFromPassphrase(str);
        Cipher instance = Cipher.getInstance(createKeyFromPassphrase.getAlgorithm());
        instance.init(1, createKeyFromPassphrase, ivParameterSpec);
        byte[] doFinal = instance.doFinal(str2.getBytes(UTF8));
        byte[] bArr2 = new byte[doFinal.length + 16];
        System.arraycopy(bArr, 0, bArr2, 0, 16);
        System.arraycopy(doFinal, 0, bArr2, 16, doFinal.length);
        return new String(Base64.encode(bArr2, 2), UTF8);
    }

    public static String decrypt(String str, String str2) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        byte[] decode = Base64.decode(str2.getBytes(UTF8), 2);
        byte[] bArr = new byte[16];
        System.arraycopy(decode, 0, bArr, 0, 16);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(bArr);
        Key createKeyFromPassphrase = createKeyFromPassphrase(str);
        Cipher instance = Cipher.getInstance(createKeyFromPassphrase.getAlgorithm());
        instance.init(2, createKeyFromPassphrase, ivParameterSpec);
        return new String(instance.doFinal(decode, 16, decode.length - 16), UTF8);
    }

    protected static Key createKeyFromPassphrase(String str) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new SecretKeySpec(SecretKeyFactory.getInstance("PBEwithSHA256and256BITAES-CBC-BC").generateSecret(new PBEKeySpec(str.toCharArray(), SALT, 65536, 32)).getEncoded(), "AES");
    }
}
