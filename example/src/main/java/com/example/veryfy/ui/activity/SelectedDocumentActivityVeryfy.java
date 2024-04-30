package com.example.veryfy.ui.activity;

import static com.ionnex.veryfy.constants.Constant.DOCUMENT_MODEL;
import static com.ionnex.veryfy.constants.Constant.DOCUMENT_TYPE;
import static com.ionnex.veryfy.constants.Constant.FR_MODEL;
import static com.ionnex.veryfy.constants.Constant.FR_REQUEST;
import static com.ionnex.veryfy.constants.Constant.FR_RESULT;
import static com.ionnex.veryfy.constants.Constant.OCR_REQUEST;
import static com.ionnex.veryfy.constants.Constant.OCR_RESULT;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewbinding.ViewBinding;

import com.blankj.utilcode.util.UriUtils;
import com.example.veryfy.R;
import com.example.veryfy.base.BaseActivity;
import com.example.veryfy.databinding.ActivitySelectedDocumentBinding;
import com.example.veryfy.ui.fragment.FRRequestFragment;
import com.example.veryfy.ui.fragment.FRResultFragment;
import com.example.veryfy.ui.fragment.OCRRequestFragment;
import com.example.veryfy.ui.fragment.OCRResultFragment;
import com.ionnex.veryfy.constants.Constant;
import com.ionnex.veryfy.listener.FRListener;
import com.ionnex.veryfy.listener.OCRListener;
import com.ionnex.veryfy.model.DocumentModel;
import com.ionnex.veryfy.model.FRModel;
import com.ionnex.veryfy.presenter.FRPresenter;
import com.ionnex.veryfy.presenter.OCRPresenter;

import java.io.File;

public class SelectedDocumentActivityVeryfy extends BaseActivity implements
        OCRListener,
        FRListener,
        OCRRequestFragment.OCRFragmentListener,
        OCRResultFragment.OCRResultFragmentListener,
        FRRequestFragment.FRFragmentListener,
        FRResultFragment.FRResultFragmentListener {
    private OCRPresenter ocrPresenter;
    private FRPresenter frPresenter;
    private String referenceId;
    private String documentType;
    private DocumentModel documentModel;
    private FRModel frModel;
    File frontFile, frontFlashFile, backFile, selfieFile;
    private ActivitySelectedDocumentBinding binding;

    @Override
    protected void onStart() {
        super.onStart();

        ocrPresenter = new OCRPresenter(this,this);
        frPresenter = new FRPresenter(this, this);
    }

    private Fragment getFragment() {
        Bundle bundle = new Bundle();
        switch (documentType) {
            case OCR_REQUEST:
                onPhase1();
                return OCRRequestFragment.newInstance(bundle);
            case OCR_RESULT:
                onPhase2();
                bundle.putInt("RETRY_COUNT", ocrPresenter.getRetryCount());
                bundle.putParcelable(DOCUMENT_MODEL, documentModel);
                return OCRResultFragment.newInstance(bundle);
            case FR_REQUEST:
                onPhase1();
                return FRRequestFragment.newInstance(bundle);
            case FR_RESULT:
                onPhase2();
                bundle.putInt("RETRY_COUNT", frPresenter.getRetryCount());
                bundle.putString("SELFIE_IMAGE", UriUtils.file2Uri(selfieFile).toString());
                bundle.putParcelable(FR_MODEL, frModel);
                return FRResultFragment.newInstance(bundle);
            default:
                return OCRRequestFragment.newInstance(bundle);
        }
    }

    private void onMoveFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.frame_layout, getFragment());
        fragmentTransaction.addToBackStack(getClass().getName());
        fragmentTransaction.commit();
    }

    @Override
    protected void setupView() {
        if (getIntent() != null) {
            referenceId = getIntent().getStringExtra(Constant.REFERENCE_ID);
            documentType = getIntent().getStringExtra(DOCUMENT_TYPE);
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        });

        binding.logoutButton.setOnClickListener(v-> {
            Intent newIntent = new Intent(this, JourneyIdActivityVeryfy.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(newIntent);
        });
        onMoveFragment();
    }

    @Override
    protected ViewBinding setViewBinding() {
        binding = ActivitySelectedDocumentBinding.inflate(getLayoutInflater());
        return binding;
    }

    @Override
    public void getOCRResult(File frontFile, File frontFlashFile, File backFile) {
        this.frontFile = frontFile;
        this.frontFlashFile = frontFlashFile;
        this.backFile = backFile;
        ocrPresenter.getOCRResult(referenceId, frontFile, frontFlashFile, backFile);
    }

    @Override
    public void onRetryOCR() {
        ocrPresenter.increaseCount();
        documentType = OCR_REQUEST;
        frontFile = null;
        backFile = null;
        frontFlashFile = null;
        onMoveFragment();
    }

    public void onPhase2() {
        binding.ll1.setBackground(ContextCompat.getDrawable(this, R.drawable.circle_green_bg));
        binding.ll2.setBackground(ContextCompat.getDrawable(this, R.drawable.circle_white_bg));
    }

    public void onPhase1() {
        binding.ll1.setBackground(ContextCompat.getDrawable(this, R.drawable.circle_white_bg));
        binding.ll2.setBackground(ContextCompat.getDrawable(this, R.drawable.circle_gray_bg));
    }

    @Override
    public void onRetryFR() {
        frPresenter.increaseCount();
        documentType = FR_REQUEST;
        selfieFile = null;
        onMoveFragment();
    }

    @Override
    public void onTryAgainFR() {
        frPresenter.increaseCount();
        documentType = FR_REQUEST;
        selfieFile = null;
        onMoveFragment();
    }

    @Override
    public void onDone() {
        finish();
    }

    @Override
    public void onOCRResult(DocumentModel documentModel) {
        documentType = OCR_RESULT;
        this.documentModel = documentModel;
        onMoveFragment();
    }

    @Override
    public void onGetFRResult(File selfieFile) {
        this.selfieFile = selfieFile;
        frPresenter.getFRResult(referenceId, selfieFile);
    }

    @Override
    public void onFRResult(FRModel frModel) {
        documentType = FR_RESULT;
        this.frModel = frModel;
        onMoveFragment();
    }
}