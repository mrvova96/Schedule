package com.example.schedule.ui.lesson;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.schedule.R;
import com.example.schedule.data.database.entity.Lesson;
import com.example.schedule.databinding.FragmentLessonAddBinding;
import com.example.schedule.databinding.FragmentMainBinding;
import com.example.schedule.presentation.lesson.LessonAddViewModel;
import com.example.schedule.ui.main.MainFragment;
import com.google.android.material.snackbar.Snackbar;

public class LessonAddFragment extends Fragment {

    private static final String FACULTY_ID = "facultyID";

    private int facultyID;

    private FragmentLessonAddBinding binding;
    private FragmentMainBinding parentBinding;
    private LessonAddViewModel viewModel;
    private Snackbar snackbar;

    public static LessonAddFragment newInstance(int facultyID) {
        LessonAddFragment fragment = new LessonAddFragment();
        Bundle args = new Bundle();
        args.putInt(FACULTY_ID, facultyID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLessonAddBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        if (getParentFragment() != null)
            parentBinding = ((MainFragment) getParentFragment()).getBinding();

        viewModel = new ViewModelProvider(this).get(LessonAddViewModel.class);

        if (getArguments() != null) {
            facultyID = getArguments().getInt(FACULTY_ID);
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.types_of_lessons, R.layout.item_spinner);
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        binding.spinnerAddLessonType.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnBackPressed();

        viewModel.isAdded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isAdded) {
                if (isAdded) {
                    parentBinding.imageButtonAdd.setVisibility(View.VISIBLE);
                    getParentFragmentManager().popBackStack();
                    showMessage("Запись успешно добавлена");
                } else
                    showMessage("Предмет уже содержится для данного факультета");
            }
        });

        binding.buttonAddLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataIsCorrect()) {
                    saveLesson();
                }
            }
        });
    }

    private void saveLesson() {
        String lessonName = binding.editTextAddLesson.getText().toString().trim();
        String lessonType = binding.spinnerAddLessonType.getSelectedItem().toString();
        viewModel.add(new Lesson(lessonName, lessonType, facultyID));
    }

    private boolean dataIsCorrect() {
        if (binding.editTextAddLesson.getText().toString().isBlank()) {
            showMessage("Пожалуйста, заполните все поля");
            return false;
        }
        return true;
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