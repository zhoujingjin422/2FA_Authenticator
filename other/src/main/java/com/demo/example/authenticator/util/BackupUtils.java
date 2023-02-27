package com.demo.example.authenticator.util;

import android.os.Environment;

import com.demo.example.authenticator.tokens.MalformedTokenException;
import com.demo.example.authenticator.tokens.Token;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public final class BackupUtils {
    public static final String BACKUP_DIR = "AuthenticateBackups";
    public static final String CHARSET_UTF8 = "UTF-8";
    private static final SimpleDateFormat FILENAME_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);

    
    public static class Pair<F, S> {
        public F first;
        public S second;

        public Pair(F f, S s) {
            this.first = f;
            this.second = s;
        }
    }

    private BackupUtils() {
    }

    public static String getBackupContent(String str, List<Token> list) throws BackupException {
        StringBuilder sb = new StringBuilder();
        for (Token token : list) {
            sb.append(token.toUri(false).toString());
            sb.append('\n');
        }
        try {
            return "Authenticator encrypted tokens\n-----BEGIN TOKEN BLOCK-----\n" + CryptoUtil.encrypt(str, sb.toString()) + "\n" + "-----END TOKEN BLOCK-----";
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidParameterSpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            e.printStackTrace();
            throw new BackupException("Encryption failed", e);
        }
    }

    public static Pair<File, String> backupTokens(String str, List<Token> list) throws BackupException {
        try {
            String backupContent = getBackupContent(str, list);
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), BACKUP_DIR);
            file.mkdirs();
            File file2 = new File(file, FILENAME_FORMAT.format(Calendar.getInstance(Locale.getDefault()).getTime()));
            PrintWriter printWriter = new PrintWriter(file2, "UTF-8");
            printWriter.write(backupContent);
            printWriter.close();
            return new Pair<>(file2, backupContent);
        } catch (BackupException | FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new BackupException("Encryption failed", e);
        }
    }

    public static boolean hasBackups() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), BACKUP_DIR);
        return file.listFiles() != null && file.listFiles().length > 0;
    }

    public static File[] listBackups() throws IOException {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), BACKUP_DIR).listFiles();
    }

    public static Pair<ArrayList<Token>, ArrayList<Token>> importTokensFromContent(String str, String str2, List<Token> list) throws BackupException {
        try {
            StringBuilder sb = new StringBuilder();
            int indexOf = str.indexOf("-----BEGIN TOKEN BLOCK-----");
            int indexOf2 = str.indexOf("-----END TOKEN BLOCK-----", indexOf);
            if (indexOf == -1 || indexOf2 == -1) {
                throw new BackupException("Import contains no encrypted tokens", null);
            }
            sb.append((CharSequence) str, indexOf + 28, indexOf2 - 1);
            return decryptTokens(str2, list, sb);
        } catch (MalformedTokenException | IOException | IllegalArgumentException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            throw new BackupException("Encryption failed", e);
        }
    }

    public static Pair<ArrayList<Token>, ArrayList<Token>> importTokensFromFile(String str, String str2, List<Token> list) throws BackupException {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), BACKUP_DIR), str)), Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            boolean z = false;

            Boolean con = true;
            while (con) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    con = false;
                    break;
                } else if ("-----BEGIN TOKEN BLOCK-----".equals(readLine)) {
                    z = true;
                } else if ("-----END TOKEN BLOCK-----".equals(readLine)) {
                    con = false;
                    break;
                } else if (z) {
                    sb.append(readLine);
                }
            }
            bufferedReader.close();
            return decryptTokens(str2, list, sb);
        } catch (MalformedTokenException | IOException | IllegalArgumentException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            e.printStackTrace();
            throw new BackupException("Encryption failed", e);
        }
    }

    private static Pair<ArrayList<Token>, ArrayList<Token>> decryptTokens(String str, List<Token> list, StringBuilder sb) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException, IOException, MalformedTokenException, BackupException, InvalidKeySpecException {
        String str2;
        if (sb.length() != 0) {
            if (str == null) {
                str2 = sb.toString().replace("0otpauth://", "0\notpauth://");
            } else {
                str2 = CryptoUtil.decrypt(str, sb.toString());
            }
            BufferedReader bufferedReader = new BufferedReader(new StringReader(str2));
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();

            Boolean con = true;
            while (con) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    con = false;
                    return new Pair<>(arrayList2, arrayList);
                }
                Token token = new Token(readLine);
                if (list.contains(token)) {
                    arrayList.add(token);
                } else {
                    list.add(token);
                    arrayList2.add(token);
                }
            }
        } else {
            throw new BackupException("Import contains no encrypted tokens", null);
        }
        return null;
    }

    public static boolean deleteBackup(String str) {
        return new File(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), BACKUP_DIR), str).delete();
    }
}
