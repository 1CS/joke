package com.lcs.joke.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.lcs.joke.R;
import com.lcs.joke.databinding.ActivityMainBinding;
import com.lcs.joke.net.Api;
import com.lcs.joke.net.NetCallback;
import com.lcs.joke.net.bean.Joke;
import com.lcs.joke.net.bean.response.BaseResponse;
import com.lcs.joke.net.bean.response.JokeResponse;
import com.lcs.joke.net.retrofit.AppServer;
import com.lcs.joke.ui.adapter.BaseAdapter;
import com.lcs.joke.ui.adapter.TextJokeAdapter;
import com.lcs.joke.utils.DebugLog;

public class MainActivity extends TaskActivity {
    private ActivityMainBinding binding;
    private TextJokeAdapter adapter;
    private int pageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initView();
        getLatestJoke(pageIndex);
    }

    private void initView() {
        adapter = new TextJokeAdapter(MainActivity.this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        binding.recyclerView.setAdapter(adapter);
        adapter.setEmptyTip(getString(R.string.tip_error));
        DebugLog.e("xxxx " + System.currentTimeMillis());
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Joke joke = adapter.getItem(position);
                share(joke.content);
            }
        });
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!adapter.hasFooterView) {
                    return;
                }

                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = lm.getChildCount();
                int totalItemCount = lm.getItemCount();
                int firstVisiblePosition = lm.findFirstVisibleItemPosition();

                if ((firstVisiblePosition + visibleItemCount) >= totalItemCount) {
                    getLatestJoke(++pageIndex);
                }
            }
        });
    }

    private void share(String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        startActivity(intent);
    }

    public void getLatestJoke(int pageIndex) {
        NetCallback<BaseResponse<JokeResponse>> callback = new NetCallback<BaseResponse<JokeResponse>>() {
            @Override
            public void onSuccess(BaseResponse<JokeResponse> response) {
                adapter.setLoaded(true);
                if (response.result.data == null || response.result.data.isEmpty()) {
                    adapter.removeFooterView();
                    return;
                }

                if (response.result.data.size() < AppServer.PAGE_SIZE) {
                    adapter.removeFooterView();
                    adapter.addAll(response.result.data);
                    return;
                }

                if (adapter.hasFooterView) {
                    adapter.insert(adapter.getItemCount() - 1, response.result.data);
                } else {
                    adapter.addAll(response.result.data);
                    adapter.addFooterView(new Joke());
                }
            }

            @Override
            public void onError(Throwable e) {
                DebugLog.e("onError : " + e.getMessage());
            }
        };
        addTask(Api.getLatestJoke(pageIndex, callback));
    }
}
