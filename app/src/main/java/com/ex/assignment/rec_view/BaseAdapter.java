package com.ex.assignment.rec_view;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ex.assignment.R;
import com.ex.assignment.adapter.PaginationAdapter;
import com.ex.assignment.model.UnSplashPicture;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LinkedBlockingDeque<Operation> mQueue = new LinkedBlockingDeque<>();
    private List<Object> mItems = new LinkedList<>();
    ;
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private boolean isUpdateHandlerRunning = false;
    private UpdateHandler mHandler = new UpdateHandler();
    private int mConsecutiveOps = 8;
    private boolean isLoadingAdded = false;


    private void addOperation(boolean letItStart, Operation operation) {
        mQueue.add(operation);
        if (letItStart)
            startAdapterUpdater();
    }

    private Runnable adapterUpdater = new Runnable() {
        @Override
        public void run() {
            isUpdateHandlerRunning = true;
            try {
                updateItems(mConsecutiveOps);
            } finally {
                if (hasObservers() && !mQueue.isEmpty())
                    mHandler.postDelayed(121, 300, adapterUpdater);
                else
                    stopAdapterUpdater();
            }
        }
    };

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.item_list, parent, false);
                viewHolder = getViewHolder(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingViewHolder(viewLoading);
                break;
        }
        return viewHolder;
    }

    public abstract RecyclerView.ViewHolder getViewHolder(View viewItem);


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        T item = getItem(position);
        switch (getItemViewType(position)) {
            case ITEM:
                PaginationAdapter.ItemViewHolder itemViewHolder = (PaginationAdapter.ItemViewHolder) holder;
                bindItem(itemViewHolder, item);
                break;

            case LOADING:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    protected abstract void bindItem(PaginationAdapter.ItemViewHolder itemViewHolder, T item);


    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }


    private class Operation {
        public int position;
        public TYPE type;
        public T obj;

        Operation(int position, TYPE type, T obj) {
            this.position = position;
            this.type = type;
            this.obj = obj;
        }
    }

    enum TYPE {
        ADD,
        CHANGE,
        REMOVE,
        INSERT
    }

    public class UpdateHandler extends Handler {

        public final boolean postDelayed(int runnableID, long delayMillis, Runnable r) {
            Message m = Message.obtain(this, r);
            m.what = runnableID;
            return sendMessageDelayed(m, delayMillis);
        }

        public final boolean post(int runnableID, Runnable r) {
            return postDelayed(runnableID, 0, r);
        }

        public final boolean hasActiveRunnable(int runnableID) {
            return hasMessages(runnableID);
        }
    }

    private void addOperations(int startIdx, TYPE type, @NotNull List<T> list) {
        for (Iterator<T> objIt = list.iterator(); objIt.hasNext(); ) {
            addOperation(false, new Operation(startIdx++, type, objIt.next()));
        }
        startAdapterUpdater();
    }


    private void updateItems(int consecutiveOps) {
        if (mQueue != null && !mQueue.isEmpty() && hasObservers()) {
            boolean hasOps = false;
            TYPE prevOp = mQueue.peek().type;
            int i = 0;
            int start = mQueue.peek().position;
            while (!mQueue.isEmpty() && i < consecutiveOps) {
                if (prevOp == mQueue.peek().type) {
                    hasOps = true;
                    Operation op = mQueue.poll();
                    if (op.type == TYPE.INSERT) {
                        if (op.position >= mItems.size())
                            mItems.add(op.obj);
                        else
                            mItems.set(op.position, op.obj);
                    } else if (op.type == TYPE.CHANGE) {
                        if (op.obj != null)
                            mItems.set(op.position, op.obj);
                    } else if (op.type == TYPE.ADD) {
                        if (op.position >= mItems.size())
                            mItems.add(op.obj);
                        else
                            mItems.add(op.position, op.obj);
                    }
                    prevOp = op.type;
                    i++;
                } else {
                    hasOps = false;
                    if (hasObservers()) {
                        executeOps(start, i, prevOp);
                        break;
                    }
                }
            }
            if (hasOps && hasObservers()) {
                executeOps(start, i, prevOp);
            }
        }
    }

    private void executeOps(int start, int count, TYPE type) {
        if (type == TYPE.INSERT) {
            if (count == 1)
                notifyItemInserted(getAdapterItemPosition(start));
            else
                notifyItemRangeInserted(getAdapterItemPosition(start),
                        count);
        } else if (type == TYPE.CHANGE) {
            if (count == 1)
                notifyItemChanged(getAdapterItemPosition(start));
            else
                notifyItemRangeChanged(getAdapterItemPosition(start),
                        count);
        } else if (type == TYPE.REMOVE) {
            if (count == 1) {
                mItems.remove(start);
                notifyItemRemoved(getAdapterItemPosition(start));
            } else {
                mItems.subList(start, start + count).clear();
                notifyItemRangeRemoved(getAdapterItemPosition(start),
                        count);
            }
        } else if (type == TYPE.ADD) {
            if (count == 1)
                notifyItemChanged(getAdapterItemPosition(start));
            else
                notifyItemRangeChanged(getAdapterItemPosition(start),
                        getItemCount());
        }
    }

    private int getAdapterItemPosition(int pos) {
        return pos;
    }

    private void startAdapterUpdater() {
        if (!mHandler.hasActiveRunnable(121))
            adapterUpdater.run();
    }

    private void stopAdapterUpdater() {
        isUpdateHandlerRunning = false;
        mHandler.removeCallbacks(adapterUpdater);
    }

    public T getItem(int position) {
        return (T) mItems.get(position);
    }

    public void setmItems(T mItems) {
        if (mItems == null) {
            this.mItems.clear();
            notifyDataSetChanged();
            return;
        }
        this.mItems.add(null);
        notifyDataSetChanged();
    }

    public void addAll(List<T> nodes) {
        if (nodes == null || nodes.isEmpty())
            return;
        int pos = mItems.size();
        if (mItems.isEmpty())
            mItems.addAll(nodes.size() > mConsecutiveOps ? nodes.subList(0, mConsecutiveOps) : nodes);
        else
            mItems.addAll(pos, nodes.size() > mConsecutiveOps ? nodes.subList(0, mConsecutiveOps) : nodes);

        notifyItemRangeInserted(getAdapterItemPosition(pos), nodes.size() > mConsecutiveOps
                ? mConsecutiveOps
                : nodes.size());

        if (nodes.size() > mConsecutiveOps) {
            addOperations(getAdapterItemPosition(pos) + mConsecutiveOps,
                    TYPE.ADD,
                    nodes.subList(mConsecutiveOps, nodes.size()));
        }
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        mItems.add(new Loading());
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = getItemCount() - 1;
        if (position < 0) return;
        T result = getItem(position);

        if (result != null) {
            mItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(T item) {
        mItems.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        Object item = mItems.get(position);
        if (item instanceof Loading) {
            return LOADING;
        } else {
            return ITEM;
        }
        //   return (position == getItemCount() - 1 && isLoadingAdded)|| getItem(position).sEmpty() ? LOADING : ITEM ;
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);

        }
    }

    private static class Loading {
        public boolean isEmpty() {
            return true;
        }
    }

//
//    public void addAll(List<UnSplashPicture.Result> moveResults) {
//        for (UnSplashPicture.Result result : moveResults) {
//            add(result);
//        }
//        notifyDataSetChanged();
//    }
}
