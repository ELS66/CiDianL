package com.example.cidianl.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cidianl.R;
import com.example.cidianl.bean.Word;
import com.example.cidianl.utils.Animat;

public class StudyAdatpter extends ListAdapter<Word, StudyAdatpter.StudyViewHolder> {


    public StudyAdatpter() {
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

    @NonNull
    @Override
    public StudyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_word,parent,false);
        final StudyViewHolder holder = new StudyViewHolder(itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.textViewChinese.getVisibility() == View.INVISIBLE) {
                    Animat.rotation(itemView,holder.textViewEnglish,holder.textViewChinese,300);
                } else {
                    Animat.rotation(itemView,holder.textViewChinese,holder.textViewEnglish,300);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudyViewHolder holder, int position) {
        final Word word = getItem(position);
        holder.textViewEnglish.setText(word.getEnglish());
        holder.textViewChinese.setText(word.getChinese());
    }

    static class StudyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewEnglish,textViewChinese;
        Button button;
        public StudyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEnglish = itemView.findViewById(R.id.textViewEnglish);
            textViewChinese = itemView.findViewById(R.id.textViewChinese);
        }
    }
}
