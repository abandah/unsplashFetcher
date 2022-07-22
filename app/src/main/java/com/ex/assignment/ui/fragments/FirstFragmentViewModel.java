package com.ex.assignment.ui.fragments;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FirstFragmentViewModel extends ViewModel {
    private MutableLiveData<String>searchText = new MutableLiveData<>("");
    FirstFragmentNavigator firstFragmentNavigator;

    public void setFirstFragmentNavigator(FirstFragmentNavigator firstFragmentNavigator) {
        this.firstFragmentNavigator = firstFragmentNavigator;
    }

    public MutableLiveData<String> getSearchText() {
        return searchText;
    }

    public void onClickSearch(View view) {
        if(validate(searchText.getValue()))
        firstFragmentNavigator.onClickSearch(searchText.getValue());


    }

    private boolean validate(String query){
        if(query.isEmpty()){
           // binding.textviewFirst.setError("Please enter a query");
            return false;
        }
        return true;
    }
}
