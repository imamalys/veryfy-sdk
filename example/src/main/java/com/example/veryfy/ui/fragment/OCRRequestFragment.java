package com.example.veryfy.ui.fragment;

import static android.app.Activity.RESULT_OK;
import static com.ionnex.veryfy.constants.Constant.PERMISSION_CODE;
import static com.ionnex.veryfy.constants.Constant.REQUEST_IMAGE_IS_BACK;
import static com.ionnex.veryfy.constants.Constant.REQUEST_IMAGE_IS_FRONT;
import static com.ionnex.veryfy.constants.Constant.REQUEST_IMAGE_IS_FRONT_FLASH;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.veryfy.R;
import com.example.veryfy.databinding.FragmentOcrRequestBinding;
import com.example.veryfy.dialog.AskMoreImageDialog;
import com.ionnex.veryfy.ui.CameraActivityVeryfy;

import java.io.File;

public class OCRRequestFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private FragmentOcrRequestBinding binding;
    private int selectCaptureId;
    OCRFragmentListener ocrFragmentListener;
    private boolean isFlash = false;
    private String idPassport;
    private File frontFile;
    private File frontFlashFile;
    private File backFile;

    public OCRRequestFragment() {
        // Required empty public constructor
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
        binding = FragmentOcrRequestBinding.inflate(getLayoutInflater());

        binding.btnFront.setOnClickListener(v-> {
            selectCaptureId = REQUEST_IMAGE_IS_FRONT;
            checkPermission();
        });

        binding.btnFrontFlash.setOnClickListener(v-> {
            selectCaptureId = REQUEST_IMAGE_IS_FRONT_FLASH;
            checkPermission();
        });

        binding.btnBack.setOnClickListener(v-> {
            selectCaptureId = REQUEST_IMAGE_IS_BACK;
            checkPermission();
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.document_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        binding.documentSpinner.setAdapter(adapter);
        binding.documentSpinner.setOnItemSelectedListener(this);

        return  binding.getRoot();
    }

    ActivityResultLauncher<Intent> mIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (selectCaptureId == REQUEST_IMAGE_IS_FRONT && result.getResultCode() == RESULT_OK){
                    frontFile = (File) result.getData().getExtras().get("file");
                    Glide.with(requireContext()).load(frontFile).into(binding.captureFront);
                    String selectedItem = idPassport;
                    if (!isFlash) {
                        if (selectedItem.equals("Passport")) {
                            ocrFragmentListener.getOCRResult(frontFile, frontFlashFile, backFile);
                        } else {
                            new AskMoreImageDialog( (dialog, which) -> {
                                ocrFragmentListener.getOCRResult(frontFile, frontFlashFile, backFile);
                                dialog.dismiss();
                            }, (dialog, which) -> {
                                binding.captureBack.setVisibility(View.VISIBLE);
                                binding.btnBack.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                            } ).show( getParentFragmentManager(),"");
                        }
                    } else {
                        if (frontFlashFile != null)  {
                            new AskMoreImageDialog( (dialog, which) -> {
                                ocrFragmentListener.getOCRResult(frontFile, frontFlashFile, backFile);
                                dialog.dismiss();
                            }, (dialog, which) -> {
                                binding.captureBack.setVisibility(View.VISIBLE);
                                binding.btnBack.setVisibility(View.VISIBLE);
                                dialog.dismiss();
                            } ).show( getParentFragmentManager(),"");
                        }
                    }
                } else if (selectCaptureId == REQUEST_IMAGE_IS_FRONT_FLASH && result.getResultCode() == RESULT_OK) {
                    frontFlashFile = (File) result.getData().getExtras().get("file");
                    Glide.with(requireContext()).load(frontFlashFile).into(binding.captureFrontFlash);
                    if (frontFile != null) {
                        new AskMoreImageDialog((dialog, which) -> {
                            ocrFragmentListener.getOCRResult(frontFile, frontFlashFile, backFile);
                            dialog.dismiss();
                        }, (dialog, which) -> {
                            binding.captureBack.setVisibility(View.VISIBLE);
                            binding.btnBack.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                        }).show(getParentFragmentManager(),"");
                    }
                } else if (selectCaptureId == REQUEST_IMAGE_IS_BACK && result.getResultCode() == RESULT_OK) {
                    backFile = (File) result.getData().getExtras().get("file");
                    Glide.with(requireContext()).load(backFile).into(binding.captureBack);
                    ocrFragmentListener.getOCRResult(frontFile, frontFlashFile, backFile);
                }
            });

    public static OCRRequestFragment newInstance(Bundle bundle) {
        OCRRequestFragment fragment = new OCRRequestFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
            } else {
                checkRequestSelected();
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSION_CODE);
            } else {
                checkRequestSelected();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    checkRequestSelected();
                } else {
                    checkPermission();
                }
                break;
        }
    }


    private void checkRequestSelected(){
        Intent newIntent = new Intent(requireActivity(), CameraActivityVeryfy.class);
        newIntent.putExtra("flash", selectCaptureId == REQUEST_IMAGE_IS_FRONT_FLASH);
        newIntent.putExtra("selfie", false);
        mIntent.launch(newIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ocrFragmentListener = (OCRFragmentListener) context;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        idPassport = selectedItem;
        if (parent.getItemAtPosition(position).toString().equals("Document Type")) {
            binding.llLayout.setVisibility(View.GONE);
        }
        else if (parent.getItemAtPosition(position).toString().equals("National ID") ||
                parent.getItemAtPosition(position).toString().equals("Passport") ||
                parent.getItemAtPosition(position).toString().equals("PRC") ||
                parent.getItemAtPosition(position).toString().equals("Voter ID") ||
                parent.getItemAtPosition(position).toString().equals("Postal ID"))
        {
            binding.captureFrontFlash.setVisibility(View.GONE);
            binding.btnFrontFlash.setVisibility(View.GONE);
            binding.llAttention.setVisibility(View.GONE);
            binding.llLayout.setVisibility(View.VISIBLE);
            isFlash = false;
        }
        else {
            binding.captureFrontFlash.setVisibility(View.VISIBLE);
            binding.btnFrontFlash.setVisibility(View.VISIBLE);
            binding.llAttention.setVisibility(View.VISIBLE);
            binding.llLayout.setVisibility(View.VISIBLE);
            isFlash = true;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface OCRFragmentListener {
        void getOCRResult(File frontFile, File frontFlashFile, File backFile);
    }
}
