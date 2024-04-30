package com.example.veryfy.ui.activity;

import static com.ionnex.veryfy.constants.Constant.DOCUMENT_TYPE;
import static com.ionnex.veryfy.constants.Constant.FR_REQUEST;
import static com.ionnex.veryfy.constants.Constant.OCR_REQUEST;
import static com.ionnex.veryfy.constants.Constant.REFERENCE_ID;

import android.content.Intent;

import androidx.viewbinding.ViewBinding;

import com.example.veryfy.base.BaseActivity;
import com.example.veryfy.databinding.ActivityDocumentTypeBinding;

public class DocumentTypeActivityVeryfy extends BaseActivity {

    private ActivityDocumentTypeBinding binding;
    private String referenceId;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void setupView() {
        if (getIntent() != null) {
            referenceId = getIntent().getStringExtra(REFERENCE_ID);
        }

        binding.btnOcr.setOnClickListener(v-> {
            Intent intent = new Intent(this, SelectedDocumentActivityVeryfy.class);
            intent.putExtra(REFERENCE_ID, referenceId);
            intent.putExtra(DOCUMENT_TYPE, OCR_REQUEST);
            startActivity(intent);
        });

        binding.btnFr.setOnClickListener(v-> {
            Intent intent = new Intent(this, SelectedDocumentActivityVeryfy.class);
            intent.putExtra(REFERENCE_ID, referenceId);
            intent.putExtra(DOCUMENT_TYPE, FR_REQUEST);
            startActivity(intent);
        });
    }

    @Override
    protected ViewBinding setViewBinding() {
        binding = ActivityDocumentTypeBinding.inflate(getLayoutInflater());
        return binding;
    }
}