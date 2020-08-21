package com.example.cidianl;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editEnglish,editChinese;
    private FloatingActionButton fla;
    private List<Word> allwords;
    private LiveData<List<Word>> findWords;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    MyViewModel myViewModel;
    SearchView searchView;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this,R.id.fragment);
        AppBarConfiguration configuration = new AppBarConfiguration.Builder(bottomNavigationView.getMenu()).build();
        NavigationUI.setupActionBarWithNavController(this,navController,configuration);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);

    }

    public void onItemSelected() {
        StudyFragment newFragment = new StudyFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment,newFragment);
        transaction.commit();
    }



    public void wordPlayer(String path) {
        try {
            mediaPlayer.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath()+"/2"+path+".mp3");
            mediaPlayer.pause();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this,R.id.fragment);
        return navController.navigateUp();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        android.widget.SearchView searchView = (SearchView)menu.findItem(R.id.item_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String pattern = newText.trim();
                findWords.removeObservers(MainActivity.this);
                findWords = myViewModel.findWordWithPattern(pattern);
                findWords.removeObservers(MainActivity.this);
                findWords.observe(MainActivity.this, new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
                        int temp  = myAdapter.getItemCount();
                        allwords = words;
                        if (temp != words.size()) {
                            myAdapter.submitList(words);
                        }
                    }
                });
                return true;
            }



        });
        return super.onCreateOptionsMenu(menu);
    }


    private void showeditTextDialog() {
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.edit_dialog,null);
        final AlertDialog editTextDialog = new AlertDialog.Builder(this)
                .setTitle("添加单词")
                .setView(dialogView)
                .create();
        editEnglish = dialogView.findViewById(R.id.editEnglish);
        editChinese = dialogView.findViewById(R.id.editChinese);
        editTextDialog.show();
        editEnglish.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                editChinese.requestFocus();
                return true;
            }
        });
        editChinese.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String x1 = editChinese.getText().toString();
                String x2 = editEnglish.getText().toString();
                if (x1.length() == 0  || x2.length() == 0){
                    Toast.makeText(MyApplication.getContext(),"请输入单词",Toast.LENGTH_SHORT).show();
                }else {
                    Word word = new Word(x2,x1);
                    myViewModel.insertWords(word);
                    Toast.makeText(MyApplication.getContext(),"添加成功",Toast.LENGTH_SHORT).show();
                    downloadFile(x2);
                    editTextDialog.dismiss();
                }
                return true;
            }
        });
    }


    }**/


}