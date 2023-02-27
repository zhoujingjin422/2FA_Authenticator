package com.demo.example.authenticator.ui;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


public class DragDropItemTouchCallback extends ItemTouchHelper.Callback {
    private int mDragFrom = -1;
    private int mDragTo = -1;
    private final ReorderListener mListener;

    
    public interface ReorderListener {
        void onItemDismiss(int i);

        void onItemMove(int i, int i2);

        void onItemMoveComplete(int i, int i2);

        void onItemMoveStarted(int i);
    }

    @Override 
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override 
    public boolean isLongPressDragEnabled() {
        return true;
    }

    public DragDropItemTouchCallback(ReorderListener reorderListener) {
        this.mListener = reorderListener;
    }

    @Override 
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return ItemTouchHelper.Callback.makeMovementFlags(3, 0);
    }

    @Override 
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
        int adapterPosition = viewHolder.getAdapterPosition();
        int adapterPosition2 = viewHolder2.getAdapterPosition();
        if (this.mDragFrom == -1) {
            this.mDragFrom = adapterPosition;
            this.mListener.onItemMoveStarted(adapterPosition);
        }
        this.mDragTo = adapterPosition2;
        this.mListener.onItemMove(adapterPosition, adapterPosition2);
        return true;
    }

    @Override 
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        this.mListener.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override 
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int i;
        super.clearView(recyclerView, viewHolder);
        int i2 = this.mDragFrom;
        if (!(i2 == -1 || (i = this.mDragTo) == -1 || i2 == i)) {
            this.mListener.onItemMoveComplete(i2, i);
        }
        this.mDragTo = -1;
        this.mDragFrom = -1;
    }
}
