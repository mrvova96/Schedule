package com.example.schedule.ui.teacher;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.schedule.R;
import com.example.schedule.adapter.recyclerView.TeacherEditAdapter;
import com.example.schedule.data.database.entity.Teacher;
import com.example.schedule.databinding.FragmentMainBinding;
import com.example.schedule.databinding.FragmentTeacherBinding;
import com.example.schedule.presentation.teacher.TeacherViewModel;
import com.example.schedule.ui.main.MainFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class TeacherFragment extends Fragment {

    private static final String FACULTY_ID = "facultyID";

    private int facultyID;

    private FragmentTeacherBinding binding;
    private FragmentMainBinding parentBinding;
    private TeacherViewModel viewModel;
    private TeacherEditAdapter teacherEditAdapter;
    private Snackbar snackbar;

    public static TeacherFragment newInstance(int facultyID) {
        TeacherFragment fragment = new TeacherFragment();
        Bundle args = new Bundle();
        args.putInt(FACULTY_ID, facultyID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshTeachers(facultyID);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTeacherBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(TeacherViewModel.class);

        if (getParentFragment() != null)
            parentBinding = ((MainFragment) getParentFragment()).getBinding();

        if (getArguments() != null) {
            facultyID = getArguments().getInt(FACULTY_ID);
        }

        initAdapter();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnBackPressed();

        viewModel.getTeachers().observe(getViewLifecycleOwner(), new Observer<List<Teacher>>() {
            @Override
            public void onChanged(List<Teacher> teachers) {
                if (teachers.isEmpty())
                    binding.textViewEmptyList.setVisibility(View.VISIBLE);
                else
                    binding.textViewEmptyList.setVisibility(View.INVISIBLE);
                teacherEditAdapter.setTeachers(teachers);
            }
        });

        parentBinding.imageButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (snackbar != null)
                    snackbar.dismiss();
                parentBinding.imageButtonAdd.setVisibility(View.INVISIBLE);
                launchNextFragment(TeacherAddFragment.newInstance(facultyID));
            }
        });

        teacherEditAdapter.setOnUpdateClickListener(new TeacherEditAdapter.OnUpdateClickListener() {
            @Override
            public void onUpdateClick(Teacher teacher) {
                if (snackbar != null)
                    snackbar.dismiss();
                parentBinding.imageButtonAdd.setVisibility(View.INVISIBLE);
                launchNextFragment(TeacherUpdateFragment.newInstance(
                        teacher.getID(), teacher.getSurname(), teacher.getName(),
                        teacher.getPatronymic(), teacher.getFacultyID()));
            }
        });

        teacherEditAdapter.setOnDeleteClickListener(new TeacherEditAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(Teacher teacher) {
                new AlertDialog.Builder(requireContext())
                        .setMessage("Также будут удалены все связанные записи, продолжить?")
                        .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.remove(teacher, facultyID);
                                showMessage("Запись успешно удалена");
                            }
                        })
                        .setNegativeButton("Назад", null)
                        .show();
            }
        });
    }

    private void initAdapter() {
        teacherEditAdapter = new TeacherEditAdapter();
        binding.recyclerViewTeacher.setAdapter(teacherEditAdapter);
        binding.recyclerViewTeacher.setLayoutManager(new LinearLayoutManager(getContext()));
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