package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener {
    Button button;
    ListView lv;
    ArrayAdapter<String> adapter;
    ArrayList<String> dataList;

    private TextView tvTotalInput, tvTotalOutput, tvTotalBalance;
    private SharedViewModel sharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        button = rootView.findViewById(R.id.button);
        lv = rootView.findViewById(R.id.listView);
        tvTotalInput = rootView.findViewById(R.id.tvTotalInput);
        tvTotalOutput = rootView.findViewById(R.id.tvTotalOutput);
        tvTotalBalance = rootView.findViewById(R.id.tvTotal);

        button.setOnClickListener(this);
        tvTotalInput.setOnClickListener(this);
        tvTotalOutput.setOnClickListener(this);

        // 初始化 ViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        dataList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        lv.setAdapter(adapter);

        // 設置觀察者更新 UI
        sharedViewModel.getTotalInput().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double totalInput) {
                tvTotalInput.setText("總收入  ▶\n" + totalInput);
            }
        });

        sharedViewModel.getTotalOutput().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double totalOutput) {
                tvTotalOutput.setText("總支出  ▶\n" + totalOutput);
            }
        });

        sharedViewModel.getBalance().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double balance) {
                tvTotalBalance.setText("結餘: " + balance);
            }
        });

        sharedViewModel.getDataList().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> newDataList) {
                dataList.clear();  // 清空舊資料
                dataList.addAll(newDataList);  // 添加新資料
                adapter.notifyDataSetChanged();  // 更新 ListView
            }
        });

        return rootView;
    }

    private ArrayList<String> filterDataByType(String type) {
        ArrayList<String> filteredData = new ArrayList<>();
        for (String data : dataList) {
            if (data.contains(type)) {
                filteredData.add(data);
            }
        }
        return filteredData;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button) {
            Intent it = new Intent(getActivity(), AddDataActivity.class);
            startActivityForResult(it, 1);
        }
        if (view.getId() == R.id.tvTotalInput) {
            Intent intent = new Intent(getActivity(), ReportActivity.class);
            ArrayList<String> incomeData = filterDataByType("收入");
            intent.putExtra("type", "收入");
            intent.putStringArrayListExtra("data", incomeData);
            startActivity(intent);
        }
        if (view.getId() == R.id.tvTotalOutput) {
            Intent intent = new Intent(getActivity(), ReportActivity.class);
            ArrayList<String> expenseData = filterDataByType("支出");
            intent.putExtra("type", "支出");
            intent.putStringArrayListExtra("data", expenseData);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            // 獲取回傳的資料
            String amount = data.getStringExtra("money");
            String dateTime = data.getStringExtra("dateTime");
            String type = data.getStringExtra("type");
            String spr = data.getStringExtra("class");

            if (amount != null && dateTime != null && type != null) {
                try {
                    double amountValue = Double.parseDouble(amount);

                    // 把資料添加到 ViewModel
                    String dataString = dateTime + "  金額: " + amount + "  " + type + " " + spr;
                    sharedViewModel.addData(amountValue, type, dataString);
                } catch (NumberFormatException e) {
                    // 顯示錯誤訊息或處理異常
                    Toast.makeText(getContext(), "金額格式錯誤", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
