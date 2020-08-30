package com.example.cidianl;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyViewModel extends AndroidViewModel {

    private WordRepository wordRepository;
    //public List<String> allDictionary = new ArrayList<>();
    public DictionarySave dictionarySave;

    

    public MyViewModel(@NonNull Application application,SavedStateHandle handle) {
        super(application);
        wordRepository = new WordRepository(application);
        dictionarySave = new DictionarySave(getApplication(),"DICTIONARY");
        if (dictionarySave.load("DICTIONARY") == null || dictionarySave.load("DICTIONARY").size() <= 0) {
            //this.getAllDictionary().setValue(dictionarySave.load("DICTIONARY")); ;
        } else {
            this.getAllDictionary().setValue(dictionarySave.load("DICTIONARY"));
        }
    }

    private MutableLiveData<Integer> yesnumber;

    public MutableLiveData<Integer> getYesnumber() {
        if (yesnumber == null) {
            yesnumber = new MutableLiveData<>();
            yesnumber.setValue(0);
        }
        return yesnumber;
    }

    private MutableLiveData<List<String>> allDictionary;

    public MutableLiveData<List<String>> getAllDictionary() {
        if (allDictionary == null) {
            allDictionary = new MutableLiveData<>();
            List<String> stringList = new ArrayList<>();
            stringList.add("我的");
            allDictionary.setValue(stringList);
        }
        return allDictionary;
    }

    public void addYesNumber(int n) {
        yesnumber.setValue(yesnumber.getValue() + 1);
    }

    LiveData<List<Word>> getListLiveData(String dictionary) {
        return wordRepository.getListLiveData(dictionary);
    }
    LiveData<List<Word>> findWordWithPattern(String pattern) {
        return wordRepository.findWordWithPattern(pattern);
    }
    LiveData<List<Word>> getlikeWord() {
        return wordRepository.getlikeWord();
    }
    List<Word> getAllWordDictionary (String dictionary) throws ExecutionException,InterruptedException {
        return wordRepository.getAllWordDictionary(dictionary);
    }
    List<Word> getOtherWords(String studyWord) throws ExecutionException, InterruptedException {
        return wordRepository.getOtherWord(studyWord);
    }
    List<Word> getPipeiWord() throws ExecutionException,InterruptedException {
        return wordRepository.getPipeiWord();
    }
    List<Word> getAllWord() throws ExecutionException, InterruptedException {
        return wordRepository.getAllWords();
    }
    Word getStudyWord() throws ExecutionException, InterruptedException {
        return wordRepository.getStudyWord();
    }
    Word getselectWord (String selectWord) throws ExecutionException, InterruptedException {
        return wordRepository.getselectWord(selectWord);
    }

    void insertWords(Word... words) {
        wordRepository.insertWords(words);
    }
    void deleteWords(Word... words) {
        wordRepository.deleteWords(words);
    }
    void getnewWord(Word word) throws ExecutionException, InterruptedException {
        wordRepository.getnewWord(word);
    }
    int x1 = 0;
    int b1 = 0;
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    int numno = 0;
    Date start = null;
    String whatDictionary = "我的";

    @Override
    protected void onCleared() {
        super.onCleared();
        dictionarySave.save("DICTIONARY",this.getAllDictionary().getValue());
    }
}
