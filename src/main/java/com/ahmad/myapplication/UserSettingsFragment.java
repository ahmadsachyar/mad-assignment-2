package com.ahmad.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class UserSettingsFragment extends Fragment {

    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private RadioGroup radioGroupTheme;
    private CheckBox checkBoxNotifications;
    
    private Button buttonSave;
    private Button buttonReset;
    
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "UserPreferences";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_THEME = "theme";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String THEME_LIGHT = "Light";
    private static final String THEME_DARK = "Dark";
    private static final String THEME_AUTO = "Auto";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_settings, container, false);


        editTextUsername = view.findViewById(R.id.editTextUsername);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        radioGroupTheme = view.findViewById(R.id.radioGroupTheme);
        checkBoxNotifications = view.findViewById(R.id.checkBoxNotifications);
        buttonSave = view.findViewById(R.id.buttonSave);
        buttonReset = view.findViewById(R.id.buttonReset);

        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        loadPreferences();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    savePreferences();
                    Context context = requireContext();
                    Toast.makeText(context, context.getString(R.string.preferences_saved), Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPreferences();
                Context context = requireContext();
                Toast.makeText(context, context.getString(R.string.preferences_reset), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private boolean validateInput() {
        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString();

        Context context = requireContext();
        
        // Validate username
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError(context.getString(R.string.username_required));
            editTextUsername.requestFocus();
            return false;
        }

        if (username.length() < 3) {
            editTextUsername.setError(context.getString(R.string.username_min_length));
            editTextUsername.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError(context.getString(R.string.email_required));
            editTextEmail.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError(context.getString(R.string.email_invalid));
            editTextEmail.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError(context.getString(R.string.password_required));
            editTextPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            editTextPassword.setError(context.getString(R.string.password_min_length));
            editTextPassword.requestFocus();
            return false;
        }

        if (radioGroupTheme.getCheckedRadioButtonId() == -1) {
            Toast.makeText(context, context.getString(R.string.select_theme), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString();
        
        int selectedThemeId = radioGroupTheme.getCheckedRadioButtonId();
        String theme = "";
        if (selectedThemeId == R.id.radioButtonLight) {
            theme = THEME_LIGHT;
        } else if (selectedThemeId == R.id.radioButtonDark) {
            theme = THEME_DARK;
        } else if (selectedThemeId == R.id.radioButtonAuto) {
            theme = THEME_AUTO;
        }

        boolean notifications = checkBoxNotifications.isChecked();

        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_THEME, theme);
        editor.putBoolean(KEY_NOTIFICATIONS, notifications);

        editor.apply();
    }

    private void loadPreferences() {
        String username = sharedPreferences.getString(KEY_USERNAME, "");
        String email = sharedPreferences.getString(KEY_EMAIL, "");
        String password = sharedPreferences.getString(KEY_PASSWORD, "");
        String theme = sharedPreferences.getString(KEY_THEME, "");
        boolean notifications = sharedPreferences.getBoolean(KEY_NOTIFICATIONS, false);

        editTextUsername.setText(username);
        editTextEmail.setText(email);
        editTextPassword.setText(password);
        checkBoxNotifications.setChecked(notifications);

        // Set theme radio button
        if (THEME_LIGHT.equals(theme)) {
            radioGroupTheme.check(R.id.radioButtonLight);
        } else if (THEME_DARK.equals(theme)) {
            radioGroupTheme.check(R.id.radioButtonDark);
        } else if (THEME_AUTO.equals(theme)) {
            radioGroupTheme.check(R.id.radioButtonAuto);
        }
    }

    private void resetPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Clear UI fields
        editTextUsername.setText("");
        editTextEmail.setText("");
        editTextPassword.setText("");
        radioGroupTheme.clearCheck();
        checkBoxNotifications.setChecked(false);
    }
}

