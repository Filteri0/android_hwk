package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * SharedViewModel 用於管理記帳應用程式的共享數據狀態
 * 繼承自 ViewModel，可以在配置更改（如螢幕旋轉）時保存和恢復數據
 */
public class SharedViewModel extends ViewModel {
    // MutableLiveData 用於存儲可變的數據，並在數據變化時通知觀察者
    // 記錄所有記帳數據的字串列表
    private final MutableLiveData<ArrayList<String>> dataList = new MutableLiveData<>(new ArrayList<>());


    private final MutableLiveData<Double> totalInput = new MutableLiveData<>(0.0); // 記錄總收入
    private final MutableLiveData<Double> totalOutput = new MutableLiveData<>(0.0); // 記錄總支出
    private final MutableLiveData<Double> balance = new MutableLiveData<>(0.0); // 記錄當前結餘

    public LiveData<ArrayList<String>> getDataList() {
        return dataList;
    }
    public LiveData<Double> getTotalInput() {
        return totalInput;
    }
    public LiveData<Double> getTotalOutput() {
        return totalOutput;
    }
    // 提供對結餘的只讀訪問
    public LiveData<Double> getBalance() {
        return balance;
    }

    /**
     * 添加新的記帳數據
     * @param amount 金額
     * @param type 類型（"收入"或"支出"）
     * @param dataString 要顯示的數據字串
     */
    public void addData(double amount, String type, String dataString) {
        if ("收入".equals(type))
            totalInput.setValue((totalInput.getValue() != null ? totalInput.getValue() : 0.0) + amount); // 當前總收入值，為空則使用 0.0
        else if ("支出".equals(type))
            totalOutput.setValue((totalOutput.getValue() != null ? totalOutput.getValue() : 0.0) + amount); // 獲取總支出值，為空則使用 0.0


        // 計算新的結餘（總收入 - 總支出）
        double newBalance = (totalInput.getValue() != null ? totalInput.getValue() : 0.0) -
                (totalOutput.getValue() != null ? totalOutput.getValue() : 0.0);

        balance.setValue(newBalance); // 更新結餘

        ArrayList<String> updatedList = new ArrayList<>(dataList.getValue() != null ? dataList.getValue() : new ArrayList<>()); // 更新數據列表
        updatedList.add(dataString); // 添加新的數據字串
        dataList.setValue(updatedList); // 更新 dataList
    }
}