package com.example.catch_taboo.ui.taboo;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.catch_taboo.ui.word.WordViewModel;

public class TabooModelFactory implements ViewModelProvider.Factory {

    private String mGameName;

    public TabooModelFactory(String gameName) {
        mGameName = gameName;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TabooViewModel(mGameName);
    }
}
