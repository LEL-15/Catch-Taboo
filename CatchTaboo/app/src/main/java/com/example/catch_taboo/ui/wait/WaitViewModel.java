package com.example.catch_taboo.ui.wait;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WaitViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WaitViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Someone on your team has a word.\n Help guess what it is!");
    }

    public LiveData<String> getText() {
        return mText;
    }

}
