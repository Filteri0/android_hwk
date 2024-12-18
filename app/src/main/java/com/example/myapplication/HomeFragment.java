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
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

/**
 * 主頁 Fragment - 記帳資訊總覽和新增功能
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    Button button; // 新增記帳按鈕
    ListView lv; // 記帳列表
    ArrayAdapter<String> adapter; // 列表適配器
    ArrayList<String> dataList; // 資料列表

    // 總收入、總支出和結餘顯示文字視圖
    private TextView tvTotalInput,
            tvTotalOutput,
            tvTotalBalance;

    private SharedViewModel sharedViewModel; // 共享數據的 ViewModel

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false); // 載入佈局
        initializeUIComponents(rootView); // 初始化UI元件
        setupViewModelObservers(); // 設置ViewModel監聽

        return rootView;
    }

    /**
     * 初始化 UI 元件
     * @param rootView 根佈局視圖
     */
    private void initializeUIComponents(View rootView) {
        button = rootView.findViewById(R.id.button); // 綁定新增按鈕
        lv = rootView.findViewById(R.id.listView); // 綁定列表視圖
        tvTotalInput = rootView.findViewById(R.id.tvTotalInput); // 綁定總收入文字視圖
        tvTotalOutput = rootView.findViewById(R.id.tvTotalOutput); // 綁定總支出文字視圖
        tvTotalBalance = rootView.findViewById(R.id.tvTotal); // 綁定結餘文字視圖

        button.setOnClickListener(this); // 設置按鈕點擊監聽器
        tvTotalInput.setOnClickListener(this); // 設置總收入文字視圖點擊監聽器
        tvTotalOutput.setOnClickListener(this); // 設置總支出文字視圖點擊監聽器

        dataList = new ArrayList<>(); // 初始化資料列表
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList); // 創建列表適配器
        lv.setAdapter(adapter); // 設置列表適配器
    }

    /**
     * 設置 ViewModel 數據監聽
     * 監聽總收入、總支出、結餘和資料列表的變化
     */
    private void setupViewModelObservers() {
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class); // 獲取共享ViewModel

        sharedViewModel.getTotalInput().observe(getViewLifecycleOwner(), totalInput ->
                tvTotalInput.setText("總收入  ▶\n" + totalInput) // 更新總收入顯示
        );

        sharedViewModel.getTotalOutput().observe(getViewLifecycleOwner(), totalOutput ->
                tvTotalOutput.setText("總支出  ▶\n" + totalOutput) // 更新總支出顯示
        );

        sharedViewModel.getBalance().observe(getViewLifecycleOwner(), balance ->
                tvTotalBalance.setText("結餘: " + balance) // 更新結餘顯示
        );

        sharedViewModel.getDataList().observe(getViewLifecycleOwner(), newDataList -> {
            dataList.clear();      // 清空舊資料
            dataList.addAll(newDataList);  // 添加新資料
            adapter.notifyDataSetChanged();  // 更新 ListView
        });
    }

    /**
     * 根據類型篩選記帳資料
     * @param type 篩選類型（"收入"或"支出"）
     * @return 篩選後的資料列表
     */
    private ArrayList<String> filterDataByType(String type) {
        ArrayList<String> filteredData = new ArrayList<>();
        for (String data : dataList) {
            if (data.contains(type)) { // 判斷資料是否包含指定類型
                filteredData.add(data);
            }
        }
        return filteredData;
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.button) { // 點擊新增按鈕
            Intent it = new Intent(getActivity(), AddDataActivity.class);
            startActivityForResult(it, 1);
        }
        else if (viewId == R.id.tvTotalInput) // 點擊總收入文字視圖
            openReportActivity("收入");
        else if (viewId == R.id.tvTotalOutput) // 點擊總支出文字視圖
            openReportActivity("支出");
    }

    /**
     * 開啟報表頁面
     * @param type 報表類型（"收入"或"支出"）
     */
    private void openReportActivity(String type) {
        Intent intent = new Intent(getActivity(), ReportActivity.class);
        ArrayList<String> filteredData = filterDataByType(type); // 篩選指定類型資料
        intent.putExtra("type", type);
        intent.putStringArrayListExtra("data", filteredData);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 處理新增記帳的回傳結果
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            String amount = data.getStringExtra("money"); // 獲取金額
            String dateTime = data.getStringExtra("dateTime"); // 獲取日期
            String type = data.getStringExtra("type"); // 獲取類型（收入/支出）
            String spr = data.getStringExtra("class"); // 獲取分類

            // 驗證資料並處理
            if (amount != null && dateTime != null && type != null) {
                try {
                    double amountValue = Double.parseDouble(amount); // 轉換金額為雙精度浮點數
                    String dataString = dateTime + "  金額: " + amount + "  " + type + " " + spr; // 組合顯示字串

                    sharedViewModel.addData(amountValue, type, dataString); // 添加資料到 ViewModel
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "金額格式錯誤", Toast.LENGTH_SHORT).show(); // 顯示金額格式錯誤提示
                }
            }
        }
    }
}