package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.app.DatePickerDialog;

import java.util.Calendar;

/**
 * 新增記帳資料的活動 - 提供使用者輸入收支資訊的介面
 */
public class AddDataActivity extends AppCompatActivity implements View.OnClickListener {

    Switch swh; // 收入/支出切換開關
    Button date, submit; // 日期選擇按鈕和提交按鈕
    EditText money; // 金額輸入框
    Spinner spr; // 類別下拉選單

    String selectedDateTime = ""; // 儲存選擇的日期
    String selectedSpr = ""; // 儲存選擇的類別

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // 調用父類初始化方法
        setContentView(R.layout.activity_add_data); // 設置佈局

        swh = findViewById(R.id.swType); // 初始化開關元件
        date = findViewById(R.id.btnDate); // 初始化日期按鈕
        submit = findViewById(R.id.btnSubmit); // 初始化提交按鈕
        money = findViewById(R.id.etMoney); // 初始化金額輸入框
        spr = findViewById(R.id.spinner); // 初始化下拉選單

        date.setOnClickListener(this); // 為日期按鈕設置點擊監聽器
        submit.setOnClickListener(this); // 為提交按鈕設置點擊監聽器
        swh.setOnClickListener(this); // 為開關設置點擊監聽器
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSubmit) { // 如果點擊提交按鈕
            String amount = money.getText().toString(); // 獲取輸入的金額

            if (!amount.isEmpty() && !selectedDateTime.isEmpty()) { // 檢查金額和日期是否已填寫
                String type = swh.isChecked() ? "收入" : "支出"; // 根據開關確定收入或支出
                selectedSpr = spr.getSelectedItem().toString(); // 獲取選擇的類別

                Intent resultIntent = new Intent(); // 創建返回結果的意圖
                resultIntent.putExtra("money", amount); // 添加金額數據
                resultIntent.putExtra("dateTime", selectedDateTime); // 添加日期數據
                resultIntent.putExtra("class", selectedSpr); // 添加類別數據
                resultIntent.putExtra("type", type); // 添加類型數據

                setResult(RESULT_OK, resultIntent); // 設置結果
                finish(); // 關閉當前活動
            } else {
                Toast.makeText(this, "請填寫金額並選擇日期", Toast.LENGTH_SHORT).show(); // 顯示提示訊息
            }
        }

        if (view.getId() == R.id.btnDate) { // 如果點擊日期按鈕
            final Calendar calendar = Calendar.getInstance(); // 獲取當前日曆實例
            int year = calendar.get(Calendar.YEAR); // 獲取當前年份
            int month = calendar.get(Calendar.MONTH); // 獲取當前月份
            int day = calendar.get(Calendar.DAY_OF_MONTH); // 獲取當前日期

            // 創建日期選擇對話框
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this, (view1, selectedYear, selectedMonth, selectedDay) -> {
                selectedDateTime = selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay; // 格式化選擇的日期
                date.setText(selectedDateTime); // 設置按鈕顯示選擇的日期
            }, year, month, day);
            datePickerDialog.show(); // 顯示日期選擇對話框
        }

        if (view.getId() == R.id.swType) { // 如果點擊收入/支出開關
            boolean isIncome = swh.isChecked(); // 判斷是否為收入
            ArrayAdapter<String> adapter; // 宣告適配器

            if (isIncome)
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.add_input_spinner)); // 收入類別適配器
            else
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.add_output_spinner)); // 支出類別適配器

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 設置下拉視圖樣式
            spr.setAdapter(adapter); // 為下拉選單設置適配器
        }
    }
}