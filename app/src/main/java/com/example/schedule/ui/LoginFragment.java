package com.example.schedule.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.schedule.R;
import com.example.schedule.ScheduleRepository;
import com.example.schedule.data.roles.ScheduleRole;
import com.example.schedule.presentation.login.LoginViewModel;

public class LoginFragment extends Fragment {

    private LoginViewModel viewModel = new LoginViewModel(new ScheduleRepository() {
        @Override
        public boolean login(String login, String password) {
            return false;
        }

        @Override
        public ScheduleRole getCurrentUserScheduleRole() {
            return null;
        }
    });

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.login("lol", "kek");
        viewModel.loginLiveData.observe(getViewLifecycleOwner(), loginState -> {
            boolean userExists = loginState.isUserExists();
        });
    }
}