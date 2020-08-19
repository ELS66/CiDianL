package com.example.cidianl;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class WordRepository {
    private LiveData<List<Word>>listLiveData;
    private WordDao wordDao;

    WordRepository(Context context) {
        WordDatabase wordDatabase = WordDatabase.getDatabase(context.getApplicationContext());
        wordDao = wordDatabase.getWordDao();
        listLiveData = wordDao.getAllWords();
    }

    LiveData<List<Word>> getListLiveData() {
        return listLiveData;
    }
    LiveData<List<Word>> findWordWithPattern(String pattern) {
        return wordDao.findWordWithPattern("%" + pattern  + "%");
    }

    Word getselectWord (String selectWord) {
        return wordDao.getSelectWord(selectWord);
    }

    void insertWords (Word... words) {
        new InsertAsyncTask (wordDao).execute(words);
    }

    void deleteWords (Word... words) {
        new DeleteAsyncTask (wordDao).execute(words);
    }



    static class InsertAsyncTask extends AsyncTask<Word,Void,Void> {
        private WordDao wordDao;

        InsertAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.insertWord(words);
            return null;
        }
    }
    static class DeleteAsyncTask extends AsyncTask<Word,Void,Void> {
        private WordDao wordDao;

        DeleteAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.deleteWords(words);
            return null;
        }
    }
}