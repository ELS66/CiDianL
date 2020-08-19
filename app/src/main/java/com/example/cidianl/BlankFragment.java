package com.example.cidianl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class BlankFragment extends Fragment {

    //private BlankViewModel mViewModel;
    private MyViewModel myViewModel;
    Button button;
    String lsx;
    TextView textView;
    List<Word> list;


    public static BlankFragment newInstance() {
        return new BlankFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.blank_fragment, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = ViewModelProviders.of(this).get(BlankViewModel.class);
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        // TODO: Use the ViewModel

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.onItemSelected();
            }
        });

    }

    private Word queryByEnglish(String english) {
        final Word word = WordDatabase.getDatabase(requireContext()).getWordDao().getSelectWord(english);
        return word;
    }


}