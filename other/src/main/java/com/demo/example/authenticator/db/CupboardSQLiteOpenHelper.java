package com.demo.example.authenticator.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.demo.example.authenticator.tokens.Token;
import nl.qbusict.cupboard.CupboardBuilder;
import nl.qbusict.cupboard.CupboardFactory;


public class CupboardSQLiteOpenHelper extends SQLiteOpenHelper {
    static {
        CupboardFactory.setCupboard(new CupboardBuilder().useAnnotations().build());
        CupboardFactory.cupboard().register(Token.class);
    }

    public CupboardSQLiteOpenHelper(Context context) {
        super(context, "tokens.db", (SQLiteDatabase.CursorFactory) null, 5);
    }

    @Override 
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        CupboardFactory.cupboard().withDatabase(sQLiteDatabase).createTables();
    }

    @Override 
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        CupboardFactory.cupboard().withDatabase(sQLiteDatabase).upgradeTables();
    }
}
