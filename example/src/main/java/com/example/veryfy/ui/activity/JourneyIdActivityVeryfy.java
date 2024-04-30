package com.example.veryfy.ui.activity;

import android.content.Intent;

import androidx.viewbinding.ViewBinding;

import com.example.veryfy.base.BaseActivity;
import com.example.veryfy.databinding.ActivityJourneyIdBinding;
import com.example.veryfy.dialog.VeryfyLoadingDialog;
import com.ionnex.veryfy.constants.Constant;
import com.ionnex.veryfy.listener.JourneyIdListener;
import com.ionnex.veryfy.model.JourneyIdModel;
import com.ionnex.veryfy.presenter.JourneyIdPresenter;

public class JourneyIdActivityVeryfy extends BaseActivity implements JourneyIdListener {
    private JourneyIdPresenter presenter;
    private ActivityJourneyIdBinding binding;
    private VeryfyLoadingDialog loadingDialog;

    @Override
    protected void onStart() {
        super.onStart();

        loadingDialog = new VeryfyLoadingDialog(this);
        presenter = new JourneyIdPresenter(this,this);
    }

    @Override
    protected void onDestroy() {
        loadingDialog.cancel();
        super.onDestroy();
    }

    @Override
    protected void setupView() {
        binding.btnStart.setOnClickListener(view -> presenter.getJourneyId());
    }

    @Override
    protected ViewBinding setViewBinding() {
        binding = ActivityJourneyIdBinding.inflate(getLayoutInflater());
        return binding;
    }

    @Override
    public void onJourneyIdResult(JourneyIdModel journeyIdModel) {
        Intent intent = new Intent(this, DocumentTypeActivityVeryfy.class);
        intent.putExtra(Constant.REFERENCE_ID, journeyIdModel.getReferenceId());
        startActivity(intent);
    }
}