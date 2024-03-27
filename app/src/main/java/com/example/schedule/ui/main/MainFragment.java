package com.example.schedule.ui.main;

import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.schedule.R;
import com.example.schedule.data.enums.UserType;
import com.example.schedule.databinding.FragmentMainBinding;
import com.example.schedule.ui.admin.AdminMenuFragment;
import com.example.schedule.ui.schedule.ScheduleMainFragment;

public class MainFragment extends Fragment {

    private static final String USER_TYPE = "userType";
    private static final String USER_NAME = "userName";
    private static final String GROUP_NAME = "groupName";
    private static final String ID = "ID";

    private UserType userType;
    private String userName;
    private String groupName;
    private long id;

    private FragmentMainBinding binding;

    public static MainFragment newInstance(
            UserType userType, String userName, String groupName, long id) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER_TYPE, userType);
        args.putString(USER_NAME, userName);
        args.putString(GROUP_NAME, groupName);
        args.putLong(ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            userType = (UserType) getArguments().getSerializable(USER_TYPE);
            userName = getArguments().getString(USER_NAME);
            groupName = getArguments().getString(GROUP_NAME);
            id = getArguments().getLong(ID);
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.imageButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(requireContext())
                        .setMessage("Выйти из учетной записи?")
                        .setPositiveButton("Выйти", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().deleteSharedPreferences("loginSettings");
                                launchLoginFragment();
                            }
                        })
                        .setNegativeButton("Назад", null)
                        .show();
            }
        });

        binding.textViewUserName.setText(userName);

        switch (userType) {
            case MAIN_ADMIN:
                binding.textViewGroupName.setText(groupName);
                binding.textViewAdminPanel.setText("Панель администратора");
                launchNextFragment(AdminMenuFragment.newInstance(userType, 0, null));
                break;
            case ADMIN:
                binding.textViewGroupName.setText("Администратор");
                binding.textViewAdminPanel.setText(groupName);
                launchNextFragment(AdminMenuFragment.newInstance(userType, (int) id, groupName));
                break;
            case STUDENT:
                binding.textViewGroupName.setText(groupName);
                launchNextFragment(ScheduleMainFragment.newInstance(userType, 0, id, 0));
                break;
            case TEACHER:
                binding.textViewGroupName.setText(groupName);
                launchNextFragment(ScheduleMainFragment.newInstance(userType, 0, 0, id));
                break;
        }
    }

    private void launchNextFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainerAdmin, fragment)
                .commit();
    }

    private void launchLoginFragment() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.fragmentContainer, LoginFragment.newInstance())
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    public FragmentMainBinding getBinding() {
        return binding;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}