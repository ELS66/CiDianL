package com.example.cidianl;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WordDao {
    @Insert
    void insertWord(Word... words);

    @Delete
    void deleteWords(Word... words);

    @Query("SELECT * FROM WORD ORDER BY UID DESC")
    LiveData<List<Word>> getAllWords();

    @Query("SELECT * FROM Word WHERE english  LIKE :pattern  ORDER BY UID DESC")
    LiveData<List<Word>> findWordWithPattern(String pattern);

    @Query("SELECT * FROM word WHERE ENGLISH = :selectWord")
    Word getSelectWord(String selectWord);

    @Query("SELECT * FROM Word WHERE isstudy = 1 ORDER BY studyend ASC , studystart DESC LIMIT 1")
    Word getStudyWord();

    @Query(("SELECT * FROM word WHERE english != :studyword ORDER BY RANDOM() LIMIT 3"))
    List<Word> getOtherWord(String studyword);

    @Update
    void getNewWord(Word...words);
}
