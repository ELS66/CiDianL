package com.example.cidianl;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class WordRepository {
    private LiveData<List<Word>>listLiveData;
    private WordDao wordDao;

    WordRepository(Context context) {
        WordDatabase wordDatabase = WordDatabase.getDatabase(context.getApplicationContext());
        wordDao = wordDatabase.getWordDao();
        //listLiveData = wordDao.getAllWords();
    }

    LiveData<List<Word>> getListLiveData(String dictionary) {
        return wordDao.getListWords(dictionary);
    }
    LiveData<List<Word>> findWordWithPattern(String pattern) {
        return wordDao.findWordWithPattern("%" + pattern  + "%");
    }
    LiveData<List<Word>> getlikeWord() {
        return wordDao.getlikeWord();
    }

    List<Word> getAllWordDictionary(String dictionary) throws ExecutionException,InterruptedException {
        return new getAllWordDictionaryAsyncTask(wordDao).execute(dictionary).get();
    }

    List<Word> getOtherWord (String studyWord) throws ExecutionException,InterruptedException {
        return new getOtherWordAsyncTask(wordDao).execute(studyWord).get();
    }

    List<Word> getPipeiWord () throws ExecutionException,InterruptedException{
        return new getPipeiWordAsyncTask(wordDao).execute().get();
    }
    List<Word> getAllWords () throws ExecutionException,InterruptedException {
        return  new getAllWordAsyncTask(wordDao).execute().get();
    }

    Word getStudyWord () throws ExecutionException,InterruptedException{
        return new getStudyWordAsyncTask(wordDao).execute().get();
    }

    Word getselectWord (String selectWord) throws ExecutionException,InterruptedException{
        return new getSelectWordAsyncTask(wordDao).execute(selectWord).get();
    }



    void insertWords (Word... words) {
        new InsertAsyncTask (wordDao).execute(words);
    }

    void deleteWords (Word... words) {
        new DeleteAsyncTask (wordDao).execute(words);
    }

    void getnewWord (Word... words) throws ExecutionException,InterruptedException{
        new getNewWordAsyncTask(wordDao).execute(words);
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
    static class getAllWordDictionaryAsyncTask extends AsyncTask<String, Void, List<Word>> {
        private WordDao wordDao;
        getAllWordDictionaryAsyncTask(WordDao wordDao){this.wordDao = wordDao;}

        @Override
        protected List<Word> doInBackground(String... strings) {
            return wordDao.getAllWordDictionary(strings[0]);
        }
    }
    static class getSelectWordAsyncTask extends AsyncTask<String, Void, Word> {
        private WordDao wordDao;

        getSelectWordAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }


        @Override
        protected Word doInBackground(String...string) {
            return wordDao.getSelectWord(string[0]);
        }
    }
    static class getOtherWordAsyncTask extends AsyncTask<String, Void, List<Word>> {
        private WordDao wordDao;

        getOtherWordAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }


        @Override
        protected List<Word> doInBackground(String...strings) {
            return wordDao.getOtherWord(strings[0]);
        }
    }
    static class getStudyWordAsyncTask extends AsyncTask<Void, Void, Word> {
        private WordDao wordDao;

        getStudyWordAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Word doInBackground(Void... voids) {
            return wordDao.getStudyWord();
        }
    }
    static class getNewWordAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDao wordDao;

        getNewWordAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.getNewWord(words);
            return null;
        }
    }

    static class getPipeiWordAsyncTask extends AsyncTask<Void,Void,List<Word>> {
        private WordDao wordDao;

        getPipeiWordAsyncTask(WordDao wordDao) {this.wordDao = wordDao;}


        @Override
        protected List<Word> doInBackground(Void... voids) {
            return wordDao.getPipeiWord();
        }
    }
    static class getAllWordAsyncTask extends AsyncTask<Void,Void,List<Word>> {
        private WordDao wordDao;

        getAllWordAsyncTask(WordDao wordDao) {this.wordDao = wordDao;}


        @Override
        protected List<Word> doInBackground(Void... voids) {
            return wordDao.getAllWords();
        }
    }
}