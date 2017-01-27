package com.lifehackig.realm.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.lifehackig.realm.R;
import com.lifehackig.realm.RealmApplication;
import com.lifehackig.realm.UserManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.ObjectServerError;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

public class SignInActivity extends AppCompatActivity {
    @BindView(R.id.username) EditText usernameView;
    @BindView(R.id.password) EditText passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        final SyncUser user = SyncUser.currentUser();
        if (user != null) {
            loginComplete(user);
        }
    }

    private void loginComplete(SyncUser user) {
        UserManager.setActiveUser(user);
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.signIn)
    public void attemptSignIn() {
        final String username = usernameView.getText().toString().trim();
        final String password = passwordView.getText().toString().trim();

        SyncUser.loginAsync(SyncCredentials.usernamePassword(username, password, false), RealmApplication.AUTH_URL, new SyncUser.Callback() {
            @Override
            public void onSuccess(SyncUser user) {
                loginComplete(user);
            }

            @Override
            public void onError(ObjectServerError error) {
                String errorMsg;
                switch (error.getErrorCode()) {
                    case UNKNOWN_ACCOUNT:
                        errorMsg = "Account does not exists.";
                        break;
                    case INVALID_CREDENTIALS:
                        errorMsg = "The provided credentials are invalid!"; // This message covers also expired account token
                        break;
                    default:
                        errorMsg = error.toString();
                }
                Toast.makeText(SignInActivity.this, errorMsg, Toast.LENGTH_LONG).show();
            }
        });

    }

    @OnClick(R.id.signUp)
    public void signUp() {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

}
