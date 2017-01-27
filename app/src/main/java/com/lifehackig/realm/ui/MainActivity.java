package com.lifehackig.realm.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.lifehackig.realm.R;
import com.lifehackig.realm.UserManager;
import com.lifehackig.realm.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.textView) TextView textView;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).equalTo("uid", UserManager.getCurrentUserId()).findFirst();
        if (user.getUsername()!= null) {
            textView.setText(user.getUsername());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_log_out:
                UserManager.logoutActiveUser();
                Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}