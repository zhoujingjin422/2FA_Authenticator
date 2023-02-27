package com.demo.example.authenticator.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import com.demo.example.authenticator.tokens.Token;
import nl.qbusict.cupboard.CupboardFactory;


public class TokenProvider extends ContentProvider {
    public static final String BASE_TOKEN = "token";
    private static final UriMatcher MATCHER;
    public static final String PROVIDER_AUTHORITY = "authentic.app.authenticator.provider";
    public static final Uri TOKEN_URI = Uri.parse("content://authentic.app.authenticator.provider/token");
    private CupboardSQLiteOpenHelper mHelper;

    @Override 
    public String getType(Uri uri) {
        return null;
    }

    static {
        UriMatcher uriMatcher = new UriMatcher(-1);
        MATCHER = uriMatcher;
        uriMatcher.addURI(PROVIDER_AUTHORITY, BASE_TOKEN, 1);
        MATCHER.addURI(PROVIDER_AUTHORITY, "token/#", 0);
    }

    @Override 
    public boolean onCreate() {
        this.mHelper = new CupboardSQLiteOpenHelper(getContext());
        return true;
    }

    private ContentResolver getContentResolver() {
        return getContext().getContentResolver();
    }

    @Override 
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        Cursor cursor = CupboardFactory.cupboard().withDatabase(this.mHelper.getReadableDatabase()).query(getTableClass(uri)).withProjection(strArr).withSelection(str, strArr2).orderBy(str2).getCursor();
        cursor.setNotificationUri(getContentResolver(), uri);
        return cursor;
    }

    @Override 
    public Uri insert(Uri uri, ContentValues contentValues) {
        long insertWithOnConflict = this.mHelper.getWritableDatabase().insertWithOnConflict(CupboardFactory.cupboard().withEntity(getTableClass(uri)).getTable(), null, contentValues, 5);
        getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, insertWithOnConflict);
    }

    @Override 
    public int delete(Uri uri, String str, String[] strArr) {
        long j;
        String str2;
        SQLiteDatabase writableDatabase = this.mHelper.getWritableDatabase();
        int match = MATCHER.match(uri);
        if (match == 0) {
            j = Long.parseLong(uri.getLastPathSegment());
        } else if (match == 1) {
            j = -1;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        if (j != -1) {
            StringBuilder sb = new StringBuilder();
            if (TextUtils.isEmpty(str)) {
                str2 = "";
            } else {
                str2 = str + " AND ";
            }
            sb.append(str2);
            sb.append("_id");
            sb.append("=");
            sb.append(j);
            str = sb.toString();
        }
        int delete = CupboardFactory.cupboard().withDatabase(writableDatabase).delete(Token.class, str, strArr);
        getContentResolver().notifyChange(uri, null);
        return delete;
    }

    @Override 
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        long j;
        String str2;
        SQLiteDatabase writableDatabase = this.mHelper.getWritableDatabase();
        int match = MATCHER.match(uri);
        if (match == 0) {
            j = Long.parseLong(uri.getLastPathSegment());
        } else if (match == 1) {
            j = -1;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        if (j != -1) {
            StringBuilder sb = new StringBuilder();
            if (TextUtils.isEmpty(str)) {
                str2 = "";
            } else {
                str2 = str + " AND ";
            }
            sb.append(str2);
            sb.append("_id");
            sb.append("=");
            sb.append(j);
            str = sb.toString();
        }
        int update = CupboardFactory.cupboard().withDatabase(writableDatabase).update(Token.class, contentValues, str, strArr);
        getContentResolver().notifyChange(uri, null);
        return update;
    }

    @Override 
    public int bulkInsert(Uri uri, ContentValues[] contentValuesArr) {
        SQLiteDatabase writableDatabase = this.mHelper.getWritableDatabase();
        String tableString = getTableString(uri);
        writableDatabase.beginTransaction();
        try {
            int i = 0;
            for (ContentValues contentValues : contentValuesArr) {
                try {
                    writableDatabase.insertWithOnConflict(tableString, null, contentValues, 5);
                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                    writableDatabase.endTransaction();
                    return -1;
                }
            }
            writableDatabase.setTransactionSuccessful();
            writableDatabase.endTransaction();
            if (i <= 0) {
                return i;
            }
            getContentResolver().notifyChange(uri, null);
            return i;
        } catch (Throwable unused) {
            writableDatabase.endTransaction();
            return -1;
        }
    }

    private Class<?> getTableClass(Uri uri) {
        int match = MATCHER.match(uri);
        if (match == 0 || match == 1) {
            return Token.class;
        }
        throw new IllegalArgumentException("Unknown URI: " + uri);
    }

    private String getTableString(Uri uri) {
        int match = MATCHER.match(uri);
        if (match == 0 || match == 1) {
            return CupboardFactory.cupboard().getTable(Token.class);
        }
        throw new IllegalArgumentException("Unknown URI: " + uri);
    }
}
