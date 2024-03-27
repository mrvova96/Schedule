package com.example.schedule.ui.main;

import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.schedule.R;
import com.example.schedule.data.database.entity.MainAdmin;
import com.example.schedule.data.enums.UserType;
import com.example.schedule.databinding.FragmentMainBinding;
import com.example.schedule.databinding.FragmentWelcomeBinding;

import java.io.Serializable;
import java.util.Calendar;
import java.util.TimeZone;

public class WelcomeFragment extends Fragment {

    private static final String USER_TYPE = "userType";
    private static final String USER_NAME = "userName";
    private static final String NAME = "name";
    private static final String GROUP_NAME = "groupName";
    private static final String ID = "ID";

    private UserType userType;
    private String fullName;
    private String name;
    private String groupName;
    private long id;

    private FragmentWelcomeBinding binding;

    public static WelcomeFragment newInstance(
            UserType userType, String fullName, String name, String groupName, long id) {
        WelcomeFragment fragment = new WelcomeFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER_TYPE, userType);
        args.putString(USER_NAME, fullName);
        args.putString(NAME, name);
        args.putString(GROUP_NAME, groupName);
        args.putLong(ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            userType = (UserType) getArguments().getSerializable(USER_TYPE);
            fullName = getArguments().getString(USER_NAME);
            name = getArguments().getString(NAME);
            groupName = getArguments().getString(GROUP_NAME);
            id = getArguments().getLong(ID);
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        saveLoginSettings();
        setOnBackPressed();

        int hour = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow")).get(Calendar.HOUR_OF_DAY);
        if (hour > 4 && hour <= 11) {
            binding.layout.setBackgroundResource(R.drawable.background_morning);
            binding.textViewWelcome.setText("Доброе утро,\n".concat(name).concat("!"));
        } else if (hour > 11 && hour <= 16) {
            binding.layout.setBackgroundResource(R.drawable.background_afternoon);
            binding.textViewWelcome.setText("Добрый день,\n".concat(name).concat("!"));
        } else if (hour > 16 && hour <= 23) {
            binding.layout.setBackgroundResource(R.drawable.background_evening);
            binding.textViewWelcome.setText("Добрый вечер,\n".concat(name).concat("!"));
        } else {
            binding.layout.setBackgroundResource(R.drawable.background_night);
            binding.textViewWelcome.setText("Доброй ночи,\n".concat(name).concat("!"));
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                launchNextFragment(MainFragment.newInstance(userType, fullName, groupName, id));
            }
        }, 3000);
    }

    private void launchNextFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.fragmentContainer, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    private void saveLoginSettings() {
        SharedPreferences prefs = getActivity().getSharedPreferences("loginSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userType", userType.name());
        editor.putString("fullName", fullName);
        editor.putString("name", name);
        editor.putString("groupName", groupName);
        editor.putLong("id", id);
        editor.apply();
    }

    private void setOnBackPressed() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(requireContext())
                        .setMessage("Выйти из приложения?")
                        .setPositiveButton("Выйти", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().finish();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("Назад", null)
                        .show();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }
}