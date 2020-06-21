package com.e.shelter.settings;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.e.shelter.R;
import com.e.shelter.validation.PasswordValidator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ConfirmPasswordDialog extends DialogFragment {

    private static final String TAG = "ConfirmPasswordDialog";

    public interface OnConfirmPasswordListener {
        public void onConfirmPassword(String password);
    }
    OnConfirmPasswordListener onConfirmPasswordListener;
    private TextInputEditText passwordEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm_password, container, false);
        Log.d("TAG", "onCreateView: started");

        passwordEditText = view.findViewById(R.id.password_text_input_dialog);

        MaterialButton confirmMaterialButton = view.findViewById(R.id.cancel_dialog_confirm_password);
        confirmMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: confirm");
                if (PasswordValidator.isValidEmailTextInputEditText(passwordEditText.getText().toString(), passwordEditText)) {
                    onConfirmPasswordListener.onConfirmPassword(passwordEditText.getText().toString());
                    getDialog().dismiss();
                }

            }
        });

        MaterialButton cancelMaterialButton = view.findViewById(R.id.cancel_dialog_confirm_password);
        cancelMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing dialog");
                getDialog().dismiss();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onConfirmPasswordListener = (OnConfirmPasswordListener) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach", e);
        }
    }


}
