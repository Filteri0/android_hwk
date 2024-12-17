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

/**
 * 主頁 Fragment
 * 顯示記帳資訊、收支總覽，並提供新增記帳功能
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    Button button;
    ListView lv;
    ArrayAdapter<String> adapter;
    ArrayList<String> dataList;

    private TextView tvTotalInput,
            tvTotalOutput,
            tvTotalBalance;

    // ViewModel 用於管理共享數據
    private SharedViewModel sharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initializeUIComponents(rootView);
        setupViewModelObservers();

        return rootView;
    }

    /**
     * 初始化 UI 元件
     * @param rootView 根佈局視圖
     */
    private void initializeUIComponents(View rootView) {
        button = rootView.findViewById(R.id.button);
        lv = rootView.findViewById(R.id.listView);
        tvTotalInput = rootView.findViewById(R.id.tvTotalInput);
        tvTotalOutput = rootView.findViewById(R.id.tvTotalOutput);
        tvTotalBalance = rootView.findViewById(R.id.tvTotal);

        button.setOnClickListener(this);
        tvTotalInput.setOnClickListener(this);
        tvTotalOutput.setOnClickListener(this);

        dataList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        lv.setAdapter(adapter);
    }

    /**
     * 設置 ViewModel 數據監聽
     * 監聽總收入、總支出、結餘和資料列表的變化
     */
    private void setupViewModelObservers() {
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        sharedViewModel.getTotalInput().observe(getViewLifecycleOwner(), totalInput ->
                tvTotalInput.setText("總收入  ▶\n" + totalInput)
        );

        sharedViewModel.getTotalOutput().observe(getViewLifecycleOwner(), totalOutput ->
                tvTotalOutput.setText("總支出  ▶\n" + totalOutput)
        );

        sharedViewModel.getBalance().observe(getViewLifecycleOwner(), balance ->
                tvTotalBalance.setText("結餘: " + balance)
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
            if (data.contains(type)) {
                filteredData.add(data);
            }
        }
        return filteredData;
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.button) {
            Intent it = new Intent(getActivity(), AddDataActivity.class);
            startActivityForResult(it, 1);
        }
        else if (viewId == R.id.tvTotalInput)
            openReportActivity("收入");
        else if (viewId == R.id.tvTotalOutput)
            openReportActivity("支出");
    }

    /**
     * 開啟報表頁面
     * @param type 報表類型（"收入"或"支出"）
     */
    private void openReportActivity(String type) {
        Intent intent = new Intent(getActivity(), ReportActivity.class);
        ArrayList<String> filteredData = filterDataByType(type);
        intent.putExtra("type", type);
        intent.putStringArrayListExtra("data", filteredData);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 處理新增記帳的回傳結果
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            String amount = data.getStringExtra("money");
            String dateTime = data.getStringExtra("dateTime");
            String type = data.getStringExtra("type");
            String spr = data.getStringExtra("class");

            // 驗證資料並處理
            if (amount != null && dateTime != null && type != null) {
                try {
                    double amountValue = Double.parseDouble(amount);
                    String dataString = dateTime + "  金額: " + amount + "  " + type + " " + spr;

                    sharedViewModel.addData(amountValue, type, dataString);
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "金額格式錯誤", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}