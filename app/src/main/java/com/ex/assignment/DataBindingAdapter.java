package com.ex.assignment;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;

import com.ex.assignment.adapter.PaginationAdapter;
import com.ex.assignment.rec_view.AdapterListener;
import com.ex.assignment.rec_view.CompletionListener;
import com.ex.assignment.rec_view.PaginationRecyclerView;
import com.google.android.material.textfield.TextInputLayout;

public class DataBindingAdapter {
    @BindingAdapter({"searchForKeyWord"})
    public static void searchForKeyWord(PaginationRecyclerView paginationRecyclerView, String s) {
       if(paginationRecyclerView ==null  || paginationRecyclerView.getAdapter() == null)
           return;
        paginationRecyclerView.startSearching(s);
    }

    @BindingAdapter({"setCompletionListener"})
    public static void searchForKeyWord(PaginationRecyclerView paginationRecyclerView, CompletionListener completionListener) {
        if(paginationRecyclerView ==null  || paginationRecyclerView.getAdapter() == null)
            return;
        paginationRecyclerView.setCompletionListener(completionListener);
    }

    @BindingAdapter({"setAdapterListener"})
    public static void setAdapterListener(PaginationRecyclerView paginationRecyclerView, AdapterListener adapterListener) {
        if(paginationRecyclerView ==null  || paginationRecyclerView.getAdapter() == null)
            return;
        paginationRecyclerView.setAdapterListener(adapterListener);
    }
}
