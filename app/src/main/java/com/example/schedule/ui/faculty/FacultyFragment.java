package com.example.schedule.ui.faculty;

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
import com.example.schedule.adapter.recyclerView.FacultyEditAdapter;
import com.example.schedule.data.database.entity.Faculty;
import com.example.schedule.databinding.FragmentFacultyBinding;
import com.example.schedule.databinding.FragmentMainBinding;
import com.example.schedule.presentation.faculty.FacultyViewModel;
import com.example.schedule.ui.main.MainFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class FacultyFragment extends Fragment {

    private FragmentFacultyBinding binding;
    private FragmentMainBinding parentBinding;
    private FacultyViewModel viewModel;
    private FacultyEditAdapter facultyEditAdapter;
    private Snackbar snackbar;

    public static FacultyFragment newInstance() {
        return new FacultyFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshFaculties();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFacultyBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        if (getParentFragment() != null)
            parentBinding = ((MainFragment) getParentFragment()).getBinding();

        viewModel = new ViewModelProvider(this).get(FacultyViewModel.class);
        initAdapter();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnBackPressed();

        viewModel.getFaculties().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(List<Faculty> faculties) {
                if (faculties.isEmpty())
                    binding.textViewEmptyList.setVisibility(View.VISIBLE);
                else
                    binding.textViewEmptyList.setVisibility(View.INVISIBLE);
                facultyEditAdapter.setFaculties(faculties);
            }
        });

        parentBinding.imageButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (snackbar != null)
                    snackbar.dismiss();
                parentBinding.textViewAdminPanel.setText("Факультеты");
                parentBinding.imageButtonAdd.setVisibility(View.INVISIBLE);
                launchNextFragment(FacultyAddFragment.newInstance());
            }
        });

        facultyEditAdapter.setOnUpdateClickListener(new FacultyEditAdapter.OnUpdateClickListener() {
            @Override
            public void onUpdateClick(Faculty faculty) {
                if (snackbar != null)
                    snackbar.dismiss();
                parentBinding.textViewAdminPanel.setText("Факультеты");
                parentBinding.imageButtonAdd.setVisibility(View.INVISIBLE);
                launchNextFragment(FacultyUpdateFragment.newInstance(faculty.getID(), faculty.getFacultyName()));
            }
        });

        facultyEditAdapter.setOnDeleteClickListener(new FacultyEditAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(Faculty faculty) {
                new AlertDialog.Builder(requireContext())
                        .setMessage("Также будут удалены все связанные записи, продолжить?")
                        .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.remove(faculty);
                                showMessage("Запись успешно удалена");
                            }
                        })
                        .setNegativeButton("Назад", null)
                        .show();
            }
        });
    }

    private void initAdapter() {
        facultyEditAdapter = new FacultyEditAdapter();
        binding.recyclerViewFaculty.setAdapter(facultyEditAdapter);
        binding.recyclerViewFaculty.setLayoutManager(new LinearLayoutManager(getContext()));
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
                parentBinding.textViewAdminPanel.setText("Панель администратора");
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