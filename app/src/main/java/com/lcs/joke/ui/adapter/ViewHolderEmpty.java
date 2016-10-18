package com.lcs.joke.ui.adapter;

import android.databinding.ObservableField;
import android.support.v7.widget.DecorContentParent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lcs.joke.databinding.ItemEmptyBinding;
import com.lcs.joke.utils.DebugLog;

/**
 * Created by LiuCongshan on 2016/6/5.
 * ViewHolderEmpty
 */
public class ViewHolderEmpty extends RecyclerView.ViewHolder {
    private ItemEmptyBinding mBinding;

    public ViewHolderEmpty(View view) {
        super(view);
        mBinding = ItemEmptyBinding.bind(view);
    }

    public void bind(ObservableField<String> tip, boolean isLoaded) {
        mBinding.setTip(tip);
        mBinding.setIsLoaded(isLoaded);
    }
}
