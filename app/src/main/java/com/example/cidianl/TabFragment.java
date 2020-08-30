package com.example.cidianl;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TabFragment extends Fragment {

    private List<Word> allwords;
    private List<Word> listWord;
    RecyclerView recyclerView;

    public static TabFragment newInstance (String label){
        Bundle args = new Bundle();
        args.putString("label",label);
        TabFragment fragment = new TabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview =  inflater.inflate(R.layout.tab_layout,container,false);
        assert getArguments() != null;
        String label = getArguments().getString("label");
        MyViewModel myViewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);

        recyclerView = rootview.findViewById(R.id.tabRecyclerView);
        MyAdapter myAdapter;
        myAdapter = new MyAdapter();
        try {
            listWord = myViewModel.getAllWordDictionary(label);
            myAdapter.submitList(listWord);
            Log.d("下载",label);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
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

        LiveData<List<Word>> findWords;
        findWords = myViewModel.getListLiveData(label);
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

        return rootview;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
