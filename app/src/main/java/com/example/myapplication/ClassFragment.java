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

/**
 * 類別報表Fragment - 提供收支類別明細和總金額的視圖
 */
public class ClassFragment extends Fragment {
    SharedViewModel sharedViewModel; // 用於管理跨頁面共享數據的ViewModel

    // UI 元件宣告
    TextView tvTotalAmount; // 顯示總金額的文字視圖
    ListView listView; // 顯示明細列表的列表視圖
    Spinner spinnerReportType; // 類別選擇下拉選單
    Switch switchIncomeExpense; // 收入/支出切換開關

    ArrayAdapter<String> listAdapter; // 列表的適配器
    ArrayAdapter<String> spinnerAdapter; // 下拉選單的適配器

    private ArrayList<String> currentData = new ArrayList<>(); // 當前顯示的資料列表

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_class, container, false); // 載入佈局

        initializeUIComponents(rootView); // 初始化UI元件
        setupUIListeners(); // 設置UI監聽器
        observeViewModelData(); // 觀察ViewModel資料變化

        return rootView;
    }

    /**
     * 初始化所有 UI 元件的引用
     * @param rootView 根佈局視圖
     */
    private void initializeUIComponents(View rootView) {
        tvTotalAmount = rootView.findViewById(R.id.tvTotalAmount); // 綁定總金額文字視圖
        listView = rootView.findViewById(R.id.lvReportList); // 綁定列表視圖
        spinnerReportType = rootView.findViewById(R.id.spinnerReportType); // 綁定下拉選單
        switchIncomeExpense = rootView.findViewById(R.id.switchIncomeExpense); // 綁定收支開關

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class); // 獲取共享ViewModel

        listAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, currentData); // 創建列表適配器
        listView.setAdapter(listAdapter); // 設置列表適配器

        spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new ArrayList<>()); // 創建下拉選單適配器
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 設置下拉視圖樣式
        spinnerReportType.setAdapter(spinnerAdapter); // 設置下拉選單適配器
    }

    /**
     * 設置 UI 元件的監聽器
     */
    private void setupUIListeners() {
        switchIncomeExpense.setOnCheckedChangeListener((buttonView, isChecked) -> { // 收支開關變更監聽器
            updateSpinnerCategories(isChecked); // 更新下拉選單類別
        });

        spinnerReportType.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) { // 下拉選單選擇監聽器
                updateListView(sharedViewModel.getDataList().getValue()); // 更新列表視圖
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // 未選擇任何項目時的處理
            }
        });
    }

    /**
     * 觀察 ViewModel 資料變化
     */
    private void observeViewModelData() {
        sharedViewModel.getDataList().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> dataList) { // 資料變更時的回調
                updateListView(dataList); // 更新列表視圖
            }
        });

        updateSpinnerCategories(false); // 預設設置為支出類別
    }

    private void updateSpinnerCategories(boolean isIncome) { // 更新下拉選單類別
        // 根據收支類型選擇對應的資源陣列
        int arrayResourceId = isIncome ?
                R.array.add_input_spinner :
                R.array.add_output_spinner;

        String[] categories = getResources().getStringArray(arrayResourceId); // 從資源中取得類別陣列

        spinnerAdapter.clear(); // 清除舊有資料
        spinnerAdapter.addAll(categories); // 添加新類別
        spinnerAdapter.notifyDataSetChanged(); // 通知資料集變更
    }

    /**
     * 更新 ListView 顯示的資料
     * @param dataList 原始資料列表
     */
    private void updateListView(ArrayList<String> dataList) {
        if (dataList == null) return; // 若資料為空則返回

        boolean showIncome = switchIncomeExpense.isChecked(); // 判斷是否顯示收入
        String typeToShow = showIncome ? "收入" : "支出"; // 確定要顯示的類型
        String selectedCategory = spinnerReportType.getSelectedItem() != null ? // 取得選擇的類別
                spinnerReportType.getSelectedItem().toString() : "全部";

        currentData.clear(); // 清空當前資料
        double totalAmount = 0; // 初始化總金額

        for (String item : dataList) { // 遍歷資料列表
            if (item.contains(typeToShow)) { // 篩選收支類型
                if ("全部".equals(selectedCategory) || item.contains(selectedCategory)) { // 篩選類別
                    currentData.add(item); // 添加符合條件的項目

                    String[] parts = item.split("金額: ");
                    if (parts.length > 1) {
                        try {
                            String amountStr = parts[1].split(" ")[0]; // 提取金額
                            totalAmount += Double.parseDouble(amountStr); // 計算總金額
                        } catch (NumberFormatException ignored) {
                            // 忽略金額轉換錯誤
                        }
                    }
                }
            }
        }

        listAdapter.notifyDataSetChanged(); // 通知列表資料變更
        tvTotalAmount.setText(String.valueOf(totalAmount)); // 設置總金額顯示
    }
}