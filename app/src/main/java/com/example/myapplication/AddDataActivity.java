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
 * 新增記帳資料的活動
 * 提供使用者輸入收入/支出金額、日期和類別的介面
 */
public class AddDataActivity extends AppCompatActivity implements View.OnClickListener {

    // UI 元件宣告
    Switch swh;
    Button date, submit;
    EditText money;
    Spinner spr;

    // 儲存選擇的日期時間和類別
    String selectedDateTime = "";
    String selectedSpr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        swh = findViewById(R.id.swType);
        date = findViewById(R.id.btnDate);
        submit = findViewById(R.id.btnSubmit);
        money = findViewById(R.id.etMoney);
        spr = findViewById(R.id.spinner);

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
                selectedSpr = spr.getSelectedItem().toString();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("money", amount);
                resultIntent.putExtra("dateTime", selectedDateTime);
                resultIntent.putExtra("class", selectedSpr);
                resultIntent.putExtra("type", type);

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

            // 創建日期選擇對話框
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this, (view1, selectedYear, selectedMonth, selectedDay) -> {
                selectedDateTime = selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay;
                date.setText(selectedDateTime);
            }, year, month, day);
            datePickerDialog.show();
        }

        // 切換收入/支出開關時的處理
        if (view.getId() == R.id.swType) {
            boolean isIncome = swh.isChecked();
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