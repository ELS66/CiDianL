package com.example.cidianl.adapter;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cidianl.R;
import com.example.cidianl.bean.Word;
import com.example.cidianl.config.MyApplication;
import com.example.cidianl.db.WordRepository;
import com.example.cidianl.utils.DownloadFile;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class MyAdapter extends ListAdapter<Word,MyAdapter.MyViewHolder> {

    WordRepository wordRepository = new WordRepository(MyApplication.getContext());
    boolean islike = false;

    public MyAdapter() {
        super(new DiffUtil.ItemCallback<Word>() {
            @Override
            public boolean areItemsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
                return oldItem.getUid() == newItem.getUid();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
                return (oldItem.getEnglish().equals(newItem.getEnglish()) && oldItem.getChinese().equals(newItem.getChinese()) && oldItem.isIslike() == newItem.isIslike());
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.textViewNumber.setText(String.valueOf(holder.getAdapterPosition() + 1));
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_card,parent,false);
        final MyViewHolder holder =new MyViewHolder(itemView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = MyApplication.getContext().getExternalFilesDir("mp3")+"/"+holder.textViewEnglish.getText().toString() + ".mp3";
                if (fileIsExists(path)) {
                    try {
                        holder.imageButtonDownload.setVisibility(View.INVISIBLE);
                        MediaPlayer mediaPlayer = new MediaPlayer();
                        Log.d("下载",path);
                        mediaPlayer.setDataSource(path);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    holder.imageButtonDownload.setVisibility(View.VISIBLE);
                }


            }
        });
        holder.imageButtonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadFile downloadFile = new DownloadFile();
                downloadFile.download(holder.textViewEnglish.getText().toString());
            }
        });

        holder.toggleButtonlove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Word word = null;
                String english = holder.textViewEnglish.getText().toString();
                try {
                    word = wordRepository.getselectWord(english);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                if (buttonView.isChecked()){

                    if (word != null) {
                        word.setIslike(true);
                    }
                    try {
                        wordRepository.getnewWord(word);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {

                    if (word != null) {
                        word.setIslike(false);
                    }
                    try {
                        wordRepository.getnewWord(word);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        return new MyViewHolder(itemView);
    }

    private boolean fileIsExists(String filePath) {
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Word word = getItem(position);
        holder.textViewNumber.setText(String.valueOf(position + 1));
        holder.textViewEnglish.setText(word.getEnglish());
        holder.textViewChinese.setText(word.getChinese());
        holder.toggleButtonlove.setChecked(word.isIslike());
        holder.imageButtonDownload.setVisibility(View.INVISIBLE);

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNumber;
        TextView textViewEnglish;
        TextView textViewChinese;
        ToggleButton toggleButtonlove;
        ImageButton imageButtonDownload;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNumber = itemView.findViewById(R.id.textViewNumber);
            textViewEnglish = itemView.findViewById(R.id.textViewEnglish);
            textViewChinese = itemView.findViewById(R.id.textViewChinese);
            toggleButtonlove = itemView.findViewById(R.id.toggleButtonlove);
            imageButtonDownload = itemView.findViewById(R.id.imageButtonDownload);
        }
    }

}
