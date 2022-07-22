package com.ex.assignment.ui.fragments;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ex.assignment.rec_view.AdapterListener;
import com.ex.assignment.rec_view.CompletionListener;

public class SecondFragmentViewModel extends ViewModel implements CompletionListener, AdapterListener {
    SecondFragmentNavigator secondFragmentNavigator;
    private MutableLiveData<String>searchText = new MutableLiveData<>("");
    private MutableLiveData<Integer>visibilityOfLoading = new MutableLiveData<>(View.GONE);

    public MutableLiveData<Integer> getVisibilityOfLoading() {
        return visibilityOfLoading;
    }

    public MutableLiveData<String> getSearchText() {
        return searchText;
    }

    public void setSecondFragmentNavigator(SecondFragmentNavigator secondFragmentNavigator) {
        this.secondFragmentNavigator = secondFragmentNavigator;
    }

    public void searchForKeyWord(String keyWord) {
        searchText.setValue(keyWord);

    }

    @Override
    public void onComplete() {
        visibilityOfLoading.setValue(View.GONE);
    }

    @Override
    public void onProgress() {
        visibilityOfLoading.setValue(View.VISIBLE);

    }

    @Override
    public void onItemClick(String link) {
        secondFragmentNavigator.openExternalLink(link);

    }
}
