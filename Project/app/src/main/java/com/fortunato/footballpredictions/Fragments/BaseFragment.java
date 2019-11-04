package com.fortunato.footballpredictions.Fragments;

import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.fortunato.footballpredictions.DataStructures.BaseType;

public abstract class BaseFragment extends Fragment {
    public abstract void addItem(BaseType object);

    public abstract ProgressBar getProgBar();

    public abstract void flush();
}
