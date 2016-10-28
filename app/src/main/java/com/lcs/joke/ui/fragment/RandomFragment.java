package com.lcs.joke.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lcs.joke.R;
import com.lcs.joke.databinding.FragmentRandomBinding;
import com.lcs.joke.databinding.FragmentTabBinding;
import com.lcs.joke.net.Api;
import com.lcs.joke.net.NetCallback;
import com.lcs.joke.net.bean.Joke;
import com.lcs.joke.net.bean.response.BaseResponse;
import com.lcs.joke.net.bean.response.JokeResponse;
import com.lcs.joke.ui.adapter.BaseAdapter;
import com.lcs.joke.ui.adapter.JokeTextAdapter;
import com.lcs.joke.utils.DebugLog;

public class RandomFragment extends TaskFragment {
    private JokeTextAdapter adapter;
    private FragmentRandomBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_random, container, false);
        initView();
        getLatestJoke();
        return binding.getRoot();
    }

    private void initView() {
        adapter = new JokeTextAdapter(getActivity());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
        binding.recyclerView.setAdapter(adapter);
        adapter.setEmptyTip(getString(R.string.tip_error));
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Joke joke = adapter.getItem(position);
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

    public void getLatestJoke() {
        NetCallback<BaseResponse<JokeResponse>> callback = new NetCallback<BaseResponse<JokeResponse>>() {
            @Override
            public void onSuccess(BaseResponse<JokeResponse> response) {
                adapter.setLoaded(true);
                adapter.addAll(response.result.data);
            }

            @Override
            public void onError(Throwable e) {
                DebugLog.e("onError : " + e.getMessage());
            }
        };
        addTask(Api.getLatestJoke(callback));
    }
}
