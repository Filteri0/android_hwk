package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassFragment extends Fragment {
    SharedViewModel sharedViewModel;
    TextView tvTotalAmount;
    ListView listView;
    Spinner spinnerReportType;
    Switch switchIncomeExpense;
    ArrayAdapter<String> listAdapter;
    private ArrayList<String> currentData = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_class, container, false);

        // 初始化 UI 元件
        tvTotalAmount = rootView.findViewById(R.id.tvTotalAmount);
        listView = rootView.findViewById(R.id.lvReportList);
        spinnerReportType = rootView.findViewById(R.id.spinnerReportType);
        switchIncomeExpense = rootView.findViewById(R.id.switchIncomeExpense);

        // 初始化 ViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // 初始化 ListView 的 Adapter
        listAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, currentData);
        listView.setAdapter(listAdapter);

        // 初始化 Spinner 的 Adapter
        spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReportType.setAdapter(spinnerAdapter);

        // 設置 Switch 監聽器
        switchIncomeExpense.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSpinnerCategories(isChecked);
        });

        // 觀察 ViewModel 的資料列表
        sharedViewModel.getDataList().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> dataList) {
                updateListView(dataList);
            }
        });

        // 初始化時設置支出類別
        updateSpinnerCategories(false);

        // Spinner 選項改變時更新 ListView
        spinnerReportType.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                updateListView(sharedViewModel.getDataList().getValue());
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // 無操作
            }
        });

        return rootView;
    }

    // 根據 Switch 狀態更新 Spinner 的類別
    private void updateSpinnerCategories(boolean isIncome) {
        // 取得對應的字串陣列資源
        int arrayResourceId = isIncome ?
                R.array.add_input_spinner :
                R.array.add_output_spinner;

        // 從資源中取得類別陣列
        String[] categories = getResources().getStringArray(arrayResourceId);


        spinnerAdapter.clear();
        spinnerAdapter.addAll(categories);
        spinnerAdapter.notifyDataSetChanged();
    }

    private void updateListView(ArrayList<String> dataList) {
        if (dataList == null) return;

        // 根據 Switch 和 Spinner 的狀態篩選資料
        boolean showIncome = switchIncomeExpense.isChecked();
        String typeToShow = showIncome ? "收入" : "支出";
        String selectedCategory = spinnerReportType.getSelectedItem() != null ?
                spinnerReportType.getSelectedItem().toString() : "全部";

        currentData.clear();
        double totalAmount = 0;

        for (String item : dataList) {
            if (item.contains(typeToShow)) {
                if ("全部".equals(selectedCategory) || item.contains(selectedCategory)) {
                    currentData.add(item);

                    // 從資料中提取金額並累加
                    String[] parts = item.split("金額: ");
                    if (parts.length > 1) {
                        try {
                            String amountStr = parts[1].split(" ")[0];
                            totalAmount += Double.parseDouble(amountStr);
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
            }
        }

        // 更新 ListView 和總金額
        listAdapter.notifyDataSetChanged();
        tvTotalAmount.setText(String.valueOf(totalAmount));
    }
}