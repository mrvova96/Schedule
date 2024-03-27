package com.example.schedule.adapter.viewPager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.schedule.data.enums.UserType;
import com.example.schedule.databinding.FragmentMainBinding;
import com.example.schedule.databinding.FragmentScheduleMainBinding;
import com.example.schedule.ui.schedule.ScheduleFragment;
import com.google.android.material.tabs.TabLayout;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private UserType userType;
    private int facultyID;
    private long groupID;
    private long teacherID;
    private TabLayout tabLayout;
    private String weekType;
    private boolean openCurrentDay;

    public ViewPagerAdapter(@NonNull Fragment fragment, UserType userType, int facultyID, long groupID, long teacherID,
                            TabLayout tabLayout, String weekType, boolean openCurrentDay) {
        super(fragment);
        this.userType = userType;
        this.facultyID = facultyID;
        this.groupID = groupID;
        this.teacherID = teacherID;
        this.tabLayout = tabLayout;
        this.weekType = weekType;
        this.openCurrentDay = openCurrentDay;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (!openCurrentDay) {
            openCurrentDay = true;
            int currentDayID = LocalDate.now().getDayOfWeek() == DayOfWeek.SUNDAY
                    ? 0 : LocalDate.now().getDayOfWeek().getValue() - 1;
            tabLayout.getTabAt(currentDayID).select();
        }

        if (weekType.equals("Числитель"))
            return ScheduleFragment.newInstance(userType, facultyID, groupID, teacherID, position + 1);
        else
            return ScheduleFragment.newInstance(userType, facultyID, groupID, teacherID, position + 7);
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
