package com.example.catch_taboo.ui.word;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class WordModelFactory implements ViewModelProvider.Factory {

    private String mGameName;

    public WordModelFactory(String gameName) {
        mGameName = gameName;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new WordViewModel(mGameName);
    }
}
