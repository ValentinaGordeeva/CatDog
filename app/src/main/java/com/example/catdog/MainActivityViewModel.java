package com.example.catdog;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {
    public final ObservableField<String> message = new ObservableField<>();
}
