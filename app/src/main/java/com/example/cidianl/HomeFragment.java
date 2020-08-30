package com.example.cidianl;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
    private List<TabFragment> tabFragmentList = new ArrayList<>();
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private TabLayoutMediator mediator;
    private LiveData<List<String>> listLiveDataDic;
    DictionarySave dictionarySave;
    Context mContext;

    MainActivity mainActivity = (MainActivity) getActivity();

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.home_fragment, container, false);
        setHasOptionsMenu(true);
        mContext = getActivity().getApplicationContext();
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        dictionarySave = new DictionarySave(mContext,"DICTIONARY");
        myViewModel.getAllDictionary().setValue(dictionarySave.load("DICTIONARY"));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tabLayout = requireActivity().findViewById(R.id.tabLayout);
        for (int i = 0;i < myViewModel.getAllDictionary().getValue().size();i++) {
            tabLayout.addTab(tabLayout.newTab().setText(myViewModel.getAllDictionary().getValue().get(i)));
            tabFragmentList.add(TabFragment.newInstance(myViewModel.getAllDictionary().getValue().get(i)));
        }
        viewPager2 = requireActivity().findViewById(R.id.viewpage2);
        viewPager2.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        viewPager2.setAdapter(new FragmentStateAdapter(getChildFragmentManager(),getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return TabFragment.newInstance(myViewModel.getAllDictionary().getValue().get(position));
            }

            @Override
            public int getItemCount() {
                return myViewModel.getAllDictionary().getValue().size();
            }
        });
        viewPager2.registerOnPageChangeCallback(changeCallback);
        mediator = new TabLayoutMediator(tabLayout, viewPager2, true,new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                TextView tabView = new TextView(getActivity());
                tabView.setText(myViewModel.getAllDictionary().getValue().get(position));
                tabView.setTextSize(14);
                tab.setCustomView(tabView);
            }
        });
        mediator.attach();

        listLiveDataDic = myViewModel.getAllDictionary();
        listLiveDataDic.observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
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

    private ViewPager2.OnPageChangeCallback changeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            int tabCount = tabLayout.getTabCount();
            for (int i =0; i<tabCount;i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                TextView tabView = null;
                if (tab != null) {
                    tabView = (TextView) tab.getCustomView();
                }
                if (tab != null) {
                    if (tab.getPosition() == position) {
                        assert tabView != null;
                        myViewModel.whatDictionary = tabView.getText().toString();
                        tabView.setTextSize(20);
                    } else {
                        assert tabView != null;
                        tabView.setTextSize(14);
                    }
                }
            }
        }
    };

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
                    Word word = new Word(x2,x1,myViewModel.whatDictionary);
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
        MenuItem menuItemadd = menu.findItem(R.id.item_add);
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
        menuItemadd.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            EditText editTextadd;

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                NavController navController = Navigation.findNavController(requireActivity(),R.id.fragment);
                navController.navigate(R.id.action_item_home_to_addFragment);
                return false;
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

    @Override
    public void onDestroy() {
        mediator.detach();
        viewPager2.unregisterOnPageChangeCallback(changeCallback);
        super.onDestroy();
    }


}

