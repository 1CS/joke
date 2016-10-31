package com.lcs.joke.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lcs.joke.R;
import com.lcs.joke.databinding.ItemTextJokeBinding;
import com.lcs.joke.net.bean.Joke;
import com.lcs.joke.utils.rxbus.RxBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class JokeTextAdapter extends BaseAdapter<Joke> {
    private SimpleDateFormat formatter;

    public JokeTextAdapter(Context context) {
        super(context, R.layout.item_text_joke);
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        final Joke joke = getItem(position);
        joke.content = joke.content.replaceAll("　　", "");
        joke.content = joke.content.replaceAll("  ", "");
        vh.binding.tvContent.setText(joke.content);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(joke.unixtime + "000"));
        vh.binding.tvTime.setText(formatter.format(calendar.getTime()));
        vh.binding.tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getDefault().post(joke);
            }
        });
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ItemTextJokeBinding binding;

        ViewHolder(View view) {
            super(view);
            binding = ItemTextJokeBinding.bind(view);
        }
    }
}
