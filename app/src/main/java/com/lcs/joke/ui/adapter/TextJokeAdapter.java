package com.lcs.joke.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lcs.joke.R;
import com.lcs.joke.databinding.ItemTextJokeBinding;
import com.lcs.joke.net.bean.Joke;

import java.util.ArrayList;

public class TextJokeAdapter extends BaseAdapter<Joke> {
    public TextJokeAdapter(Context context) {
        super(context, R.layout.item_text_joke);
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ViewHolderFooter || holder instanceof ViewHolderEmpty) {
            return;
        }

        ViewHolder vh = (ViewHolder) holder;
        Joke joke = getItem(position);
        joke.content = joke.content.replaceAll("　　", "");
        joke.content = joke.content.replaceAll("  ", "");
        vh.tvContent.setText(joke.content);
        vh.tvTime.setText(String.format("更新时间：%s", joke.updatetime));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvContent, tvTime;

        public ViewHolder(View view) {
            super(view);
            tvContent = (TextView) view.findViewById(R.id.tv_content);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
        }
    }
}
