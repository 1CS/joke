package com.lcs.joke.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lcs.joke.R;
import com.lcs.joke.net.bean.Joke;

public class PicJokeAdapter extends BaseAdapter<Joke> {
    public PicJokeAdapter(Context context) {
        super(context, R.layout.item_pic_joke);
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
        vh.tvContent.setText(joke.content);
        vh.tvTime.setText(String.format("更新时间：%s", joke.updatetime));

        Uri uri = Uri.parse(joke.url);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
            .setUri(uri)
            .setAutoPlayAnimations(true)
            .build();
        vh.ivContent.setController(controller);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvContent, tvTime;
        private SimpleDraweeView ivContent;

        public ViewHolder(View view) {
            super(view);
            tvContent = (TextView) view.findViewById(R.id.tv_content);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            ivContent = (SimpleDraweeView) view.findViewById(R.id.iv_content);
        }
    }
}
