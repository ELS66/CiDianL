package com.example.cidianl;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PipeiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PipeiFragment extends Fragment {

    Timer timer1;

    public PipeiFragment() {
        // Required empty public constructor
    }


    public static PipeiFragment newInstance(String param1, String param2) {
        PipeiFragment fragment = new PipeiFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pipei, container, false);
        MyViewModel myViewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        myViewModel.start = new Date(System.currentTimeMillis());
        myViewModel.x1 = 0;
        myViewModel.b1 = 0;
        myViewModel.i1 = 0;
        myViewModel.i2 = 0;
        myViewModel.i3 = 0;
        myViewModel.numno = 0;
        Date start = null;
        Button buttona1,buttona2,buttona3,buttona4,buttona5,buttona6,buttona7,buttona8,buttona9,buttona10,buttona11,buttona12;
        TextView textViewno = view.findViewById(R.id.textViewno);
        buttona1 = view.findViewById(R.id.buttona1);
        buttona2 = view.findViewById(R.id.buttona2);
        buttona3 = view.findViewById(R.id.buttona3);
        buttona4 = view.findViewById(R.id.buttona4);
        buttona5 = view.findViewById(R.id.buttona5);
        buttona6 = view.findViewById(R.id.buttona6);
        buttona7 = view.findViewById(R.id.buttona7);
        buttona8 = view.findViewById(R.id.buttona8);
        buttona9 = view.findViewById(R.id.buttona9);
        buttona10 = view.findViewById(R.id.buttona10);
        buttona11 = view.findViewById(R.id.buttona11);
        buttona12 = view.findViewById(R.id.buttona12);
        List<Button> buttonList = Arrays.asList(buttona1,buttona2,buttona3,buttona4,buttona5,buttona6,buttona7,buttona8,buttona9,buttona10,buttona11,buttona12);
        List<Word> wordlist = null;
        try {
             wordlist = myViewModel.getPipeiWord();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        List<String > stringList = new ArrayList<>(12);
        List<String> englishList = new ArrayList<>(6);
        List<String> chineseList = new ArrayList<>(6);
        if (wordlist != null) {
            for (int i = 0;i<6 ; i++) {
                englishList.add(wordlist.get(i).getEnglish());
            }
            for (int i = 0;i<6 ; i++) {
            chineseList.add(wordlist.get(i).getChinese());
            }
            for (int i = 0;i<6 ; i++) {
                stringList.add(wordlist.get(i).getEnglish());
                stringList.add(wordlist.get(i).getChinese());
            }
        }
        List<String> text = new ArrayList<>(12);
        for (int i = 0; i<12 ; i++) {
            text.add(stringList.get(i));
        }
        Collections.shuffle(text);
        Log.d("下载",String.valueOf(stringList));
        Log.d("下载",String.valueOf(text));
        for (int i = 0; i <12 ; i++) {
            buttonList.get(i).setText(text.get(i));
        }
        for (int i=0; i<12 ;i++) {

            int finalI = i;
            buttonList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myViewModel.x1 == 0) {
                        buttonList.get(finalI).setBackgroundColor(Color.YELLOW);
                        String string = buttonList.get(finalI).getText().toString();
                        myViewModel.i1 = englishList.indexOf(string);
                        if (myViewModel.i1 == -1) {
                            myViewModel.i1 = chineseList.indexOf(string);
                        }
                        myViewModel.b1 = finalI;
                        myViewModel.x1 = myViewModel.x1 + 1;
                    } else {
                        String string = buttonList.get(finalI).getText().toString();
                        if (myViewModel.b1 == finalI) {
                            buttonList.get(finalI).setBackgroundColor(Color.WHITE);
                            myViewModel.x1 = 0;
                        } else {
                        myViewModel.i2 = englishList.indexOf(string);
                        if (myViewModel.i2 == -1){
                            myViewModel.i2 = chineseList.indexOf(string);
                        }
                        if (myViewModel.i1 == myViewModel.i2) {
                            buttonList.get(finalI).setBackgroundColor(Color.GREEN);
                            buttonList.get(myViewModel.b1).setBackgroundColor(Color.GREEN);
                            int c1 = myViewModel.b1;
                            myViewModel.b1 = 0;
                            myViewModel.x1 = 0;
                            Timer timer = new Timer();
                            TimerTask task = new TimerTask() {
                                @Override
                                public void run() {
                                    buttonList.get(finalI).setVisibility(View.INVISIBLE);
                                    buttonList.get(c1).setVisibility(View.INVISIBLE);
                                    myViewModel.i3 ++;
                                    if (myViewModel.i3 == 6) {
                                        Message message = new Message();
                                        message.what = 0x0526;
                                        handler.sendMessage(message);
                                    }
                                }
                            };
                            timer.schedule(task,200);

                        } else {
                            int d1 = myViewModel.b1;
                            buttonList.get(finalI).setBackgroundColor(Color.RED);
                            buttonList.get(d1).setBackgroundColor(Color.RED);
                            myViewModel.numno = myViewModel.numno + 1;
                            textViewno.setText(String.valueOf(myViewModel.numno));
                            myViewModel.x1 = 0;
                            Timer timer = new Timer();
                            TimerTask task = new TimerTask() {
                                @Override
                                public void run() {
                                    buttonList.get(finalI).setBackgroundColor(Color.WHITE);
                                    buttonList.get(d1).setBackgroundColor(Color.WHITE);
                                    myViewModel.b1 = 0;
                                }
                            };
                            timer.schedule(task,200);
                        }
                        }
                    }
                }
            });
        }
        TimerTask task =  new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 0x0527;
                handler.sendMessage(message);
            }
        };
        timer1 = new Timer();
        timer1.schedule(task,90,90);


        return view;


    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            TextView textView = requireActivity().findViewById(R.id.textView);
            MyViewModel myViewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
            if (msg.what == 0x0527){
                Date end = new Date(System.currentTimeMillis());
                long diff = end.getTime() - myViewModel.start.getTime();
                long seconds = (diff % (1000 * 60)) / 1000 ;
                long mseconds = diff % 100;
                textView.setText(seconds + ":" + mseconds);
            }
            if (msg.what == 0x0526) {
                timer1.cancel();
                String text = textView.getText().toString();
                Toast.makeText(requireContext(),text,Toast.LENGTH_SHORT).show();
                NavController navController = Navigation.findNavController(requireActivity(),R.id.fragment);
                navController.navigate(R.id.action_pipeiFragment_to_item_study);
            }
            return false;
        }
    });

}