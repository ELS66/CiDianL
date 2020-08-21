package com.example.cidianl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudyHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudyHomeFragment extends Fragment {
    Button button;
    List<Word> wordList;
    public StudyHomeFragment() {
        // Required empty public constructor
    }

    public static StudyHomeFragment newInstance(String param1, String param2) {
        StudyHomeFragment fragment = new StudyHomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //NavController navController = Navigation.findNavController(requireActivity(),R.id.fragment);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_study_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StudyAdatpter studyAdatpter = new StudyAdatpter();
        ViewPager2 viewPager2 = requireActivity().findViewById(R.id.viewPage);
        MyViewModel myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        LiveData<List<Word>> listword = myViewModel.getListLiveData();
        listword.observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                int temp = studyAdatpter.getItemCount();
                wordList = words;
                studyAdatpter.submitList(words);
            }
        });
        viewPager2.setAdapter(studyAdatpter);
        viewPager2.setOffscreenPageLimit(1);
        viewPager2.setPageTransformer(new MarginPageTransformer(100));
    }
}