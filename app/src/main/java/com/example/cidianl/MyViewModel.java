package com.example.cidianl;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MyViewModel extends AndroidViewModel {

    private WordRepository wordRepository;
    

    public MyViewModel(@NonNull Application application) {
        super(application);
        wordRepository = new WordRepository(application);
    }

    LiveData<List<Word>> getListLiveData() {
        return wordRepository.getListLiveData();
    }
    LiveData<List<Word>> findWordWithPattern(String pattern) {
        return wordRepository.findWordWithPattern(pattern);
    }
    Word getselectWord (String selectWord) {
        return wordRepository.getselectWord(selectWord);
    }

    void insertWords(Word... words) {
        wordRepository.insertWords(words);
    }
    void deleteWords(Word... words) {
        wordRepository.deleteWords(words);
    }
}
