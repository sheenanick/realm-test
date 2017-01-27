package com.lifehackig.realm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.lifehackig.realm.R;
import com.lifehackig.realm.RealmApplication;
import com.lifehackig.realm.UserManager;
import com.lifehackig.realm.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncCredentials;
import io.realm.SyncUser;


public class SignUpActivity extends AppCompatActivity{
    @BindView(R.id.username) EditText usernameView;
    @BindView(R.id.password) EditText passwordView;
    @BindView(R.id.confirmPassword) EditText confirmPasswordView;

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
        username = usernameView.getText().toString();
        String password = passwordView.getText().toString();

        SyncUser.loginAsync(SyncCredentials.usernamePassword(username, password, true), RealmApplication.AUTH_URL, new SyncUser.Callback() {
            @Override
            public void onSuccess(SyncUser user) {
                Log.d("success", "onSuccess");
                signUpComplete(user);
            }
            @Override
            public void onError(ObjectServerError error) {
                String errorMsg;
                switch (error.getErrorCode()) {
                    case EXISTING_ACCOUNT: errorMsg = "Account already exists"; break;
                    default:
                        errorMsg = error.toString();
                }
                Log.d("error", errorMsg);
            }
        });
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
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
