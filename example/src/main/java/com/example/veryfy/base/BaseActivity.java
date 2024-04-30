package com.example.veryfy.base;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.example.veryfy.dialog.VeryfyLoadingDialog;
import com.ionnex.veryfy.base.BaseListener;

public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity implements BaseListener {
    private VeryfyLoadingDialog loadingDialog;
    private VB viewBinding;

    @Override
    protected void onStart() {
        super.onStart();

        loadingDialog = new VeryfyLoadingDialog(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loadingDialog.cancel();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding = setViewBinding();
        setContentView(viewBinding.getRoot());
        setupView();
    }

    @Override
    public void onLoadingShow() {
        loadingDialog.show();
    }

    @Override
    public void onLoadingDismiss() {
        loadingDialog.hide();
    }

    @Override
    public void onShowMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    protected abstract void setupView();

    protected abstract VB setViewBinding();
}
