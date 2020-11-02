package com.example.cidianl.adapter;

import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cidianl.R;
import com.example.cidianl.bean.Word;

public class BlankAdapter extends ListAdapter<Word, BlankAdapter.BlankViewHolder> {

    public BlankAdapter() {
        super(new DiffUtil.ItemCallback<Word>() {
            @Override
            public boolean areItemsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
                return oldItem.getUid() == newItem.getUid();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
                return (oldItem.getEnglish().equals(newItem.getEnglish()) && oldItem.getChinese().equals(newItem.getChinese()));
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(@NonNull BlankViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.textViewNumber.setText(String.valueOf(holder.getAdapterPosition() + 1));
    }

    @NonNull
    @Override
    public BlankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_card,parent,false);
        final BlankViewHolder holder = new BlankViewHolder(itemView);
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
        return new BlankViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BlankViewHolder holder, int position) {
        final Word word = getItem(position);
        holder.textViewNumber.setText(String.valueOf(position + 1));
        holder.textViewEnglish.setText(word.getEnglish());
        holder.textViewChinese.setText(word.getChinese());
        holder.toggleButtonlove.setVisibility(View.INVISIBLE);
    }

    public static class BlankViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNumber;
        TextView textViewEnglish;
        TextView textViewChinese;
        ToggleButton toggleButtonlove;

        public BlankViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNumber = itemView.findViewById(R.id.textViewNumber);
            textViewEnglish = itemView.findViewById(R.id.textViewEnglish);
            textViewChinese = itemView.findViewById(R.id.textViewChinese);
            toggleButtonlove = itemView.findViewById(R.id.toggleButtonlove);
        }
    }
}
