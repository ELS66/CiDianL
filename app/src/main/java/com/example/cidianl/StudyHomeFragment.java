package com.example.cidianl;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudyHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudyHomeFragment extends Fragment {
    Button button;
    List<Word> wordList;
    List<Word> allWord;
    MyViewModel myViewModel;
    DictionarySave dictionarySave;
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
        dictionarySave = new DictionarySave(getActivity().getApplicationContext(),"DICTIONARY");
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        LiveData<List<Word>> listword = myViewModel.getListLiveData("我的");
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
        ImageButton imageButton = requireActivity().findViewById(R.id.imageButton2);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(),R.id.fragment);
                navController.navigate(R.id.action_item_study_to_pipeiFragment);
            }
        });
        ImageButton imageButton1 = requireActivity().findViewById(R.id.imageButton3);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(),R.id.fragment);
                navController.navigate(R.id.action_item_study_to_studyFragment2);
            }
        });
        Button button = requireActivity().findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    allWord = myViewModel.getAllWord();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                StringBuilder builder = new StringBuilder();
                builder.append("id,英语,中文\r\n");
                for (Word u:allWord){
                    builder.append(u.getEnglish()).append(",").append(u.getChinese()).append("\r\n");
                }

                try {
                    String data = builder.toString();
                    Log.d("下载",data);
                    String fileName = "Word.csv";
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/2";
                    if (!new File(path).exists()) {
                        new File(path,fileName);
                    }
                    File file = new File(path,fileName);
                    OutputStream out = new FileOutputStream(file);
                    out.write(data.getBytes());
                    out.close();
                    Toast.makeText(requireContext(),"yes",Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(),"no",Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button button1 = requireActivity().findViewById(R.id.button6);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewModel.getAllDictionary().getValue().add("kotlin");
            }
        });
        /*button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/2","Word.csv");
                FileInputStream fileInputStream;
                Scanner scanner;
                try {
                    fileInputStream = new FileInputStream(file);
                    scanner = new Scanner(fileInputStream,"UTF-8");
                    scanner.nextLine();
                    while (scanner.hasNextLine()){
                        String soureString = scanner.nextLine();
                        Log.d("下载",soureString);
                        Pattern pattern = Pattern.compile(",");
                        Matcher matcher = pattern.matcher(soureString);
                        String[] result = pattern.split(soureString);
                        Word newWord = new Word(result[0],result[1],"我的");
                        myViewModel.insertWords(newWord);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });**/
    }

    @Override
    public void onPause() {
        super.onPause();
        dictionarySave.save("DICTIONARY",myViewModel.getAllDictionary().getValue());
    }
}