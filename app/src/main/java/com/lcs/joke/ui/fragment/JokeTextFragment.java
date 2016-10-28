package com.lcs.joke.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lcs.joke.R;
import com.lcs.joke.databinding.FragmentTabBinding;
import com.lcs.joke.net.Api;
import com.lcs.joke.net.NetCallback;
import com.lcs.joke.net.bean.Joke;
import com.lcs.joke.net.bean.response.BaseResponse;
import com.lcs.joke.net.bean.response.JokeResponse;
import com.lcs.joke.net.retrofit.AppServer;
import com.lcs.joke.ui.adapter.JokeTextAdapter;
import com.lcs.joke.utils.DebugLog;
import com.lcs.joke.utils.rxbus.Callback;
import com.lcs.joke.utils.rxbus.RxBus;

public class JokeTextFragment extends TaskFragment {
    private JokeTextAdapter adapter;
    private int pageIndex = 1;
    private String time;
    private FragmentTabBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tab, container, false);
        initView();
        time = String.valueOf(System.currentTimeMillis() / 1000);
        loadData();
        return binding.getRoot();
    }

    private void initView() {
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        adapter = new JokeTextAdapter(getActivity());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
        binding.recyclerView.setAdapter(adapter);
        adapter.setEmptyTip(getString(R.string.tip_error));
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.recyclerView.scrollToPosition(0);
            }
        });

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
                if (TextUtils.isEmpty(joke.url)) {
                    share(joke.content);
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

    public void loadMore() {
        pageIndex++;
        NetCallback<BaseResponse<JokeResponse>> callback = new NetCallback<BaseResponse<JokeResponse>>() {
            @Override
            public void onSuccess(BaseResponse<JokeResponse> response) {
                if (response.result.data == null || response.result.data.isEmpty()) {
                    adapter.removeFooterView();
                    return;
                }

                if (response.result.data.size() < AppServer.PAGE_SIZE) {
                    adapter.removeFooterView();
                    adapter.addAll(response.result.data);
                    return;
                }

                adapter.insert(adapter.getItemCount() - 1, response.result.data);
            }

            @Override
            public void onError(Throwable e) {
                DebugLog.e("onError : " + e.getMessage());
            }
        };
        addTask(Api.getListJoke(pageIndex, time, callback));
    }

    private void loadData() {
        pageIndex = 1;
        NetCallback<BaseResponse<JokeResponse>> callback = new NetCallback<BaseResponse<JokeResponse>>() {
            @Override
            public void onSuccess(BaseResponse<JokeResponse> response) {
                adapter.setLoaded(true);
                binding.swipeRefreshLayout.setRefreshing(false);
                if (response.result.data == null || response.result.data.isEmpty()) {
                    return;
                }

                adapter.clear();
                adapter.addAll(response.result.data);
                if (response.result.data.size() == AppServer.PAGE_SIZE) {
                    adapter.addFooterView(new Joke());
                }
            }

            @Override
            public void onError(Throwable e) {
                DebugLog.e("onError : " + e.getMessage());
                adapter.setLoaded(true);
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        };
        addTask(Api.getListJoke(pageIndex, time, callback));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().unsubscribe(this);
    }
}
