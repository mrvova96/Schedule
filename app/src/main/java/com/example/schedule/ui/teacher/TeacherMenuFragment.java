package com.example.schedule.ui.teacher;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.schedule.R;
import com.example.schedule.data.enums.UserType;
import com.example.schedule.databinding.FragmentMainBinding;
import com.example.schedule.databinding.FragmentTeacherMenuBinding;
import com.example.schedule.ui.main.MainFragment;

public class TeacherMenuFragment extends Fragment {

    private static final String FACULTY_ID = "facultyID";
    private static final String FACULTY_NAME = "facultyName";
    private static final String USER_TYPE = "userType";

    private int facultyID;
    private String facultyName;
    private UserType userType;

    private FragmentTeacherMenuBinding binding;
    private FragmentMainBinding parentBinding;

    public static TeacherMenuFragment newInstance(int facultyID, String facultyName, UserType userType) {
        TeacherMenuFragment fragment = new TeacherMenuFragment();
        Bundle args = new Bundle();
        args.putInt(FACULTY_ID, facultyID);
        args.putString(FACULTY_NAME, facultyName);
        args.putSerializable(USER_TYPE, userType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTeacherMenuBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        if (getParentFragment() != null)
            parentBinding = ((MainFragment) getParentFragment()).getBinding();

        if (getArguments() != null) {
            facultyID = getArguments().getInt(FACULTY_ID);
            facultyName = getArguments().getString(FACULTY_NAME);
            userType = (UserType) getArguments().getSerializable(USER_TYPE);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnBackPressed();

        binding.buttonTeachersList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentBinding.imageButtonAdd.setVisibility(View.VISIBLE);
                launchNextFragment(TeacherFragment.newInstance(facultyID));
            }
        });

        binding.buttonTeacherLessons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentBinding.textViewAdminPanel.setText("Выберите преподавателя");
                launchNextFragment(TeacherListFragment.newInstance(facultyID, facultyName,
                        String.valueOf(binding.buttonTeacherLessons.getText())));
            }
        });
    }

    private void launchNextFragment(Fragment fragment) {
        getParentFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.fragmentContainerAdmin, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    private void setOnBackPressed() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (userType == UserType.ADMIN)
                    parentBinding.textViewAdminPanel.setText(facultyName);
                else
                    parentBinding.textViewAdminPanel.setText("Выберите факультет");
                getParentFragmentManager().popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        parentBinding = null;
    }
}