package com.example.brainconclient;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.brainconclient.helpers.StringResourceHelper;

public class ProfileFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_IMAGE = 1;
    private SharedPreferences prefs;
    private TextView txtProfFirstName;
    private ImageView avatarImage;

    private Button changeAvButton;
    private TextView profileLogoutBtn;
    private boolean isLoggedIn;
    private Button changeProfileButton;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        txtProfFirstName        = view.findViewById(R.id.txt_prof_first_name);
        prefs = getActivity().getSharedPreferences("YourPrefsName", MODE_PRIVATE);
        isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        changeProfileButton = view.findViewById(R.id.change_profile_button);

        profileLogoutBtn = view.findViewById(R.id.txt_logout);
        avatarImage = view.findViewById(R.id.avatar_image);
        changeAvButton = view.findViewById(R.id.change_av_button);

        changeAvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickImageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
                openGalleryForImage();
            }
        });

        profileLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logUserOut();
            }
        });

        changeProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToProfileSettings();
            }
        });
        setUserDetailsInEditTextFields();
        return view;
    }

    public void setUserDetailsInEditTextFields(){
        // GET STORED PREFERENCES:
        prefs = getActivity().getSharedPreferences(StringResourceHelper.getUserDetailPrefName(), MODE_PRIVATE);
        // SET TEXT INPUT FIELDS VALUES:
        txtProfFirstName.setText(prefs.getString("first_name", ""));
    }
    public void goToProfileSettings() {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        startActivity(intent);
    }


    public void logUserOut(){
        clearAuthenticationPreferences();
        goToLogin();
        Toast.makeText(getActivity(), "Logged Out Successfully", Toast.LENGTH_SHORT).show();
    }

    public void clearAuthenticationPreferences(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("token"); // Удаляем только токен аутентификации
        editor.apply();
    }
    // END OF LOGOUT OR CLEAR PREFERENCES.

    public void goToLogin(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void openGalleryForImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            avatarImage.setImageURI(imageUri);
        }
    }
}