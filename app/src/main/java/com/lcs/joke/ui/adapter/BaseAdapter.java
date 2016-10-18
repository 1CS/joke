package com.lcs.joke.ui.adapter;

import android.content.Context;
import android.databinding.ObservableField;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lcs.joke.R;
import com.lcs.joke.net.bean.BaseItem;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T extends BaseItem> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ObservableField<String> mTip = new ObservableField<>();
    public boolean hasFooterView = false;
    public boolean hasHeaderView = false;
    protected OnItemClickListener onItemClickListener;
    protected Context mContext;
    private boolean isLoaded = false;
    private List<T> mList;
    private int mLayoutId;

    public BaseAdapter(Context context, int layoutId) {
        mContext = context;
        mList = new ArrayList<>();
        mLayoutId = layoutId;
    }

    public void addAll(List<T> list) {
        mList.addAll(list);
        if (mList.size() == list.size()) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(getItemCount() - list.size() - 1, list.size());
        }
    }

    public void add(T t) {
        mList.add(t);
        if (mList.size() == 1) {
            notifyDataSetChanged();
        } else {
            notifyItemInserted(getItemCount() - 1);
        }
    }

    public void insert(int index, T t) {
        if (index > mList.size()) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            mList.add(index, t);
        }

        if (mList.size() == 1) {
            notifyDataSetChanged();
        } else {
            notifyItemInserted(index);
        }
    }

    public void insert(int index, List<T> list) {
        if (index > mList.size()) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            mList.addAll(index, list);
        }

        if (mList.size() == list.size()) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(index, list.size());
        }
    }

    public void remove(int index) {
        removeWithoutNotify(index);
        notifyItemRemoved(index + 1);
    }

    public void removeWithoutNotify(int index) {
        mList.remove(index);
    }

    public void clear() {
        mList.clear();
        hasFooterView = false;
        isLoaded = false;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ItemType.FOOTER.ordinal()) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_load_more, parent, false);
            return new ViewHolderFooter(view);
        }

        if (viewType == ItemType.EMPTY.ordinal()) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_empty, parent, false);
            //            ViewGroup.LayoutParams lp = view.getLayoutParams();
            //            lp.height = parent.getHeight();
            return new ViewHolderEmpty(view);
        }

        if (viewType != ItemType.DEFAULT.ordinal()) {
            return createTypeViewHolder(parent, viewType);
        }

        View view = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
        return createViewHolder(view);
    }

    public abstract RecyclerView.ViewHolder createViewHolder(View view);

    public RecyclerView.ViewHolder createTypeViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.isEmpty()) {
            return ItemType.EMPTY.ordinal();
        }

        if (hasHeaderView) {
            if (position == 0) {
                return ItemType.HEADER.ordinal();
            }
            if (hasFooterView && position == getDataSize()) {
                return ItemType.FOOTER.ordinal();
            }
        } else if (hasFooterView && position == getDataSize() - 1) {
            return ItemType.FOOTER.ordinal();
        }

        return getItem(position).itemType.ordinal();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderEmpty) {
            ((ViewHolderEmpty) holder).bind(mTip, isLoaded);
            return;
        }
        /**
         * 调用接口回调
         */
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onItemClickListener) {
                    onItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            }
        });
    }

    public void setEmptyTip(String tip) {
        mTip.set(tip);
    }

    public T getItem(int index) {
        if (mList.isEmpty() || index > mList.size()) {
            return null;
        }
        return mList.get(index);
    }

    public void setLoaded(boolean isLoaded) {
        this.isLoaded = isLoaded;
        notifyDataSetChanged();
    }

    /**
     * Just for draw
     *
     * @return 1 if data is empty,size of data otherwise.
     */
    @Override
    public int getItemCount() {
        return mList.isEmpty() ? 1 : mList.size();
    }

    /**
     * Real data size
     *
     * @return data size
     */
    public int getDataSize() {
        return mList.size();
    }

    /**
     * 设置监听方法
     *
     * @param listener OnItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void addFooterView(T t) {
        if (hasFooterView) {
            return;
        }

        hasFooterView = true;
        add(t);
    }

    public void removeFooterView() {
        if (hasFooterView) {
            hasFooterView = false;
            remove(getDataSize() - 1);
        }
    }

    public enum ItemType {DEFAULT, HEADER, FOOTER, EVEN, EMPTY}

    /**
     * item click回调方法
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
