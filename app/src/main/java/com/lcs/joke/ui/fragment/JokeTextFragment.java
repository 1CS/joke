package com.lcs.joke.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
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
import com.lcs.joke.ui.adapter.TextJokeAdapter;
import com.lcs.joke.utils.DebugLog;
import com.lcs.joke.utils.rxbus.Callback;
import com.lcs.joke.utils.rxbus.RxBus;

public class JokeTextFragment extends TaskFragment {
    private TextJokeAdapter adapter;
    private int pageIndex = 1;
    private String time;
    private FragmentTabBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tab, container, false);
        initView();
        time = String.valueOf(System.currentTimeMillis() / 1000);
        loadData(pageIndex);
        return binding.getRoot();
    }

    private void initView() {
        adapter = new TextJokeAdapter(getActivity());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
        binding.recyclerView.setAdapter(adapter);
        adapter.setEmptyTip(getString(R.string.tip_error));

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
                    loadData(++pageIndex);
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

    public void loadData(int pageIndex) {
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
        addTask(Api.getListJoke(pageIndex, time, callback));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().unsubscribe(this);
    }
}
