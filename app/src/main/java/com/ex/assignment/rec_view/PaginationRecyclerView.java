package com.ex.assignment.rec_view;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ex.assignment.adapter.PaginationAdapter;
import com.ex.assignment.model.UnSplashPicture;
import com.ex.assignment.retrofit.API;
import com.ex.assignment.retrofit.RetrofitControler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class PaginationRecyclerView extends RecyclerView {

    PaginationAdapter paginationAdapter = null;
    LinearLayoutManager linearLayoutManager= null;
    CompletionListener completionListener = null;
    private String query;

    public void setCompletionListener(CompletionListener completionListener) {
        this.completionListener = completionListener;
    }

    public PaginationRecyclerView(@NonNull Context context) {
        super(context);
        initRecyclerView();
    }

    public PaginationRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initRecyclerView();
    }

    public void setAdapterListener(AdapterListener adapterListener) {
        paginationAdapter.setAdapterListener(adapterListener);
    }


    public PaginationRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRecyclerView();
    }

    private void initRecyclerView() {
        setHasFixedSize(true);
        paginationAdapter = new PaginationAdapter(getContext());
       // paginationAdapter.setHasStableIds(true);
        setAdapter(paginationAdapter);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        setLayoutManager(linearLayoutManager);
        addOnScrollListener();
    }

    public void resetRecyclerView() {
        paginationAdapter.setmItems(null);
        currentPage = PAGE_START;
        isLastPage = false;
        isLoading = false;
    }
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = PAGE_START;

    public void addOnScrollListener() {
        super.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                searchForPictures(currentPage,query);
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

    }

    private void searchForPictures(int page,String query) {
        if(completionListener!= null && page == 1) completionListener.onProgress();
        if(query == null || query.isEmpty()){
            paginationAdapter.setmItems(null);
            return;
        }
        RetrofitControler.getCaller().create(API.class).searchForPic(page, query).enqueue(new Callback<UnSplashPicture>() {
            @Override
            public void onResponse(Call<UnSplashPicture> call, Response<UnSplashPicture> response) {
                if(response.body() == null){
                    paginationAdapter.removeLoadingFooter();
                    isLastPage = true;
                    isLoading = false;
                    if(page == 1)  showErrorDialog("Something went wrong on our end");
                    return;

                }
                if(response.body().results.size() < 10){
                    isLastPage = true;
                    paginationAdapter.removeLoadingFooter();
                    paginationAdapter.addAll(response.body().results);
                }else{

                    paginationAdapter.removeLoadingFooter();
                    paginationAdapter.addAll(response.body().results);
                    paginationAdapter.addLoadingFooter();

                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<UnSplashPicture> call, Throwable t) {
                if (t instanceof HttpException) {
                    switch (((HttpException) t).code()) {
                        case 400:
                            showErrorDialog("Bad Request\tThe request was unacceptable, often due to missing a required parameter");
                          //  Log.e("Error", "Bad Request\tThe request was unacceptable, often due to missing a required parameter");
                            break;
                        case 401:
                            showErrorDialog("Error\", \"Unauthorized\\tInvalid Access Token");
                            Log.e("Error", "Unauthorized\tInvalid Access Token");
                            break;
                        case 403: showErrorDialog("Error\", \"Forbidden\\tMissing permissions to perform request");
                            Log.e("Error", "Forbidden\tMissing permissions to perform request");
                            break;
                        case 404:
                            showErrorDialog("Not Found\tThe requested resource doesn’t exist");
                            Log.e("Error", "Not Found\tThe requested resource doesn’t exist");
                            break;
                        case 500:
                        case 503:
                            showErrorDialog("Something went wrong on our end");
                            Log.e("Error", "Something went wrong on our end");
                            break;
                        default:
                            break;
                    }

                }else{
                    showErrorDialog("Something went wrong on our end");
                    Log.e("Error", "Something went wrong on our end");
                }
                //  progressBar.setVisibility(View.GONE);
            }
        });
    }
    private void showErrorDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void startSearching(String query) {
      //  if(completionListener!= null) completionListener.onProgress();
        this.query = query;
        paginationAdapter.addLoadingFooter();
        searchForPictures(PAGE_START,query);
    }
}
