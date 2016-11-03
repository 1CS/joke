package com.lcs.joke.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.lcs.joke.R;
import com.lcs.joke.databinding.ActivityMainBinding;
import com.lcs.joke.net.Api;
import com.lcs.joke.net.NetCallback;
import com.lcs.joke.net.bean.Joke;
import com.lcs.joke.net.bean.response.BaseResponse;
import com.lcs.joke.ui.adapter.JokeTextAdapter;
import com.lcs.joke.utils.DebugLog;
import com.lcs.joke.utils.rxbus.Callback;
import com.lcs.joke.utils.rxbus.RxBus;

public class MainActivity extends TaskActivity {
    private ActivityMainBinding binding;
    private JokeTextAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initLayout();
        loadData();
    }

    private void initLayout() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.recyclerView.scrollToPosition(0);
            }
        });

        adapter = new JokeTextAdapter(getApplicationContext());
        adapter.setEmptyTip(getString(R.string.tip_error));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL,
            false));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    binding.fab.hide();
                } else {
                    LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (lm.findFirstVisibleItemPosition() == 0) {
                        binding.fab.hide();
                    } else {

                        binding.fab.show();
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!adapter.hasFooterView) {
                    return;
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (lm.findLastVisibleItemPosition() + 1 == lm.getItemCount()) {
                        loadMore();
                    }
                }
            }
        });

        RxBus.getDefault().subscribe(this, new Callback<Joke>() {
            @Override
            public void onEvent(Joke joke) {
                share(joke.content);
            }
        });
    }

    private void share(String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        startActivity(intent);
    }

    public void loadMore() {
        NetCallback<BaseResponse> callback = new NetCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if (response.result == null || response.result.isEmpty()) {
                    adapter.removeFooterView();
                    return;
                }

                adapter.insert(adapter.getItemCount() - 1, response.result);
            }

            @Override
            public void onError(Throwable e) {
                DebugLog.e("onError : " + e.getMessage());
            }
        };
        addTask(Api.getJoke(callback));
    }

    private void loadData() {
        NetCallback<BaseResponse> callback = new NetCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                adapter.setLoaded(true);
                binding.swipeRefreshLayout.setRefreshing(false);
                if (response.result == null || response.result.isEmpty()) {
                    return;
                }

                adapter.clear();
                adapter.addAll(response.result);
                adapter.addFooterView(new Joke());
            }

            @Override
            public void onError(Throwable e) {
                DebugLog.e("onError : " + e.getMessage());
                adapter.setLoaded(true);
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        };
        addTask(Api.getJoke(callback));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().unsubscribe(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showAboutDialog();
        return true;
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("联系我们");
        builder.setMessage("如有改善建议或问题，随时欢迎联系！\nQQ：10907315\n微信号：liu_congshan");
        builder.setPositiveButton("确定", null);
        builder.show();
    }
}
