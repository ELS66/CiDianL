package com.example.cidianl;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class AddFragment extends Fragment {

    MyViewModel myViewModel;
    List<String> diclist;
    List<Word> dicWord;
    CardView cardView;

    public AddFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_add, container, false);
        myViewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        diclist = myViewModel.getAllDictionary().getValue();
        RecyclerView recyclerView = rootview.findViewById(R.id.recycleViewDic);
        DicAdapter dicAdapter;
        dicAdapter = new DicAdapter();
        dicAdapter.submitList(diclist);
        recyclerView.setAdapter(dicAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        cardView = rootview.findViewById(R.id.cardViewAdd);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialogView = LayoutInflater.from(requireActivity()).inflate(R.layout.add_dialog,null);
                EditText editTextadd = dialogView.findViewById(R.id.edit_add);
                 final AlertDialog addDialog = new AlertDialog.Builder(requireActivity())
                 .setTitle("添加词典")
                 .setView(dialogView)
                 .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String dictionary_add = editTextadd.getText().toString();
                    myViewModel.getAllDictionary().getValue().add(dictionary_add);
                    myViewModel.dictionarySave.save("DICTIONARY",myViewModel.getAllDictionary().getValue());
                }
                 })
                 .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
                 })
                 .create();
                 addDialog.show();


            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final String deleteDic = diclist.get(viewHolder.getAdapterPosition());
                AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
                dialog.setTitle("警告");
                dialog.setMessage("你确定要删除吗？");
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dicAdapter.notifyDataSetChanged();
                    }
                });
               dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       try {
                           List<Word> deleteWords = myViewModel.getAllWordDictionary(deleteDic);
                           WordRepository wordRepository = new WordRepository(requireContext());
                           for (int i = 0; i < deleteWords.size();i++) {
                               wordRepository.deleteWords(deleteWords.get(i));
                           }
                       } catch (ExecutionException | InterruptedException e) {
                           e.printStackTrace();
                       }
                       List<String> newDic = myViewModel.getAllDictionary().getValue();
                       newDic.remove(viewHolder.getAdapterPosition());
                       myViewModel.getAllDictionary().setValue(newDic);
                       myViewModel.onSave();
                       dicAdapter.notifyDataSetChanged();
                   }
               });
               dialog.show();
            }
        }).attachToRecyclerView(recyclerView);

        return rootview;
    }

    public class DicAdapter extends ListAdapter<String,DicAdapter.DicViewHolder> {
        protected DicAdapter() {
            super(new DiffUtil.ItemCallback<String>() {
                @Override
                public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                    return oldItem.equals(newItem);
                }
            });
        }

        @NonNull
        @Override
        public DicAdapter.DicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View itemview = layoutInflater.inflate(R.layout.item_dic,parent,false);
            final DicViewHolder holder = new DicViewHolder(itemview);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        dicWord = myViewModel.getAllWordDictionary(holder.textViewName.toString());
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    StringBuilder builder = new StringBuilder();
                    for (Word u:dicWord) {
                        builder.append(u.getEnglish()).append(",").append(u.getChinese()).append("\r\n");
                    }
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                    startActivityForResult(intent,1);
                }
            });
            return new DicViewHolder(itemview);
        }

        @Override
        public void onBindViewHolder(@NonNull DicAdapter.DicViewHolder holder, int position) {
            final String dic = diclist.get(position);
            try {
                int size = myViewModel.getAllWordDictionary(dic).size();
                holder.textViewName.setText(dic);
                holder.textViewNumber.setText(String.valueOf(size));
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        }

        class DicViewHolder extends RecyclerView.ViewHolder {

            TextView textViewName,textViewNumber;

            public DicViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewName = itemView.findViewById(R.id.textViewdic);
                textViewNumber = itemView.findViewById(R.id.textViewdicnum);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            Toast.makeText(requireActivity(), uri.toString(),Toast.LENGTH_SHORT).show();
            Log.d("下载",uri.toString());
        }
    }
}