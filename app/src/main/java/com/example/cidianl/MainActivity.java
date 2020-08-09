package com.example.cidianl;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public void onAnimationFinished(@NonNull RecyclerView.ViewHolder viewHolder) {
                super.onAnimationFinished(viewHolder);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager != null) {
                    int firstPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                    for (int i = firstPosition ; i<lastPosition;i++) {
                        MyAdapter.MyViewHolder holder = (MyAdapter.MyViewHolder) recyclerView.findViewHolderForLayoutPosition(i);
                        if (holder != null){
                            holder.textViewNumber.setText(String.valueOf(i+1));
                        }
                    }
                }
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.START | ItemTouchHelper.END) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                final Word wordDelete = allwords.get(viewHolder.getAdapterPosition());
                myViewModel.deleteWords(wordDelete);
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("警告");
                dialog.setMessage("你确定要删除吗？");
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myViewModel.insertWords(wordDelete);
                            }
                        });
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
            }
        }).attachToRecyclerView(recyclerView);
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        findWords = myViewModel.getListLiveData();
        findWords.observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                int temp = myAdapter.getItemCount();
                allwords = words;
                if (temp != words.size()){
                    if (temp < words.size()){
                        recyclerView.smoothScrollBy(0,-200);
                    }
                    myAdapter.submitList(words);
                }
            }
        });
        //toolbar = findViewById(R.id.toolbar);
        //toolbar.inflateMenu(R.menu.toolbar);

        fla = findViewById(R.id.floatingActionButton);
        fla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showeditTextDialog();
            }
        });
    }

    @Override
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
                    editTextDialog.dismiss();
                }
                return true;
            }
        });
    }
}