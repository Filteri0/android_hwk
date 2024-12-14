package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class WebFragment extends Fragment {

    // 範例資料
    private String[] names = {"台灣銀行"};
    private String[] urls = {"https://www.bot.com.tw/tw/personal-banking"};

    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // 加載佈局
        View rootView = inflater.inflate(R.layout.fragment_web, container, false);

        // 初始化 ListView
        listView = rootView.findViewById(R.id.lvReportList);

        // 使用 ArrayAdapter 顯示 names 陣列中的項目
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, names);
        listView.setAdapter(adapter);

        // 設置項目點擊事件
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // 根據選擇的項目位置，取得對應的網址
            String url = urls[position];

            // 使用 Intent 打開該網址
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });

        return rootView;
    }
}
