package com.example.schedule.ui.classroom;

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
import com.example.schedule.adapter.recyclerView.ClassroomEditAdapter;
import com.example.schedule.data.database.entity.Classroom;
import com.example.schedule.databinding.FragmentClassroomBinding;
import com.example.schedule.databinding.FragmentMainBinding;
import com.example.schedule.presentation.classroom.ClassroomViewModel;
import com.example.schedule.ui.main.MainFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ClassroomFragment extends Fragment {

    private static final String FACULTY_ID = "facultyID";

    private int facultyID;

    private FragmentClassroomBinding binding;
    private FragmentMainBinding parentBinding;
    private ClassroomViewModel viewModel;
    private ClassroomEditAdapter classroomEditAdapter;
    private Snackbar snackbar;

    public static ClassroomFragment newInstance(int facultyID) {
        ClassroomFragment fragment = new ClassroomFragment();
        Bundle args = new Bundle();
        args.putInt(FACULTY_ID, facultyID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshClassrooms(facultyID);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentClassroomBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(ClassroomViewModel.class);

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

        viewModel.getClassrooms().observe(getViewLifecycleOwner(), new Observer<List<Classroom>>() {
            @Override
            public void onChanged(List<Classroom> classrooms) {
                if (classrooms.isEmpty())
                    binding.textViewEmptyList.setVisibility(View.VISIBLE);
                else
                    binding.textViewEmptyList.setVisibility(View.INVISIBLE);
                classroomEditAdapter.setClassrooms(classrooms);
            }
        });

        parentBinding.imageButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (snackbar != null)
                    snackbar.dismiss();
                parentBinding.imageButtonAdd.setVisibility(View.INVISIBLE);
                launchNextFragment(ClassroomAddFragment.newInstance(facultyID));
            }
        });

        classroomEditAdapter.setOnUpdateClickListener(new ClassroomEditAdapter.OnUpdateClickListener() {
            @Override
            public void onUpdateClick(Classroom classroom) {
                if (snackbar != null)
                    snackbar.dismiss();
                parentBinding.imageButtonAdd.setVisibility(View.INVISIBLE);
                launchNextFragment(ClassroomUpdateFragment.newInstance(
                        classroom.getID(), classroom.getClassroomNumber(), facultyID));
            }
        });

        classroomEditAdapter.setOnDeleteClickListener(new ClassroomEditAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(Classroom classroom) {
                new AlertDialog.Builder(requireContext())
                        .setMessage("Также будут удалены все связанные записи, продолжить?")
                        .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.remove(classroom, facultyID);
                                showMessage("Запись успешно удалена");
                            }
                        })
                        .setNegativeButton("Назад", null)
                        .show();
            }
        });
    }

    private void initAdapter() {
        classroomEditAdapter = new ClassroomEditAdapter();
        binding.recyclerViewClassroom.setAdapter(classroomEditAdapter);
        binding.recyclerViewClassroom.setLayoutManager(new LinearLayoutManager(getContext()));
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
                parentBinding.textViewAdminPanel.setText("Выберите факультет");
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