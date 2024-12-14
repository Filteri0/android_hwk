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

        tvReportTitle = findViewById(R.id.tvReportTitle);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        lvReportList = findViewById(R.id.lvReportList);

        // 接收傳遞過來的資料
        String type = getIntent().getStringExtra("type");
        reportListData = getIntent().getStringArrayListExtra("data");

        if (type != null) {
            if (type.equals("收入")) {
                tvReportTitle.setText("收入報表");
            } else if (type.equals("支出")) {
                tvReportTitle.setText("支出報表");
            }

            // 計算總金額
            double totalAmount = calculateTotalAmount(reportListData);
            tvTotalAmount.setText(String.valueOf(totalAmount));
        }

        // 設置 ListView 顯示資料
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reportListData);
        lvReportList.setAdapter(adapter);
    }

    private double calculateTotalAmount(ArrayList<String> data) {
        double total = 0.0;
        for (String entry : data) {
            // 從字符串中提取金額並進行累加（假設金額在字符串的末尾）
            String[] parts = entry.split("金額: ");
            if (parts.length > 1) {
                try {
                    total += Double.parseDouble(parts[1].split(" ")[0]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return total;
    }
    private void onClick (View view){
        finish();
    }
}


