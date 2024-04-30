package com.example.veryfy.ui.fragment;

import static com.ionnex.veryfy.constants.Constant.*;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.veryfy.adapter.OCRResultAdapter;
import com.example.veryfy.databinding.FragmentOcrResultBinding;
import com.ionnex.veryfy.model.DocumentModel;

public class OCRResultFragment extends Fragment {
    private DocumentModel documentModel;
    OCRResultAdapter adapter;
    OCRResultFragmentListener ocrResultFragmentListener;
    int retryCount = 0;
    public OCRResultFragment(){
        // Required empty public constructor
    }

    public static OCRResultFragment newInstance(Bundle bundle) {
        OCRResultFragment fragment = new OCRResultFragment();
        fragment.setArguments( bundle );
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentOcrResultBinding binding = FragmentOcrResultBinding.inflate(getLayoutInflater());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity() );
        binding.recyclerview.setLayoutManager(linearLayoutManager);
        if (getArguments() != null) {
            documentModel = getArguments().getParcelable(DOCUMENT_MODEL);
            retryCount = getArguments().getInt(RETRY_COUNT);
        }

        if (retryCount == 2) {
            binding.btnRetry.setEnabled(false);
        }

        adapter = new OCRResultAdapter(requireActivity(), documentModel.getResult());
        binding.recyclerview.setAdapter(adapter);
        binding.tvValue.setText(documentModel.getDocumentType());

        binding.btnDone.setOnClickListener(v-> ocrResultFragmentListener.onDone());

        binding.btnRetry.setOnClickListener(v -> {
            ocrResultFragmentListener.onRetryOCR();
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ocrResultFragmentListener = (OCRResultFragmentListener) context;
    }

    public interface OCRResultFragmentListener {
        void onRetryOCR();
        void onDone();
    }
}
