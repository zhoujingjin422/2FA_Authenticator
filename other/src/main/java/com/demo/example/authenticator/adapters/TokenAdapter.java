package com.demo.example.authenticator.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.appcompat.app.AlertDialog;

import com.demo.example.authenticator.inter.TokenActionListener;
import com.demo.example.authenticator.inter.TokenModifyListener;
import com.demo.example.authenticator.tokens.Token;
import com.demo.example.authenticator.tokens.TokenType;
import com.demo.example.authenticator.util.ClipboardUtil;

import com.demo.example.R;
import com.squareup.picasso.Picasso;
import java.util.List;
import java.util.TreeSet;
import nl.qbusict.cupboard.CupboardFactory;



public class TokenAdapter extends RecyclerReorderCursorAdapter<TokenViewHolder> implements TokenActionListener {
    private final Context mContext;
    private final TreeSet<Long> mHasNoAppIconCache = new TreeSet<>();
    private final LayoutInflater mLayoutInflater;
    private final Picasso mPicasso;
    private final TokenModifyListener mTokenListener;

    public TokenAdapter(Context context, TokenModifyListener tokenModifyListener, Cursor cursor) {
        super(context, cursor);
        setResetMapOnCursorChange(false);
        this.mContext = context;
        this.mTokenListener = tokenModifyListener;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.mPicasso = Picasso.with(context);
    }

    public TokenViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new TokenViewHolder(this.mLayoutInflater.inflate(R.layout.li_token, viewGroup, false));
    }

    public void onBindViewHolder(TokenViewHolder tokenViewHolder, Cursor cursor) {
        tokenViewHolder.bind(getItem(cursor), this, this.mPicasso, this.mHasNoAppIconCache);
    }

    public Token getItem(Cursor cursor) {
        return (Token) CupboardFactory.cupboard().withCursor(cursor).get(Token.class);
    }

    public void onNewHOTPRequested(Token token) {
        token.increment();
        this.mTokenListener.onTokenSaveRequested(token);
    }

    public void setHiddenState(Token token, int i, boolean z) {
        token.setHidden(z);
        this.mTokenListener.onTokenSaveRequested(token);
        notifyItemChanged(i);
    }

    @Override 
    public void copyToClipboard(Token token, int i) {
        String code = token.getCode();
        if (token.getType() == TokenType.TOTP && token.getTimeRemaining() < 4000) {
            code = token.getNextCode();
        }
        ClipboardUtil.copy(this.mContext, token.getLabel(), code);
    }

    @Override 
    public void confirmDelete(Context context, final Token token, final int i) {
        String str;
        if (TextUtils.isEmpty(token.getIssuer())) {
            str = token.getLabel();
        } else {
            str = token.getIssuer();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog.Builder title = builder.setTitle("Delete Token.");
        title.setMessage("" + context.getString(R.string.dialog_text_delete_token, str)).setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialogInterface, int i2) {
                TokenAdapter.this.mTokenListener.onTokenDeleteRequested(token);
                TokenAdapter.this.notifyItemRemoved(i);
                dialogInterface.dismiss();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialogInterface, int i2) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    public void share(Context context, Token token, int i) {
        TokenModifyListener tokenModifyListener = this.mTokenListener;
        if (tokenModifyListener != null) {
            tokenModifyListener.onTokenShareRequested(token);
        }
    }

    public int getCount() {
        if (getCursor() == null) {
            return 0;
        }
        return getCursor().getCount();
    }

    public Token getItem(int i) {
        Cursor cursor = getCursor();
        if (cursor == null) {
            return null;
        }
        cursor.moveToPosition(i);
        return (Token) CupboardFactory.cupboard().withCursor(cursor).get(Token.class);
    }

    public int saveNewSortOrder() {
        int i;
        SparseIntArray positionMap = getPositionMap();
        Cursor cursor = getCursor();
        int i2 = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            List<Token> list = CupboardFactory.cupboard().withCursor(cursor).list(Token.class);
            for (Token token : list) {
                int indexOfValue = positionMap.indexOfValue(i2);
                if (indexOfValue == -1) {
                    i = i2;
                } else {
                    i = positionMap.keyAt(indexOfValue);
                }
                token.setSortOrder(i);
                i2++;
            }
            for (Token token2 : list) {
                this.mTokenListener.onTokenSaveRequested(token2);
            }
            resetMap();
        }
        return i2;
    }
}
