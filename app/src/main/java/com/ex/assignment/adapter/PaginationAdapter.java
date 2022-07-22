package com.ex.assignment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ex.assignment.R;
import com.ex.assignment.model.UnSplashPicture;
import com.ex.assignment.model.UnSplashPicture;
import com.ex.assignment.rec_view.AdapterListener;

import java.util.LinkedList;
import java.util.List;


public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<UnSplashPicture.Result> resultList;
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private boolean isLoadingAdded = false;
    AdapterListener adapterListener;

    public void setAdapterListener(AdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }

    public PaginationAdapter(Context context) {
        this.context = context;
        resultList = new LinkedList<>();
    }

    public void setResultList(UnSplashPicture resultList) {
        if(resultList == null ||resultList.results == null) {
            this.resultList.clear();
            notifyDataSetChanged();
            return;
        }
        this.resultList = resultList.results;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.item_list, parent, false);
                viewHolder = new ItemViewHolder(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingViewHolder(viewLoading);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        UnSplashPicture.Result item = resultList.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            //    movieViewHolder.movieTitle.setText(movie.getTitle());
//                if (item == null) {
//                    return;
//                } else if (item.user == null) {
//                    return;
//                } else if (item.user.getName() == null) {
//                    return;
//                } else if (item.getAlt_description() == null) {
//                    return;
//                } else if (item.urls == null) {
//                    return;
//                } else if (item.getThumbnail() == null) {
//                    return;
//                }
                Glide.with(context)
                        .load(item.getThumbnail())
                        .placeholder(R.drawable.image)
                        .error(R.drawable.image)
                        .apply(RequestOptions.centerCropTransform())
                        .into(itemViewHolder.movieImage);

                itemViewHolder.artistname.setText(item.getName());
                itemViewHolder.artistname.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(adapterListener!=null && item.user != null && item.user.portfolio_url != null && !item.user.portfolio_url.isEmpty())
                            adapterListener.onItemClick(item.user.portfolio_url);
                    }
                });
                itemViewHolder.description.setText(item.getAlt_description());
                break;

            case LOADING:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return resultList == null ? 0 : resultList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == resultList.size() - 1 && isLoadingAdded)||resultList.get(position).isEmpty() ? LOADING : ITEM ;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new UnSplashPicture.Result());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = resultList.size() - 1;
        if(position <0) return;
        UnSplashPicture.Result result = getItem(position);

        if (result != null) {
            resultList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(UnSplashPicture.Result item) {
        resultList.add(item);
        notifyItemInserted(resultList.size() - 1);
    }

    public void addAll(List<UnSplashPicture.Result> moveResults) {
        for (UnSplashPicture.Result result : moveResults) {
            add(result);
        }
        notifyDataSetChanged();
    }

    public UnSplashPicture.Result getItem(int position) {
        return resultList.get(position);
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView description,artistname;
        private ImageView movieImage;

        public ItemViewHolder(View itemView) {
            super(itemView);
         //   movieTitle = (TextView) itemView.findViewById(R.id.movie_title);
            movieImage = (ImageView) itemView.findViewById(R.id.thumbnail);
            description =  itemView.findViewById(R.id.description);
            artistname =  itemView.findViewById(R.id.artistname);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);

        }
    }


}
