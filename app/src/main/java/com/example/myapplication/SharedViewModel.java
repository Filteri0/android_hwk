package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<String>> dataList = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Double> totalInput = new MutableLiveData<>(0.0);
    private final MutableLiveData<Double> totalOutput = new MutableLiveData<>(0.0);
    private final MutableLiveData<Double> balance = new MutableLiveData<>(0.0);

    public LiveData<ArrayList<String>> getDataList() {
        return dataList;
    }

    public LiveData<Double> getTotalInput() {
        return totalInput;
    }

    public LiveData<Double> getTotalOutput() {
        return totalOutput;
    }

    public LiveData<Double> getBalance() {
        return balance;
    }

    public void addData(double amount, String type, String dataString) {
        // 更新總收入或支出
        if ("收入".equals(type)) {
            totalInput.setValue((totalInput.getValue() != null ? totalInput.getValue() : 0.0) + amount);
        } else if ("支出".equals(type)) {
            totalOutput.setValue((totalOutput.getValue() != null ? totalOutput.getValue() : 0.0) + amount);
        }

        // 更新結餘
        double newBalance = (totalInput.getValue() != null ? totalInput.getValue() : 0.0) -
                (totalOutput.getValue() != null ? totalOutput.getValue() : 0.0);
        balance.setValue(newBalance);

        // 更新資料列表
        ArrayList<String> updatedList = new ArrayList<>(dataList.getValue() != null ? dataList.getValue() : new ArrayList<>());
        updatedList.add(dataString);
        dataList.setValue(updatedList);
    }
}
