package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class ReportActivity extends AppCompatActivity {
    TextView tvReportTitle, tvTotalAmount; // 報表標題和總金額文字視圖
    ListView lvReportList; // 報表列表視圖
    ArrayList<String> reportListData = new ArrayList<>(); // 報表資料列表
    ArrayAdapter<String> adapter; // 列表適配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // 調用父類初始化方法
        setContentView(R.layout.activity_report); // 設置佈局

        // 透過 findViewById 連結畫面元件
        tvReportTitle = findViewById(R.id.tvReportTitle); // 綁定報表標題文字視圖
        tvTotalAmount = findViewById(R.id.tvTotalAmount); // 綁定總金額文字視圖
        lvReportList = findViewById(R.id.lvReportList); // 綁定報表列表視圖

        String type = getIntent().getStringExtra("type"); // 獲取報表類型（收入/支出）
        reportListData = getIntent().getStringArrayListExtra("data"); // 獲取報表資料列表

        if (type != null) {
            if (type.equals("收入")) {
                tvReportTitle.setText("收入報表"); // 設置收入報表標題
            } else if (type.equals("支出")) {
                tvReportTitle.setText("支出報表"); // 設置支出報表標題
            }

            // 計算並顯示總金額
            double totalAmount = calculateTotalAmount(reportListData); // 計算總金額
            tvTotalAmount.setText(String.valueOf(totalAmount)); // 設置總金額顯示
        }

        // 建立 ArrayAdapter 來顯示報表列表
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reportListData); // 創建列表適配器
        lvReportList.setAdapter(adapter); // 設置列表適配器
    }

    // 計算總金額的方法
    private double calculateTotalAmount(ArrayList<String> data) {
        double total = 0.0; // 初始化總金額

        // 跑每一筆報表資料
        for (String entry : data) {
            String[] parts = entry.split("金額: "); // 從字串中提取金額（假設金額在字串的末尾）

            // 確保成功分割後可以解析金額
            if (parts.length > 1) {
                try {
                    total += Double.parseDouble(parts[1].split(" ")[0]); // 將字串轉換為 double 類型並累加
                } catch (NumberFormatException e) {
                    e.printStackTrace(); // 捕捉並處理數字格式轉換異常
                }
            }
        }
        return total; // 返回總金額
    }
}