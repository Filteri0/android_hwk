package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class ReportActivity extends AppCompatActivity {
    TextView tvReportTitle, tvTotalAmount;
    ListView lvReportList;
    ArrayList<String> reportListData = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // 透過 findViewById 連結畫面元件
        tvReportTitle = findViewById(R.id.tvReportTitle);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        lvReportList = findViewById(R.id.lvReportList);


        String type = getIntent().getStringExtra("type");
        reportListData = getIntent().getStringArrayListExtra("data");

        if (type != null) {
            if (type.equals("收入")) {
                tvReportTitle.setText("收入報表");
            } else if (type.equals("支出")) {
                tvReportTitle.setText("支出報表");
            }

            // 計算並顯示總金額
            double totalAmount = calculateTotalAmount(reportListData);
            tvTotalAmount.setText(String.valueOf(totalAmount));
        }

        // 建立 ArrayAdapter 來顯示報表列表
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reportListData);
        lvReportList.setAdapter(adapter);
    }

    // 計算總金額的方法
    private double calculateTotalAmount(ArrayList<String> data) {
        double total = 0.0;

        // 跑每一筆報表資料
        for (String entry : data) {
            String[] parts = entry.split("金額: "); // 從字串中提取金額（假設金額在字串的末尾）

            // 確保成功分割後可以解析金額
            if (parts.length > 1) {
                try {
                    total += Double.parseDouble(parts[1].split(" ")[0]); // 將字串轉換為 double 類型並累加
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return total;
    }
}