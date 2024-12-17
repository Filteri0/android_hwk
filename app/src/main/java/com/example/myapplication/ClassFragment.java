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

/**
 * 類別報表Fragment
 * 提供使用者查看收入/支出的類別明細和總金額
 */
public class ClassFragment extends Fragment {
    // ViewModel 用於管理跨頁面的共享數據
    SharedViewModel sharedViewModel;

    // UI 元件
    TextView tvTotalAmount;
    ListView listView;
    Spinner spinnerReportType;
    Switch switchIncomeExpense;


    ArrayAdapter<String> listAdapter;
    ArrayAdapter<String> spinnerAdapter;

    private ArrayList<String> currentData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_class, container, false);

        initializeUIComponents(rootView);
        setupUIListeners();
        observeViewModelData();

        return rootView;
    }

    /**
     * 初始化所有 UI 元件的引用
     * @param rootView 根佈局視圖
     */
    private void initializeUIComponents(View rootView) {
        tvTotalAmount = rootView.findViewById(R.id.tvTotalAmount);
        listView = rootView.findViewById(R.id.lvReportList);
        spinnerReportType = rootView.findViewById(R.id.spinnerReportType);
        switchIncomeExpense = rootView.findViewById(R.id.switchIncomeExpense);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        listAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, currentData);
        listView.setAdapter(listAdapter);

        spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReportType.setAdapter(spinnerAdapter);
    }

    /**
     * 設置 UI 元件的監聽器
     */
    private void setupUIListeners() {
        switchIncomeExpense.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSpinnerCategories(isChecked);
        });

        spinnerReportType.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                updateListView(sharedViewModel.getDataList().getValue());
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {

            }
        });
    }

    /**
     * 觀察 ViewModel 資料變化
     */
    private void observeViewModelData() {
        sharedViewModel.getDataList().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> dataList) {
                updateListView(dataList);
            }
        });

        updateSpinnerCategories(false);
    }

    private void updateSpinnerCategories(boolean isIncome) {
        int arrayResourceId = isIncome ?
                R.array.add_input_spinner :
                R.array.add_output_spinner;

        // 從資源中取得類別陣列
        String[] categories = getResources().getStringArray(arrayResourceId);

        spinnerAdapter.clear();
        spinnerAdapter.addAll(categories);
        spinnerAdapter.notifyDataSetChanged();
    }

    /**
     * 更新 ListView 顯示的資料
     * @param dataList 原始資料列表
     */
    private void updateListView(ArrayList<String> dataList) {
        if (dataList == null) return;

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

        listAdapter.notifyDataSetChanged();
        tvTotalAmount.setText(String.valueOf(totalAmount));
    }
}