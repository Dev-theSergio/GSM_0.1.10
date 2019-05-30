package com.kbrs.app.GsmRemoteControl;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

class DiffUtilCallback extends DiffUtil.Callback {

    List<Channels> oldItem;
    List<Channels> newItem;

    public DiffUtilCallback(List<Channels> newItem, List<Channels> oldItem){
        this.newItem = newItem;
        this.oldItem = oldItem;
    }


    @Override
    public int getOldListSize() {
        return oldItem.size();
    }

    @Override
    public int getNewListSize() {
        return newItem.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItem.get(oldItemPosition).getId() == newItem.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItem.get(oldItemPosition).getChName() == (newItem.get(newItemPosition).getChName())
                && oldItem.get(oldItemPosition).getChDesign().equals(newItem.get(newItemPosition).getChDesign())
                && oldItem.get(oldItemPosition).isChSwitch() == newItem.get(newItemPosition).isChSwitch();
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
