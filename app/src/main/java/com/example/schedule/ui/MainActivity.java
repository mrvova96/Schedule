package com.example.schedule.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Toast;

import com.example.schedule.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment = LoginFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
    }
}