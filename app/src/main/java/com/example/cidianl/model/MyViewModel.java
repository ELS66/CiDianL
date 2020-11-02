package com.example.cidianl.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.cidianl.bean.Word;
import com.example.cidianl.db.WordRepository;
import com.example.cidianl.utils.DictionarySave;

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

    public int x1 = 0;
    public int b1 = 0;
    public int i1 = 0;
    public int i2 = 0;
    public int i3 = 0;
    public int numno = 0;

    List<Word> getAllWord() throws ExecutionException, InterruptedException {
        return wordRepository.getAllWords();
    }

    public Date start = null;

    Word getselectWord(String selectWord) throws ExecutionException, InterruptedException {
        return wordRepository.getselectWord(selectWord);
    }

    public String whatDictionary = "我的";

    public LiveData<List<Word>> getListLiveData(String dictionary) {
        return wordRepository.getListLiveData(dictionary);
    }

    public LiveData<List<Word>> findWordWithPattern(String pattern) {
        return wordRepository.findWordWithPattern(pattern);
    }

    public LiveData<List<Word>> getlikeWord() {
        return wordRepository.getlikeWord();
    }

    public List<Word> getAllWordDictionary(String dictionary) throws ExecutionException, InterruptedException {
        return wordRepository.getAllWordDictionary(dictionary);
    }

    public List<Word> getOtherWords(String studyWord) throws ExecutionException, InterruptedException {
        return wordRepository.getOtherWord(studyWord);
    }

    public List<Word> getPipeiWord() throws ExecutionException, InterruptedException {
        return wordRepository.getPipeiWord();
    }

    public Word getStudyWord() throws ExecutionException, InterruptedException {
        return wordRepository.getStudyWord();
    }

    public void insertWords(Word... words) {
        wordRepository.insertWords(words);
    }

    public void deleteWords(Word... words) {
        wordRepository.deleteWords(words);
    }

    public void getnewWord(Word word) throws ExecutionException, InterruptedException {
        wordRepository.getnewWord(word);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        onSave();
    }

    public void onSave() {
        dictionarySave.save("DICTIONARY",this.getAllDictionary().getValue());
    }
}
