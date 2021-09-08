package com.greenwaterevents.android.greenwaterevents.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.greenwaterevents.android.greenwaterevents.R;

import java.util.Random;
import java.util.regex.Pattern;

import de.cketti.mailto.EmailIntentBuilder;

/**
 * Created by Mohamed Shiyas on 03-02-2018.
 */

public class ContactUsFragment extends Fragment implements View.OnClickListener {
    private TextInputLayout name;
    private TextInputLayout email;
    private TextInputLayout phoneNumber;
    private EditText inputName, inputEmail, inputPhoneNumber, inputMessage;

    public static ContactUsFragment newInstance() {
        return new ContactUsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        View v = inflater.inflate(R.layout.fragment_contact_us, container, false);

        name = v.findViewById(R.id.name);
        email = v.findViewById(R.id.emailID);
        phoneNumber = v.findViewById(R.id.phoneNumber);
        TextInputLayout writeMessage = v.findViewById(R.id.writeMessage);

        inputName = v.findViewById(R.id.inputName);
        inputEmail = v.findViewById(R.id.inputEmailID);
        inputPhoneNumber = v.findViewById(R.id.inputPhoneNumber);
        inputMessage = v.findViewById(R.id.inputMessage);

        CardView submitButton = v.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);

        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPhoneNumber.addTextChangedListener(new MyTextWatcher(inputPhoneNumber));
        inputMessage.addTextChangedListener(new MyTextWatcher(writeMessage));

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Contact Us");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validatePhoneNumber()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        sendEmail();
    }

    private void sendEmail() {
        EmailIntentBuilder.from(getContext())
                .to("greenwaterevents@gmail.com") //greenwaterevents@gmail.com
                .subject("Support for Green Water Events")
                .body(inputMessage.getText().toString() + "\n\nName: " + inputName.getText().toString().trim()
                        + "\nPhone Number: " + inputPhoneNumber.getText().toString()
                        + "\nMail Id: " + inputEmail.getText().toString().trim())
                .start();
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            name.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String emailString = inputEmail.getText().toString().trim();

        if (emailString.isEmpty() || !isValidEmail(emailString)) {
            inputEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            email.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validatePhoneNumber() {
        if (!isValidPhone(inputPhoneNumber.getText().toString())) {
            inputPhoneNumber.setError(getString(R.string.err_msg_phoneNumber));
            requestFocus(inputPhoneNumber);
            return false;
        } else {
            phoneNumber.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean isValidPhone(String phone) {
        boolean check;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            check = !(phone.length() < 10 || phone.length() > 10 || phone.startsWith("0"));
        } else {
            check = false;
        }
        return check;
    }

    @Override
    public void onClick(View view) {
        submitForm();
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.inputName:
                    validateName();
                    break;
                case R.id.inputEmailID:
                    validateEmail();
                    break;
                case R.id.inputPhoneNumber:
                    validatePhoneNumber();
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        System.gc();
        super.onDestroy();
    }
}
