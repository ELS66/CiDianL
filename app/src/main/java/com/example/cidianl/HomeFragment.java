package com.example.cidianl;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    MyAdapter myAdapter;
    private MyViewModel myViewModel;
    private EditText editEnglish,editChinese;
    private FloatingActionButton fla;
    private List<Word> allwords;
    private LiveData<List<Word>> findWords;
    private RecyclerView recyclerView;
    MainActivity mainActivity = (MainActivity) getActivity();

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.home_fragment, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        recyclerView = requireActivity().findViewById(R.id.recyclerView1);
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
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
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final Word wordDelete = allwords.get(viewHolder.getAdapterPosition());
                String fileName = wordDelete.getEnglish();
                myViewModel.deleteWords(wordDelete);
                AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
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
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/2/"+fileName+".mp3";
                        if (deleteFile(path)){
                            Toast.makeText(requireActivity(),"删除成功",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireActivity(),"删除失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.show();
            }
        }).attachToRecyclerView(recyclerView);

        findWords = myViewModel.getListLiveData();
        findWords.observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
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

        fla = requireActivity().findViewById(R.id.floatingActionButton1);
        fla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showeditTextDialog();
            }
        });
    }

    private void showeditTextDialog() {
        final View dialogView = LayoutInflater.from(requireActivity()).inflate(R.layout.edit_dialog,null);
        final AlertDialog editTextDialog = new AlertDialog.Builder(requireActivity())
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
                    Toast.makeText(requireContext(),"请输入单词",Toast.LENGTH_SHORT).show();
                }else {
                    Word word = new Word(x2,x1);
                    myViewModel.insertWords(word);
                    Toast.makeText(requireContext(),"添加成功",Toast.LENGTH_SHORT).show();
                    downloadFile(x2);
                    editTextDialog.dismiss();
                }
                return true;
            }
        });
    }



    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar,menu);
        android.widget.SearchView searchView = (SearchView)menu.findItem(R.id.item_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String pattern = newText.trim();
                findWords.removeObservers(getViewLifecycleOwner());
                findWords = myViewModel.findWordWithPattern(pattern);
                findWords.removeObservers(getViewLifecycleOwner());
                findWords.observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
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
       super.onCreateOptionsMenu(menu,inflater);
    }

    public void downloadFile(String x1) {
        final String url = "http://dict.youdao.com/dictvoice?audio=" + x1 ;
        final long startTime = System.currentTimeMillis();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Connection", "close")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;

                String savePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/2";
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath, x1+ ".mp3");
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                    }
                    fos.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

}

