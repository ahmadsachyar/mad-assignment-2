package com.ahmad.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ProfileViewFragment extends Fragment {

    private TextView textViewUsername;
    private TextView textViewEmail;
    private TextView textViewPassword;
    private TextView textViewTheme;
    private TextView textViewNotifications;
    
    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;
    private static final String PREFS_NAME = "UserPreferences";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_THEME = "theme";
    private static final String KEY_NOTIFICATIONS = "notifications";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_view, container, false);

        // Initialize views
        textViewUsername = view.findViewById(R.id.textViewUsername);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewPassword = view.findViewById(R.id.textViewPassword);
        textViewTheme = view.findViewById(R.id.textViewTheme);
        textViewNotifications = view.findViewById(R.id.textViewNotifications);

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Set up preference change listener for real-time updates
        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                loadAndDisplayPreferences();
            }
        };

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Load and display preferences whenever fragment becomes visible
        loadAndDisplayPreferences();
        // Register preference change listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister preference change listener
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    private void loadAndDisplayPreferences() {
        String username = sharedPreferences.getString(KEY_USERNAME, "");
        String email = sharedPreferences.getString(KEY_EMAIL, "");
        String password = sharedPreferences.getString(KEY_PASSWORD, "");
        String theme = sharedPreferences.getString(KEY_THEME, "");
        boolean notifications = sharedPreferences.getBoolean(KEY_NOTIFICATIONS, false);

        Context context = requireContext();
        String notSet = context.getString(R.string.not_set);
        String enabled = context.getString(R.string.enabled);
        String disabled = context.getString(R.string.disabled);

        if (username.isEmpty()) {
            textViewUsername.setText(notSet);
        } else {
            textViewUsername.setText(username);
        }

        if (email.isEmpty()) {
            textViewEmail.setText(notSet);
        } else {
            textViewEmail.setText(email);
        }

        if (password.isEmpty()) {
            textViewPassword.setText(notSet);
        } else {
            textViewPassword.setText(maskPassword(password));
        }

        if (theme.isEmpty()) {
            textViewTheme.setText(notSet);
        } else {
            textViewTheme.setText(theme);
        }

        textViewNotifications.setText(notifications ? enabled : disabled);
    }

    private String maskPassword(String password) {
        if (password == null || password.isEmpty()) {
            return "";
        }
        StringBuilder masked = new StringBuilder();
        for (int i = 0; i < password.length(); i++) {
            masked.append("*");
        }
        return masked.toString();
    }
}

