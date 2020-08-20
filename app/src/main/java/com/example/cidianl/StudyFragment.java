package com.example.cidianl;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StudyFragment extends Fragment {

    TextView textViewWord, textViewDate, textViewNum1, textViewNum2, textView1, textView2, textView3;
    Button buttona, buttonb, buttonc, buttond, buttonNext;
    ImageView imageViewdelete, imageViewvoice;
    MyViewModel myViewModel;
    List<Word> words;


    public static StudyFragment newInstance() {
        return new StudyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.study_fragment, container, false);
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        textViewWord = rootView.findViewById(R.id.textViewWord);
        textViewDate = rootView.findViewById(R.id.textViewDate);
        textViewNum1 = rootView.findViewById(R.id.textViewnum1);
        textViewNum2 = rootView.findViewById(R.id.textViewnum2);
        textView1 = rootView.findViewById(R.id.textView1);
        textView2 = rootView.findViewById(R.id.textView2);
        textView3 = rootView.findViewById(R.id.textView3);
        buttona = rootView.findViewById(R.id.button);
        buttonb = rootView.findViewById(R.id.button2);
        buttonc = rootView.findViewById(R.id.button3);
        buttond = rootView.findViewById(R.id.button4);
        buttonNext = rootView.findViewById(R.id.buttonNext);
        imageViewdelete = rootView.findViewById(R.id.imageViewdelete);
        imageViewvoice = rootView.findViewById(R.id.imageButton);
        Word studyWord = null;
        try {
            studyWord = myViewModel.getStudyWord();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        if (studyWord == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext()).setTitle("警告").setMessage("请添加至少四个单词");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.create().show();
        } else {
            List<Word> other = null;
            try {
                other = myViewModel.getOtherWords(studyWord.getEnglish());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            if (other.size() >= 3) {
                String datelast = studyWord.getDate();
                String time = datediff(datelast);
                textViewWord.setText(studyWord.getEnglish());
                List<String> answer = Arrays.asList(studyWord.getChinese(), other.get(0).getChinese(), other.get(1).getChinese(), other.get(2).getChinese());
                Collections.shuffle(answer);
                int a = answer.indexOf(studyWord.getChinese());
                buttona.setText(answer.get(0));
                buttonb.setText(answer.get(1));
                buttonc.setText(answer.get(2));
                buttond.setText(answer.get(3));
                String x1 = String.valueOf(studyWord.getStudystart());
                String x2 = String.valueOf(studyWord.getStudyend());
                textViewNum1.setText(x1);
                textViewNum2.setText(x2 + " / " + x1);
                textViewDate.setText(time);
                Drawable drawable = buttona.getBackground();
                List<Button> buttonList = Arrays.asList(buttona, buttonb, buttonc, buttond);
                Word finalStudyWord = studyWord;
                buttona.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (a == 0) {
                            buttona.setBackgroundColor(Color.GREEN);
                            updateyes(finalStudyWord);
                            try {
                                getNewWord(finalStudyWord);
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            buttonNext.setVisibility(View.VISIBLE);
                        } else {
                            buttona.setBackgroundColor(Color.RED);
                            buttonList.get(a).setBackgroundColor(Color.GREEN);
                            updateno(finalStudyWord);
                            try {
                                getNewWord(finalStudyWord);
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            buttonNext.setVisibility(View.VISIBLE);
                        }
                    }
                });
                Word finalStudyWord1 = studyWord;
                buttonb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (a == 1) {
                            buttonb.setBackgroundColor(Color.GREEN);
                            updateyes(finalStudyWord1);
                            try {
                                getNewWord(finalStudyWord1);
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            buttonNext.setVisibility(View.VISIBLE);
                        } else {
                            buttonb.setBackgroundColor(Color.RED);
                            buttonList.get(a).setBackgroundColor(Color.GREEN);
                            updateno(finalStudyWord1);
                            try {
                                getNewWord(finalStudyWord1);
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            buttonNext.setVisibility(View.VISIBLE);
                        }
                    }
                });
                Word finalStudyWord2 = studyWord;
                buttonc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (a == 2) {
                            buttonc.setBackgroundColor(Color.GREEN);
                            updateyes(finalStudyWord2);
                            try {
                                getNewWord(finalStudyWord2);
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            buttonNext.setVisibility(View.VISIBLE);
                        } else {
                            buttonc.setBackgroundColor(Color.RED);
                            buttonList.get(a).setBackgroundColor(Color.GREEN);
                            updateno(finalStudyWord2);
                            try {
                                getNewWord(finalStudyWord2);
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            buttonNext.setVisibility(View.VISIBLE);
                        }
                    }
                });
                Word finalStudyWord3 = studyWord;
                buttond.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (a == 3) {
                            buttond.setBackgroundColor(Color.GREEN);
                            updateyes(finalStudyWord3);
                            try {
                                getNewWord(finalStudyWord3);
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            buttonNext.setVisibility(View.VISIBLE);
                        } else {
                            buttond.setBackgroundColor(Color.RED);
                            buttonList.get(a).setBackgroundColor(Color.GREEN);
                            updateno(finalStudyWord3);
                            try {
                                getNewWord(finalStudyWord3);
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            buttonNext.setVisibility(View.VISIBLE);
                        }
                    }
                });
                buttonNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        replace();
                    }
                });
                Word finalStudyWord4 = studyWord;
                imageViewdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finalStudyWord4.setIsstudy(false);
                        try {
                            getNewWord(finalStudyWord4);
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        replace();
                    }
                });
                Word finalStudyWord5 = studyWord;
                imageViewvoice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            MediaPlayer mediaPlayer = new MediaPlayer();
                            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/2/" + finalStudyWord5.getEnglish() + ".mp3";
                            mediaPlayer.setDataSource(path);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext()).setTitle("警告").setMessage("请添加至少四个单词");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
            }
        }
        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    private void getNewWord(Word wordupdata) throws ExecutionException, InterruptedException {
        myViewModel.getnewWord(wordupdata);
    }

    private void updateyes(Word word) {
        int x1 = word.getStudystart();
        int x2 = word.getStudyend();
        word.setStudystart(x1 + 1);
        word.setStudyend(x2 + 1);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String datenow = sd.format(new Date());
        word.setDate(datenow);
    }

    private void updateno(Word word) {
        int x1 = word.getStudystart();
        word.setStudystart(x1 + 1);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String datenow = sd.format(new Date());
        word.setDate(datenow);
    }

    private void buttonenabled() {
        buttona.setClickable(false);
        buttonb.setClickable(false);
        buttonc.setClickable(false);
        buttond.setClickable(false);
        buttonNext.setVisibility(View.VISIBLE);
    }

    private void replace() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.onItemSelected();
    }

    private String datediff(String datelast) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String datenow = sd.format(new Date());
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long diff;
        long day = 0;
        long hour = 0;
        String time;
        try {
            diff = sd.parse(datenow).getTime() - sd.parse(datelast).getTime();
            day = diff / nd;
            hour = diff % nd / nh;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (day >= 1) {
            time = String.valueOf(day) + "天前";
        } else {
            time = String.valueOf(hour) + "小时前";
        }
        return time;
    }


}