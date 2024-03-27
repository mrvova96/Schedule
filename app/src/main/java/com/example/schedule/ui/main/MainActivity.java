package com.example.schedule.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.schedule.BuildConfig;
import com.example.schedule.R;
import com.example.schedule.data.database.ScheduleDatabase;
import com.example.schedule.data.database.dao.ScheduleDao;
import com.example.schedule.data.database.entity.Admin;
import com.example.schedule.data.database.entity.Day;
import com.example.schedule.data.database.entity.LessonsTime;
import com.example.schedule.data.database.entity.LoginDetails;
import com.example.schedule.data.database.entity.MainAdmin;
import com.example.schedule.data.enums.UserType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;

import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkDatabase();
        checkLoginSettings();
        //putData();
    }

    private void checkDatabase() {
        String dir = "/data/data/com.example.schedule/databases";
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdir();
            importDatabaseFromAssets(dir, "schedule_database");
            importDatabaseFromAssets(dir, "schedule_database-shm");
            importDatabaseFromAssets(dir, "schedule_database-wal");
        }
    }

    private void importDatabaseFromAssets(String dir, String name) {
        try {
            InputStream inStream = getAssets().open(name);
            OutputStream outStream = new FileOutputStream(dir + "/" + name);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
            inStream.close();
            outStream.close();
        } catch (Exception ignored) {
        }
    }

    private void checkLoginSettings() {
        SharedPreferences preferences = getSharedPreferences("loginSettings", MODE_PRIVATE);
        String userType = preferences.getString("userType", "");
        if (userType.isEmpty())
            launchNextFragment(LoginFragment.newInstance());
        else {
            String fullName = preferences.getString("fullName", "");
            String name = preferences.getString("name", "");
            String groupName = preferences.getString("groupName", "");
            long id = preferences.getLong("id", 0);
            launchNextFragment(WelcomeFragment.newInstance(UserType.valueOf(userType), fullName, name, groupName, id));
        }
    }

    private void launchNextFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit();
    }

    private void putData() {
        ScheduleDao scheduleDao = ScheduleDatabase.getInstance(getApplication()).scheduleDao();
        scheduleDao.add(new LoginDetails("1", "1"))
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long ID) {
                        scheduleDao.add(new MainAdmin(ID, "Платонов", "Владимир", "Алексеевич"))
                                .subscribeOn(Schedulers.io())
                                .subscribe();
                    }
                });

        Thread thread = new Thread(() -> {
            scheduleDao.add(new Day("Понедельник", "Числитель"));
            scheduleDao.add(new Day("Вторник", "Числитель"));
            scheduleDao.add(new Day("Среда", "Числитель"));
            scheduleDao.add(new Day("Четверг", "Числитель"));
            scheduleDao.add(new Day("Пятница", "Числитель"));
            scheduleDao.add(new Day("Суббота", "Числитель"));
            scheduleDao.add(new Day("Понедельник", "Знаменатель"));
            scheduleDao.add(new Day("Вторник", "Знаменатель"));
            scheduleDao.add(new Day("Среда", "Знаменатель"));
            scheduleDao.add(new Day("Четверг", "Знаменатель"));
            scheduleDao.add(new Day("Пятница", "Знаменатель"));
            scheduleDao.add(new Day("Суббота", "Знаменатель"));

            scheduleDao.add(new LessonsTime(1, "9:00", "10:35"));
            scheduleDao.add(new LessonsTime(2, "10:45", "12:20"));
            scheduleDao.add(new LessonsTime(3, "12:30", "14:05"));
            scheduleDao.add(new LessonsTime(4, "14:15", "15:50"));
            scheduleDao.add(new LessonsTime(5, "16:00", "17:35"));
            scheduleDao.add(new LessonsTime(6, "17:45", "19:20"));
        });
        thread.start();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    boolean touchTargetIsEditText = false;
                    for (View vi : v.getRootView().getTouchables()) {
                        if (vi instanceof EditText) {
                            Rect clickedViewRect = new Rect();
                            vi.getGlobalVisibleRect(clickedViewRect);
                            if (clickedViewRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                                touchTargetIsEditText = true;
                                break;
                            }
                        }
                    }
                    if (!touchTargetIsEditText) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
    }
}