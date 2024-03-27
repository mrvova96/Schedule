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
import com.example.schedule.adapter.recyclerView.TeacherLessonsEditAdapter;
import com.example.schedule.data.database.entity.Lesson;
import com.example.schedule.databinding.FragmentMainBinding;
import com.example.schedule.databinding.FragmentTeacherLessonsBinding;
import com.example.schedule.presentation.teacher.TeacherLessonsViewModel;
import com.example.schedule.ui.main.MainFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.Comparator;
import java.util.List;

public class TeacherLessonsFragment extends Fragment {

    private static final String FACULTY_ID = "facultyID";
    private static final String TEACHER_ID = "teacherID";

    private int facultyID;
    private long teacherID;

    private FragmentTeacherLessonsBinding binding;
    private FragmentMainBinding parentBinding;
    private TeacherLessonsViewModel viewModel;
    private TeacherLessonsEditAdapter teacherLessonsEditAdapter;
    private Snackbar snackbar;

    public static TeacherLessonsFragment newInstance(int facultyID, long teacherID) {
        TeacherLessonsFragment fragment = new TeacherLessonsFragment();
        Bundle args = new Bundle();
        args.putInt(FACULTY_ID, facultyID);
        args.putLong(TEACHER_ID, teacherID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshLessons(teacherID);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTeacherLessonsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(TeacherLessonsViewModel.class);

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
                else {
                    binding.textViewEmptyList.setVisibility(View.INVISIBLE);
                    lessons.sort(new Comparator<Lesson>() {
                        @Override
                        public int compare(Lesson lesson1, Lesson lesson2) {
                            return lesson1.getLessonName().compareToIgnoreCase(lesson2.getLessonName());
                        }
                    });
                }
                teacherLessonsEditAdapter.setLessons(lessons);
            }
        });

        parentBinding.imageButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (snackbar != null)
                    snackbar.dismiss();
                parentBinding.imageButtonAdd.setVisibility(View.INVISIBLE);
                launchNextFragment(TeacherLessonsAddFragment.newInstance(facultyID, teacherID));
            }
        });

        teacherLessonsEditAdapter.setOnDeleteClickListener(new TeacherLessonsEditAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(Lesson lesson) {
                new AlertDialog.Builder(requireContext())
                        .setMessage("Удалить данную запись?")
                        .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.remove(teacherID, lesson);
                                showMessage("Запись успешно удалена");
                            }
                        })
                        .setNegativeButton("Назад", null)
                        .show();
            }
        });
    }

    private void initAdapter() {
        teacherLessonsEditAdapter = new TeacherLessonsEditAdapter();
        binding.recyclerViewTeacherLessons.setAdapter(teacherLessonsEditAdapter);
        binding.recyclerViewTeacherLessons.setLayoutManager(new LinearLayoutManager(getContext()));
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
                if (snackbar != null)
                    snackbar.dismiss();
                parentBinding.imageButtonAdd.setVisibility(View.INVISIBLE);
                parentBinding.textViewAdminPanel.setText("Выберите преподавателя");
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