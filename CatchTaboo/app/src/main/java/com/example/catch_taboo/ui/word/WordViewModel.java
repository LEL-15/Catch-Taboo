package com.example.catch_taboo.ui.word;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WordViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public WordViewModel(){
        mText = new MutableLiveData<>();
        mText.setValue("This is word to guess"); //change word here!
    }

    public LiveData<String> getText() {
        return mText;
    }

}
