package com.demo.example.authenticator.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;


public abstract class RecyclerCursorAdapter<VH extends ViewHolder> extends RecyclerView.Adapter<VH> {
    private final Context mContext;
    private Cursor mCursor;
    private final NotifyingDataSetObserver mDataSetObserver;
    public boolean mDataValid;
    private int mRowIdColumn;

    public abstract void onBindViewHolder(VH vh, Cursor cursor);

    
    
    public class NotifyingDataSetObserver extends DataSetObserver {
        private NotifyingDataSetObserver() {
        }

        @Override 
        public void onChanged() {
            super.onChanged();
            RecyclerCursorAdapter.this.mDataValid = true;
            RecyclerCursorAdapter.this.notifyDataSetChanged();
        }

        @Override 
        public void onInvalidated() {
            super.onInvalidated();
            RecyclerCursorAdapter.this.mDataValid = false;
            RecyclerCursorAdapter.this.notifyDataSetChanged();
        }
    }

    public RecyclerCursorAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
        boolean z = cursor != null;
        this.mDataValid = z;
        this.mRowIdColumn = z ? this.mCursor.getColumnIndex("_id") : -1;
        NotifyingDataSetObserver notifyingDataSetObserver = new NotifyingDataSetObserver();
        this.mDataSetObserver = notifyingDataSetObserver;
        Cursor cursor2 = this.mCursor;
        if (cursor2 != null) {
            cursor2.registerDataSetObserver(notifyingDataSetObserver);
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    public Cursor getCursor() {
        return this.mCursor;
    }

    @Override 
    public int getItemCount() {
        Cursor cursor;
        if (!this.mDataValid || (cursor = this.mCursor) == null) {
            return 0;
        }
        return cursor.getCount();
    }

    @Override 
    public long getItemId(int i) {
        Cursor cursor;
        if (!this.mDataValid || (cursor = this.mCursor) == null || !cursor.moveToPosition(i)) {
            return 0;
        }
        return this.mCursor.getLong(this.mRowIdColumn);
    }

    @Override 
    public void setHasStableIds(boolean z) {
        super.setHasStableIds(true);
    }

    @Override 
    public void onBindViewHolder(VH vh, int i) {
        if (!this.mDataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        } else if (this.mCursor.moveToPosition(i)) {
            onBindViewHolder( vh, this.mCursor);
        } else {
            throw new IllegalStateException("couldn't move cursor to position " + i);
        }
    }

    public void changeCursor(Cursor cursor) {
        Cursor swapCursor = swapCursor(cursor);
        if (swapCursor != null) {
            swapCursor.close();
        }
    }

    public Cursor swapCursor(Cursor cursor) {
        NotifyingDataSetObserver notifyingDataSetObserver;
        Cursor cursor2 = this.mCursor;
        if (cursor == cursor2) {
            return null;
        }
        if (!(cursor2 == null || (notifyingDataSetObserver = this.mDataSetObserver) == null)) {
            cursor2.unregisterDataSetObserver(notifyingDataSetObserver);
        }
        this.mCursor = cursor;
        if (cursor != null) {
            NotifyingDataSetObserver notifyingDataSetObserver2 = this.mDataSetObserver;
            if (notifyingDataSetObserver2 != null) {
                cursor.registerDataSetObserver(notifyingDataSetObserver2);
            }
            this.mRowIdColumn = cursor.getColumnIndexOrThrow("_id");
            this.mDataValid = true;
            notifyDataSetChanged();
        } else {
            this.mRowIdColumn = -1;
            this.mDataValid = false;
            notifyDataSetChanged();
        }
        return cursor2;
    }
}
