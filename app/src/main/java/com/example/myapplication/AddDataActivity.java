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

public class AddDataActivity extends AppCompatActivity implements View.OnClickListener {

    Switch swh;  // 用來選擇支入或支出的 Switch
    Button date, submit;
    EditText money;
    Spinner spr;
    String selectedDateTime = "";
    String selectedSpr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        // 初始化 UI 元素
        swh = findViewById(R.id.swType);
        date = findViewById(R.id.btnDate);
        submit = findViewById(R.id.btnSubmit);
        money = findViewById(R.id.etMoney);
        spr = findViewById(R.id.spinner);

        // 設置按鈕點擊事件
        date.setOnClickListener(this);
        submit.setOnClickListener(this);
        swh.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSubmit) {
            String amount = money.getText().toString();
            if (!amount.isEmpty() && !selectedDateTime.isEmpty()) {
                String type = swh.isChecked() ? "收入" : "支出";

                // 獲取 Spinner 選中的值
                selectedSpr = spr.getSelectedItem().toString();

                // 回傳資料
                Intent resultIntent = new Intent();
                resultIntent.putExtra("money", amount);
                resultIntent.putExtra("dateTime", selectedDateTime);
                resultIntent.putExtra("class", selectedSpr); // 回傳 Spinner 選擇的類別
                resultIntent.putExtra("type", type);         // 回傳收入或支出類型
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "請填寫金額並選擇日期", Toast.LENGTH_SHORT).show();
            }
        }

        if (view.getId() == R.id.btnDate) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this, (view1, selectedYear, selectedMonth, selectedDay) -> {

                selectedDateTime = selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay;
                date.setText(selectedDateTime);
            }, year, month, day);

            datePickerDialog.show();
        }

        if (view.getId() == R.id.swType) {
            boolean isIncome = swh.isChecked(); // 是否為收入
            ArrayAdapter<String> adapter;

            if (isIncome)
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.add_input_spinner));
            else
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.add_output_spinner));

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spr.setAdapter(adapter);
        }
    }
}
