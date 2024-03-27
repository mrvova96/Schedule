package com.example.schedule.ui.teacher;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.schedule.R;
import com.example.schedule.adapter.recyclerView.TeacherLessonsListAdapter;
import com.example.schedule.data.database.entity.Lesson;
import com.example.schedule.databinding.FragmentMainBinding;
import com.example.schedule.databinding.FragmentTeacherLessonsAddBinding;
import com.example.schedule.presentation.teacher.TeacherLessonsAddViewModel;
import com.example.schedule.ui.main.MainFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class TeacherLessonsAddFragment extends Fragment {

    private static final String FACULTY_ID = "facultyID";
    private static final String TEACHER_ID = "teacherID";

    private int facultyID;
    private long teacherID;

    private FragmentTeacherLessonsAddBinding binding;
    private FragmentMainBinding parentBinding;
    private TeacherLessonsAddViewModel viewModel;
    private TeacherLessonsListAdapter teacherLessonsListAdapter;
    private Snackbar snackbar;

    public static TeacherLessonsAddFragment newInstance(int facultyID, long teacherID) {
        TeacherLessonsAddFragment fragment = new TeacherLessonsAddFragment();
        Bundle args = new Bundle();
        args.putInt(FACULTY_ID, facultyID);
        args.putLong(TEACHER_ID, teacherID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshLessons(facultyID);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTeacherLessonsAddBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(TeacherLessonsAddViewModel.class);

        if (getParentFragment() != null)
            parentBinding = ((MainFragment) getParentFragment()).getBinding();

        if (getArguments() != null) {
            facultyID = getArguments().getInt(FACULTY_ID);
            teacherID = getArguments().getLong(TEACHER_ID);
        }

        initAdapter();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnBackPressed();

        viewModel.getLessons().observe(getViewLifecycleOwner(), new Observer<List<Lesson>>() {
            @Override
            public void onChanged(List<Lesson> lessons) {
                if (lessons.isEmpty())
                    binding.textViewEmptyList.setVisibility(View.VISIBLE);
                else
                    binding.textViewEmptyList.setVisibility(View.INVISIBLE);
                teacherLessonsListAdapter.setLessons(lessons);
            }
        });

        viewModel.isAdded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isAdded) {
                if (isAdded) {
                    showMessage("Запись успешно добавлена");
                    parentBinding.imageButtonAdd.setVisibility(View.VISIBLE);
                    getParentFragmentManager().popBackStack();
                } else
                    showMessage("Предмет уже содержится в списке");
            }
        });

        teacherLessonsListAdapter.setOnLessonClickListener(new TeacherLessonsListAdapter.OnLessonClickListener() {
            @Override
            public void onLessonClick(Lesson lesson) {
                new AlertDialog.Builder(requireContext())
                        .setMessage("Добавить выбранный предмет?")
                        .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.add(teacherID, lesson.getID());
                            }
                        })
                        .setNegativeButton("Назад", null)
                        .show();
            }
        });
    }

    private void initAdapter() {
        teacherLessonsListAdapter = new TeacherLessonsListAdapter();
        binding.recyclerViewTeacherLessonsAdd.setAdapter(teacherLessonsListAdapter);
        binding.recyclerViewTeacherLessonsAdd.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setOnBackPressed() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (snackbar != null)
                    snackbar.dismiss();
                parentBinding.imageButtonAdd.setVisibility(View.VISIBLE);
                getParentFragmentManager().popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private void showMessage(String message) {
        snackbar = Snackbar.make(requireView(), message, 3000);
        snackbar.setBackgroundTint(getResources().getColor(R.color.pink, null));
        snackbar.setActionTextColor(getResources().getColor(R.color.black, null));
        TextView snackbarText = snackbar.getView()
                .findViewById(com.google.android.material.R.id.snackbar_text);
        snackbarText.setTextSize(16);
        snackbarText.setTextColor(getResources().getColor(R.color.black, null));
        snackbar.setAction(R.string.snackbar_action, v -> snackbar.dismiss());
        snackbar.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        parentBinding = null;
    }
}