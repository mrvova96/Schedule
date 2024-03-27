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
import com.example.schedule.databinding.FragmentLessonUpdateBinding;
import com.example.schedule.databinding.FragmentMainBinding;
import com.example.schedule.presentation.lesson.LessonUpdateViewModel;
import com.example.schedule.ui.main.MainFragment;
import com.google.android.material.snackbar.Snackbar;

public class LessonUpdateFragment extends Fragment {

    private static final String LESSON_ID = "lessonID";
    private static final String LESSON_NAME = "lessonName";
    private static final String LESSON_TYPE = "lessonType";
    private static final String FACULTY_ID = "facultyID";

    private int lessonID;
    private String lessonName;
    private String lessonType;
    private int facultyID;

    private FragmentLessonUpdateBinding binding;
    private FragmentMainBinding parentBinding;
    private LessonUpdateViewModel viewModel;
    private Snackbar snackbar;

    public static LessonUpdateFragment newInstance(int lessonID, String lessonName, String lessonType, int facultyID) {
        LessonUpdateFragment fragment = new LessonUpdateFragment();
        Bundle args = new Bundle();
        args.putInt(LESSON_ID, lessonID);
        args.putString(LESSON_NAME, lessonName);
        args.putString(LESSON_TYPE, lessonType);
        args.putInt(FACULTY_ID, facultyID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLessonUpdateBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(LessonUpdateViewModel.class);

        if (getParentFragment() != null)
            parentBinding = ((MainFragment) getParentFragment()).getBinding();

        if (getArguments() != null) {
            lessonID = getArguments().getInt(LESSON_ID);
            lessonName = getArguments().getString(LESSON_NAME);
            lessonType = getArguments().getString(LESSON_TYPE);
            facultyID = getArguments().getInt(FACULTY_ID);
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.types_of_lessons, R.layout.item_spinner);
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        binding.spinnerUpdateLessonType.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnBackPressed();

        binding.editTextUpdateLesson.setText(lessonName);
        for (int i = 0; i < getResources().getStringArray(R.array.types_of_lessons).length; i++) {
            if (lessonType.equals(getResources().getStringArray(R.array.types_of_lessons)[i]))
                binding.spinnerUpdateLessonType.setSelection(i);
        }

        viewModel.isUpdated().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isUpdated) {
                if (isUpdated) {
                    parentBinding.imageButtonAdd.setVisibility(View.VISIBLE);
                    getParentFragmentManager().popBackStack();
                    showMessage("Запись успешно обновлена");
                } else
                    showMessage("Предмет уже содержится для данного факультета");
            }
        });

        binding.buttonUpdateLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataIsCorrect()) {
                    updateLesson();
                }
            }
        });
    }

    private void updateLesson() {
        lessonName = binding.editTextUpdateLesson.getText().toString().trim();
        lessonType = binding.spinnerUpdateLessonType.getSelectedItem().toString();
        viewModel.update(new Lesson(lessonID, lessonName, lessonType, facultyID));
    }

    private boolean dataIsCorrect() {
        if (binding.editTextUpdateLesson.getText().toString().isBlank()) {
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