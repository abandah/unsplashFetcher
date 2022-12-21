package com.ex.assignment.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
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
import com.ex.assignment.rec_view.AdapterListener;
import com.ex.assignment.rec_view.BaseAdapter;

public class PaginationAdapter extends BaseAdapter<UnSplashPicture.Result> {

    private Context context;

    AdapterListener adapterListener;

    public void setAdapterListener(AdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }

    public PaginationAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View viewItem) {
        return new ItemViewHolder(viewItem);
    }

    @Override
    protected void bindItem(ItemViewHolder itemViewHolder, UnSplashPicture.Result item) {
        Glide.with(context)
                .load(item.urls.full)
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
    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if(holder instanceof ItemViewHolder) {
            Glide.with(context).clear(((ItemViewHolder) holder).movieImage);
        }
      //  holder.unbind();
    }




}
