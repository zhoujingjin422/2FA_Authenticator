package com.demo.example.authenticator.adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.util.SparseIntArray;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import java.util.Iterator;
import java.util.LinkedList;


public abstract class RecyclerReorderCursorAdapter<VH extends ViewHolder> extends RecyclerCursorAdapter<VH> {
    private SparseIntArray cursorPositionMap = new SparseIntArray();
    private boolean resetOnCursorChange = true;

    public RecyclerReorderCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override 
    public long getItemId(int i) {
        return super.getItemId(this.cursorPositionMap.get(i, i));
    }

    @Override 
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
        if (this.resetOnCursorChange) {
            resetMap();
        }
    }

    public void reorderItem(int i, int i2) {
        if (i != i2 && i >= 0 && i2 >= 0 && i < getItemCount() && i2 < getItemCount()) {
            int i3 = this.cursorPositionMap.get(i, i);
            if (i > i2) {
                for (int i4 = i; i4 > i2; i4--) {
                    SparseIntArray sparseIntArray = this.cursorPositionMap;
                    int i5 = i4 - 1;
                    sparseIntArray.put(i4, sparseIntArray.get(i5, i5));
                }
            } else {
                int i6 = i;
                while (i6 < i2) {
                    SparseIntArray sparseIntArray2 = this.cursorPositionMap;
                    int i7 = i6 + 1;
                    sparseIntArray2.put(i6, sparseIntArray2.get(i7, i7));
                    i6 = i7;
                }
            }
            this.cursorPositionMap.put(i2, i3);
            cleanMap();
            notifyItemMoved(i, i2);
        }
    }

    public boolean getResetMapOnCursorChange() {
        return this.resetOnCursorChange;
    }

    public void setResetMapOnCursorChange(boolean z) {
        this.resetOnCursorChange = z;
    }

    public void resetMap() {
        this.cursorPositionMap.clear();
    }

    public SparseIntArray getPositionMap() {
        if (Build.VERSION.SDK_INT >= 14) {
            return this.cursorPositionMap.clone();
        }
        return clone(this.cursorPositionMap);
    }

    private void cleanMap() {
        LinkedList linkedList = new LinkedList();
        for (int i = 0; i < this.cursorPositionMap.size(); i++) {
            if (this.cursorPositionMap.keyAt(i) == this.cursorPositionMap.valueAt(i)) {
                linkedList.add(Integer.valueOf(this.cursorPositionMap.keyAt(i)));
            }
        }
        Iterator it = linkedList.iterator();
        while (it.hasNext()) {
            this.cursorPositionMap.delete(((Integer) it.next()).intValue());
        }
    }

    private SparseIntArray clone(SparseIntArray sparseIntArray) {
        SparseIntArray sparseIntArray2 = new SparseIntArray();
        for (int i = 0; i < sparseIntArray.size(); i++) {
            int keyAt = sparseIntArray.keyAt(i);
            sparseIntArray2.put(keyAt, sparseIntArray.get(keyAt));
        }
        return sparseIntArray2;
    }
}
