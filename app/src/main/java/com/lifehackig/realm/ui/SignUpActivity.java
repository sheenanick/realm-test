package com.lifehackig.realm.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.lifehackig.realm.R;
import com.lifehackig.realm.RealmApplication;
import com.lifehackig.realm.UserManager;
import com.lifehackig.realm.model.Photo;
import com.lifehackig.realm.model.User;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

import static android.text.TextUtils.isEmpty;


public class SignUpActivity extends AppCompatActivity{
    @BindView(R.id.username) EditText usernameView;
    @BindView(R.id.password) EditText passwordView;
    @BindView(R.id.confirmPassword) EditText passwordConfirmationView;

    private Realm realm;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.signUp)
    public void attemptSignUp() {
        usernameView.setError(null);
        passwordView.setError(null);
        passwordConfirmationView.setError(null);

        username = usernameView.getText().toString();
        String password = passwordView.getText().toString();
        String passwordConfirmation = passwordConfirmationView.getText().toString();

        boolean cancel = false;
        if (isEmpty(username)) {
            usernameView.setError("This field is required");
            cancel = true;
        }
        if (isEmpty(password)) {
            passwordView.setError("This field is required");
            cancel = true;
        }
        if (isEmpty(passwordConfirmation)) {
            passwordConfirmationView.setError("This field is required");
            cancel = true;
        }
        if (!password.equals(passwordConfirmation)) {
            passwordConfirmationView.setError("Passwords do not match");
            cancel = true;
        }

        if (!cancel) {
            SyncUser.loginAsync(SyncCredentials.usernamePassword(username, password, true), RealmApplication.AUTH_URL, new SyncUser.Callback() {
                @Override
                public void onSuccess(SyncUser user) {
                    signUpComplete(user);
                }
                @Override
                public void onError(ObjectServerError error) {
                    String errorMsg = error.toString();
                    Log.d("error", errorMsg);
                    usernameView.setError("Username already exists");
                }
            });
        }
    }

    private void signUpComplete(SyncUser user) {
        UserManager.setActiveUser(user);
        createUserObject(user);

        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void createUserObject(final SyncUser currentUser) {
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm){
                User user = realm.createObject(User.class, currentUser.getIdentity());
                user.setUsername(username);

                Drawable d = getResources().getDrawable(R.drawable.testimage);
                Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                user.setProfileImage(byteArray);

                Photo realmPhoto = realm.createObject(Photo.class);
                realmPhoto.setImage(byteArray);
                user.addToPhotoStream(realmPhoto);
                user.addToPhotoStream(realmPhoto);
                user.addToPhotoStream(realmPhoto);
                user.addToPhotoStream(realmPhoto);
                user.addToPhotoStream(realmPhoto);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
