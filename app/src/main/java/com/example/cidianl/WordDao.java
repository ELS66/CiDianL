package com.example.cidianl;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
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
}
