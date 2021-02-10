package com.example.gymarbete.ui.whiteList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WhiteListModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WhiteListModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}