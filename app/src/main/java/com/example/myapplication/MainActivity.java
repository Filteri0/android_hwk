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
 * 記帳App的主活動 - 管理應用程式的主要導航結構和頁面交互
 */
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration; // 應用程式欄配置，定義導航目的地和抽屜佈局
    private ActivityMainBinding binding; // 視圖綁定，簡化視圖訪問和初始化

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // 調用父類的onCreate方法

        binding = ActivityMainBinding.inflate(getLayoutInflater()); // 使用ViewBinding技術載入佈局
        setContentView(binding.getRoot()); // 設置當前視圖的根佈局

        Toolbar toolbar = binding.toolbar; // 獲取工具欄元件
        setSupportActionBar(toolbar); // 設置自定義工具欄作為活動的操作欄

        DrawerLayout drawer = binding.drawerLayout; // 獲取側邊抽屜佈局
        NavigationView navigationView = binding.navView; // 獲取導航視圖元件

        // 配置應用程式欄，指定主要目的地頁面並關聯抽屜佈局
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_class, R.id.nav_web)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main); // 獲取導航控制器

        // 將導航控制器與應用程式欄和導航視圖關聯，自動處理標題、返回按鈕等邏輯
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main); // 重新獲取導航控制器

        // 處理向上導航邏輯，優先使用導航控制器的返回方法
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}