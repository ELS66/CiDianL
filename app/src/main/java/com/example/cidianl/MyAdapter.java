package com.example.cidianl;

import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.ExecutionException;

public class MyAdapter extends ListAdapter<Word,MyAdapter.MyViewHolder> {

    WordRepository wordRepository = new WordRepository(MyApplication.getContext());
    boolean islike = false;

    protected MyAdapter() {
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
                try {
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/2/" + holder.textViewEnglish.getText().toString() + ".mp3";
                    Log.d("下载",path);
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Word word = getItem(position);
        holder.textViewNumber.setText(String.valueOf(position + 1));
        holder.textViewEnglish.setText(word.getEnglish());
        holder.textViewChinese.setText(word.getChinese());
        holder.toggleButtonlove.setChecked(word.isIslike());
    }

    static class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView textViewNumber,textViewEnglish,textViewChinese;
        ToggleButton toggleButtonlove;
        public MyViewHolder (@NonNull View itemView) {
            super(itemView);
            textViewNumber = itemView.findViewById(R.id.textViewNumber);
            textViewEnglish = itemView.findViewById(R.id.textViewEnglish);
            textViewChinese = itemView.findViewById(R.id.textViewChinese);
            toggleButtonlove = itemView.findViewById(R.id.toggleButtonlove);
        }
    }

}
