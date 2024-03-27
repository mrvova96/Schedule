package com.example.schedule.ui.schedule;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.schedule.R;
import com.example.schedule.data.enums.UserType;
import com.example.schedule.databinding.FragmentMainBinding;
import com.example.schedule.databinding.FragmentScheduleMainBinding;
import com.example.schedule.ui.main.MainFragment;
import com.example.schedule.adapter.viewPager.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;

public class ScheduleMainFragment extends Fragment {

    private static final String USER_TYPE = "userType";
    private static final String FACULTY_ID = "facultyID";
    private static final String GROUP_ID = "groupID";
    private static final String TEACHER_ID = "teacherID";

    private UserType userType;
    private int facultyID;
    private long groupID;
    private long teacherID;

    private boolean openCurrentDay;
    private String weekType;
    private Calendar calendar;
    private String[] months;

    private FragmentScheduleMainBinding binding;
    private FragmentMainBinding parentBinding;

    public static ScheduleMainFragment newInstance(UserType userType, int facultyID, long groupID, long teacherID) {
        ScheduleMainFragment fragment = new ScheduleMainFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER_TYPE, userType);
        args.putInt(FACULTY_ID, facultyID);
        args.putLong(GROUP_ID, groupID);
        args.putLong(TEACHER_ID, teacherID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentScheduleMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        if (getParentFragment() != null)
            parentBinding = ((MainFragment) getParentFragment()).getBinding();

        if (getArguments() != null) {
            userType = (UserType) getArguments().getSerializable(USER_TYPE);
            facultyID = getArguments().getInt(FACULTY_ID);
            groupID = getArguments().getLong(GROUP_ID);
            teacherID = getArguments().getLong(TEACHER_ID);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnBackPressed();
        initData();
        initPagerAdapter(0);
        openCurrentDay = true;

        binding.imageButtonNavRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (weekType.equals(getString(R.string.numerator))) {
                    weekType = getString(R.string.denominator);
                } else {
                    weekType = getString(R.string.numerator);
                }
                initPagerAdapter(2);
            }
        });

        binding.imageButtonNavLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (weekType.equals(getString(R.string.numerator))) {
                    weekType = getString(R.string.denominator);
                } else {
                    weekType = getString(R.string.numerator);
                }
                initPagerAdapter(-12);
            }
        });
    }

    private void initPagerAdapter(int offset) {
        binding.textViewWeekType.setText(weekType);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, userType, facultyID, groupID,
                teacherID, parentBinding.tabLayout, weekType, openCurrentDay);
        binding.viewPager.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(parentBinding.tabLayout, binding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        calendar.add(Calendar.DATE, offset);
                        tab.setText("ПН\n" + calendar.get(Calendar.DATE));
                        binding.textViewCurrentMonth.setText(months[calendar.get(Calendar.MONTH)]
                                .concat(" ").concat(String.valueOf(calendar.get(Calendar.YEAR))));
                        break;
                    case 1:
                        calendar.add(Calendar.DATE, 1);
                        tab.setText("ВТ\n" + calendar.get(Calendar.DATE));
                        break;
                    case 2:
                        calendar.add(Calendar.DATE, 1);
                        tab.setText("СР\n" + calendar.get(Calendar.DATE));
                        break;
                    case 3:
                        calendar.add(Calendar.DATE, 1);
                        tab.setText("ЧТ\n" + calendar.get(Calendar.DATE));
                        break;
                    case 4:
                        calendar.add(Calendar.DATE, 1);
                        tab.setText("ПТ\n" + calendar.get(Calendar.DATE));
                        break;
                    case 5:
                        calendar.add(Calendar.DATE, 1);
                        tab.setText("СБ\n" + calendar.get(Calendar.DATE));
                        break;
                }
            }
        }).attach();
    }

    private void initData() {
        weekType = getString(R.string.numerator);
        months = getResources().getStringArray(R.array.months_of_the_year);
        calendar = Calendar.getInstance();
        if (LocalDate.now().getDayOfWeek() == DayOfWeek.SUNDAY)
            calendar.add(Calendar.DATE, 1);
        else
            calendar.add(Calendar.DATE, 1 - LocalDate.now().getDayOfWeek().getValue());
    }

    private void setOnBackPressed() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (userType == UserType.ADMIN) {
                    parentBinding.textViewGroupName.setText("Администратор");
                    parentBinding.textViewAdminPanel.setVisibility(View.VISIBLE);
                    parentBinding.tabLayout.setVisibility(View.GONE);
                    getParentFragmentManager().popBackStack();
                } else {
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