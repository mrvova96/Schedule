package com.example.schedule.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.schedule.R;
import com.example.schedule.data.roles.ScheduleRole;
import com.example.schedule.presentation.login.LoginViewModel;
import com.google.android.material.snackbar.Snackbar;

public class LoginFragment extends Fragment {

    private static final String VALIDATION_KEY = "validationKey";

    private LoginViewModel viewModel;
    private EditText editTextLogin;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Snackbar snackbar;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        buttonLogin = view.findViewById(R.id.buttonLogin);
        editTextLogin = view.findViewById(R.id.editTextLogin);
        editTextPassword = view.findViewById(R.id.editTextPassword);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.inputCheck(editTextLogin.getText().toString(), editTextPassword.getText().toString());
                viewModel.setValidateInProcess(true);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.isUserExists().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isExists) {
                if (!isExists) {
                    showErrorMessage(view);
                }
            }
        });

        viewModel.getScheduleRole().observe(getViewLifecycleOwner(), new Observer<ScheduleRole>() {
            @Override
            public void onChanged(ScheduleRole scheduleRole) {
                if (viewModel.getValidateInProcess()) {
                    viewModel.setValidateInProcess(false);
                    switch (scheduleRole) {
                        case ADMIN: {
                            //launchNextFragment(TestFragment.newInstance());
                        }
                        case STUDENT: {
                            launchNextFragment(TestFragment.newInstance());
                        }
                        case TEACHER: {
                            //launchNextFragment(TestFragment.newInstance());
                        }
                    }
                }
            }
        });
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

    private void showErrorMessage(View view) {
        snackbar = Snackbar.make(view, R.string.snackbar_message, 4000);
        snackbar.setBackgroundTint(getResources().getColor(R.color.pink, null));
        snackbar.setActionTextColor(getResources().getColor(R.color.black, null));
        TextView snackbarText = (TextView) snackbar.getView()
                .findViewById(com.google.android.material.R.id.snackbar_text);
        snackbarText.setTextSize(16);
        snackbarText.setTextColor(getResources().getColor(R.color.black, null));
        snackbar.setAction(R.string.snackbar_action, v -> snackbar.dismiss());
        snackbar.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (snackbar != null)
            snackbar.dismiss();
    }
}