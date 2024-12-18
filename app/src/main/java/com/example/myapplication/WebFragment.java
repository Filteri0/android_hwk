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
    private String[] names = {"台灣銀行", // 銀行名稱陣列
            "中華郵政全球資訊網",
            "兆豐銀行",
            "國泰世華銀行",
            "台北富邦銀行"};
    private String[] urls = {"https://www.bot.com.tw/tw/personal-banking", // 對應的銀行網址陣列
            "https://www.post.gov.tw/post/internet/Group/default.jsp",
            "https://www.megabank.com.tw/personal",
            "https://cathaybk.com.tw/cathaybk/",
            "https://www.fubon.com/banking/personal/"};
    private ListView listView; // 顯示銀行列表的清單視圖
    private ArrayAdapter<String> adapter; // 列表適配器

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_web, container, false); // 載入佈局

        listView = rootView.findViewById(R.id.lvReportList); // 綁定列表視圖

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, names); // 創建適配器
        listView.setAdapter(adapter); // 設置適配器

        listView.setOnItemClickListener((parent, view, position, id) -> { // 設置點擊事件
            String url = urls[position]; // 獲取點擊項目對應的網址
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url)); // 創建瀏覽器意圖
            startActivity(intent); // 啟動瀏覽器開啟網址
        });

        return rootView;
    }
}