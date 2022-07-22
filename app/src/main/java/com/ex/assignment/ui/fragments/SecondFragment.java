package com.ex.assignment.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.ex.assignment.MainActivity;
import com.ex.assignment.R;
import com.ex.assignment.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment implements SecondFragmentNavigator {

    private FragmentSecondBinding binding;
    private SecondFragmentViewModel viewModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(SecondFragmentViewModel.class);
        viewModel.setSecondFragmentNavigator(this);
        String keyWord = getArguments().getString("keyWord");
        ((MainActivity)getActivity()).setTitle("Search : "+keyWord);
        viewModel.searchForKeyWord(getArguments().getString("keyWord"));
        binding.setViewModel(viewModel);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void backToFirstFragment() {
        NavHostFragment.findNavController(SecondFragment.this).navigate(R.id.action_SecondFragment_to_FirstFragment);
    }

    @Override
    public void openExternalLink(String link) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(browserIntent);
    }
}