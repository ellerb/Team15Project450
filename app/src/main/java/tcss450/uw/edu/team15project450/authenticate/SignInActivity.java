package tcss450.uw.edu.team15project450.authenticate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tcss450.uw.edu.team15project450.MainActivity;
import tcss450.uw.edu.team15project450.R;

public class SignInActivity extends AppCompatActivity implements LoginFragment.LoginInteractionListener,
        RegisterFragment.RegisterInteractionListener {

    private SharedPreferences mSharedPreferences;

    public void login(String username, String pwd) {
        mSharedPreferences
                .edit()
                .putBoolean(getString(R.string.LOGGEDIN), true)
                .commit();

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void register(String username, String pwd, String name) {
        mSharedPreferences
                .edit()
                .putBoolean(getString(R.string.LOGGEDIN), true)
                .commit();

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new LoginFragment() )
                .commit();

        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS)
                , Context.MODE_PRIVATE);
        if (!mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new LoginFragment())
                    .commit();
        } else {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}
