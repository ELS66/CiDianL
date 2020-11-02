package com.example.cidianl.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cidianl.R;
import com.example.cidianl.bean.Word;
import com.example.cidianl.model.MyViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ShareFragment extends Fragment {

    Button button1,button2;
    TextView textView;
    Uri uri;
    List<Word> allWord;
    MyViewModel myViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_share, container, false);
        myViewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        button1 = rootView.findViewById(R.id.button8);
        button2 = rootView.findViewById(R.id.button7);
        textView = rootView.findViewById(R.id.textView4);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,1);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    allWord = myViewModel.getAllWordDictionary("我的");
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                StringBuilder builder = new StringBuilder();
                builder.append("id,英语,中文\r\n");
                for (Word u:allWord){
                    builder.append(u.getEnglish()).append(",").append(u.getChinese()).append("\r\n");
                }

                try {
                    String data = builder.toString();
                    Log.d("下载",data);
                    String fileName = "Word.csv";
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/2";
                    if (!new File(path).exists()) {
                        new File(path,fileName);
                    }
                    File file = new File(path,fileName);
                    OutputStream out = new FileOutputStream(file);
                    out.write(data.getBytes());
                    out.close();
                    Toast.makeText(requireContext(),"yes",Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(),"no",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
            uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            Toast.makeText(requireActivity(), uri.toString(),Toast.LENGTH_SHORT).show();
            Log.d("下载",uri.toString());
            textView.setText(uri.toString());
            File file =null;
            File cache = null;
            if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
                file = new File(uri.getPath());
            } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                ContentResolver contentResolver = requireContext().getContentResolver();
                Cursor cursor = contentResolver.query(uri, null, null, null, null);
                if (cursor.moveToFirst()) {
                    String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    try {
                        InputStream is = contentResolver.openInputStream(uri);
                        cache = new File(requireContext().getExternalCacheDir().getAbsolutePath(), displayName);
                        FileOutputStream fos = new FileOutputStream(cache);
                        FileUtils.copy(is, fos);
                        file = cache;
                        fos.close();
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                FileInputStream fileInputStream = new FileInputStream(cache);
                Scanner scanner = new Scanner(fileInputStream,"UTF-8");
                scanner.nextLine();
                while (scanner.hasNextLine()) {
                    String soureString = scanner.nextLine();
                    Log.d("下载",soureString);
                    Pattern pattern = Pattern.compile(",");
                    Matcher matcher = pattern.matcher(soureString);
                    String[] result = pattern.split(soureString);
                    Word newWord = new Word(result[0],result[1],"我的");
                    myViewModel.insertWords(newWord);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}