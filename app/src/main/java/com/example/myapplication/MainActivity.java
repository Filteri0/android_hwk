package com.example.myapplication;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar; // 重新加入 Toolbar

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

/**
 * 主活動（MainActivity）
 * 管理應用程式的主要導航結構，包括抽屜導航和頂部工具欄
 */
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 使用 ViewBinding 初始化佈局
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 設置頂部工具欄
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        // 獲取抽屜佈局和導航視圖
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // 配置應用程式欄
        // 設置主要目的地（首頁、類別、網頁）
        // 並將抽屜佈局與之關聯
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_class, R.id.nav_web)
                .setOpenableLayout(drawer)
                .build();

        // 獲取導航控制器
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        // 將導航控制器與應用程式欄和抽屜導航關聯
        // 這樣可以實現標題、返回按鈕等功能的自動管理
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // 處理向上導航（返回）邏輯
        // 使用導航控制器來管理返回邏輯
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        // 嘗試導航到上一個目的地
        // 如果無法導航，則調用父類的向上導航邏輯
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}