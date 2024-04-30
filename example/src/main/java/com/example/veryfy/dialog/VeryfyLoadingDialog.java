package com.example.veryfy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.veryfy.databinding.DialogVeryfyLoadingBinding;
import com.ionnex.veryfy.R;

public class VeryfyLoadingDialog extends Dialog {
    DialogVeryfyLoadingBinding binding;

    public VeryfyLoadingDialog(@NonNull Context context) {
        super(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DialogVeryfyLoadingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Glide.with(getContext())
                .asGif()
                .load(R.drawable.veryfy_loading)
                .into(binding.ivGif);
        setCancelable(false);
    }
}

