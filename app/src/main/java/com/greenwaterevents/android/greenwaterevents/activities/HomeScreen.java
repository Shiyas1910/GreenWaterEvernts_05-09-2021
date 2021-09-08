package com.greenwaterevents.android.greenwaterevents.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.greenwaterevents.android.greenwaterevents.MyApplication;
import com.greenwaterevents.android.greenwaterevents.fragments.AboutUsFragment;
import com.greenwaterevents.android.greenwaterevents.reciever.ConnectivityReceiver;
import com.greenwaterevents.android.greenwaterevents.fragments.ContactUsFragment;
import com.greenwaterevents.android.greenwaterevents.fragments.HomeFragment;
import com.greenwaterevents.android.greenwaterevents.fragments.PhotographyFragment;
import com.greenwaterevents.android.greenwaterevents.fragments.ServicesFragment;
import com.greenwaterevents.android.greenwaterevents.fragments.VideographyFragment;
import com.greenwaterevents.android.greenwaterevents.listener.ClickListener;
import com.greenwaterevents.android.greenwaterevents.R;
import com.greenwaterevents.android.greenwaterevents.utils.MyExceptionHandler;

import java.util.regex.Pattern;

import de.cketti.mailto.EmailIntentBuilder;

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ClickListener, ConnectivityReceiver.ConnectivityReceiverListener {


    private FragmentTransaction ft;
    private NavigationView navigationView;
    private boolean doubleBackToExitPressedOnce = false;
    SparseArray<Fragment> fragmentArray = new SparseArray<Fragment>();

    TextInputLayout name, email, phoneNumber, writeMessage;
    EditText inputName, inputEmail, inputPhoneNumber, inputMessage;
    CardView submitButton;
    private static AlertDialog dialog;
    private static final String PRIVACY_POLICY_LINK = "http://greenwaterevents.com/terms";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

        fragmentArray.clear();
        fragmentArray.put(0, HomeFragment.newInstance(this));
        fragmentArray.put(1, PhotographyFragment.newInstance());
        fragmentArray.put(2, VideographyFragment.newInstance());
        fragmentArray.put(3, ServicesFragment.newInstance());
        fragmentArray.put(4, AboutUsFragment.newInstance());
//        checkConnection();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragmentArray.get(0));
        ft.commit();

        showAlertContact();
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (doubleBackToExitPressedOnce && !drawer.isDrawerOpen(GravityCompat.START)) {
            super.onBackPressed();
            return;
        }

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "To exit, press Back agian", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.contact_us_alert:
                showAlertContact();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (id == R.id.nav_home) {
            if (fragment instanceof HomeFragment) {
                drawer.closeDrawer(GravityCompat.START);
                return true;
            } else {
                fragment = fragmentArray.get(0);
            }
        } else if (id == R.id.nav_services) {
            if (fragment instanceof ServicesFragment) {
                drawer.closeDrawer(GravityCompat.START);
                return true;
            } else {
                fragment = fragmentArray.get(3);
            }
        } else if (id == R.id.nav_video) {
            if (fragment instanceof VideographyFragment) {
                drawer.closeDrawer(GravityCompat.START);
                return true;
            } else {
                fragment = fragmentArray.get(2);
            }
        } else if (id == R.id.nav_photos) {
            if (fragment instanceof PhotographyFragment) {
                drawer.closeDrawer(GravityCompat.START);
                return true;
            } else {
                fragment = fragmentArray.get(1);
            }
        } else if (id == R.id.nav_about_us) {
            if (fragment instanceof AboutUsFragment) {
                drawer.closeDrawer(GravityCompat.START);
                return true;
            } else {
                fragment = AboutUsFragment.newInstance();
                navigationView.setCheckedItem(R.id.nav_about_us);
            }
        } else if (id == R.id.nav_contact_us) {
            if (fragment instanceof ContactUsFragment) {
                drawer.closeDrawer(GravityCompat.START);
                return true;
            } else {
                fragment = ContactUsFragment.newInstance();
                navigationView.setCheckedItem(R.id.nav_contact_us);
            }
        } else if (id == R.id.nav_privacy_policy) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY_LINK));
            startActivity(browserIntent);
        }

        //replacing the fragment
        if (fragment != null) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        hideKeyboard();

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void showAlertContact() {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.alert_contact, null);

        name = alertLayout.findViewById(R.id.name);
        email = alertLayout.findViewById(R.id.emailID);
        phoneNumber = alertLayout.findViewById(R.id.phoneNumber);
        writeMessage = alertLayout.findViewById(R.id.writeMessage);
        inputName = alertLayout.findViewById(R.id.inputName);
        inputEmail = alertLayout.findViewById(R.id.inputEmailID);
        inputPhoneNumber = alertLayout.findViewById(R.id.inputPhoneNumber);
        inputMessage = alertLayout.findViewById(R.id.inputMessage);
        submitButton = alertLayout.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });

        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPhoneNumber.addTextChangedListener(new MyTextWatcher(inputPhoneNumber));
        inputMessage.addTextChangedListener(new MyTextWatcher(writeMessage));

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
        alert.setCancelable(true);
        dialog = alert.create();
        dialog.show();
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

    private void sendEmail() {
        EmailIntentBuilder.from(this)
                .to("greenwaterevents@gmail.com") //greenwaterevents@gmail.com
                .subject("Support for Green Water Events")
                .body(inputMessage.getText().toString() + "\n\nName: " + inputName.getText().toString().trim()
                        + "\nPhone Number: " + inputPhoneNumber.getText().toString()
                        + "\nMail Id: " + inputEmail.getText().toString().trim())
                .start();
        dialog.dismiss();
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputName.setError(getString(R.string.err_msg_name));
            inputName.requestFocus();
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
            inputEmail.requestFocus();
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
            inputPhoneNumber.requestFocus();
            return false;
        } else {
            phoneNumber.setErrorEnabled(false);
        }
        return true;
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
    public void getClick(String pageName) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (pageName.equals("photo")) {
            if (fragment instanceof PhotographyFragment) {

            } else {
                navigationView.setCheckedItem(R.id.nav_photos);
                fragment = fragmentArray.get(1);
            }
        } else {
            if (fragment instanceof VideographyFragment) {

            } else {
                navigationView.setCheckedItem(R.id.nav_video);
                fragment = fragmentArray.get(2);
            }
        }
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
//        showSnack(isConnected);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
