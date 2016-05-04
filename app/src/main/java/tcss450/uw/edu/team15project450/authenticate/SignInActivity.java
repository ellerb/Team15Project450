package tcss450.uw.edu.team15project450.authenticate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import tcss450.uw.edu.team15project450.MainActivity;
import tcss450.uw.edu.team15project450.R;

public class SignInActivity extends AppCompatActivity
        implements LoginFragment.LoginInteractionListener, RegisterFragment.RegisterUserListener {

    private SharedPreferences mSharedPreferences;
    private String mUserId;
    private String mPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        if (!mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new LoginFragment()).commit();
        } else {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }

        // if register button is clicked
        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment registerFragment = new RegisterFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, registerFragment)
                        .commit();
            }
        });
    }

    public void login(String url, String userId, String pwd) {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Checks if userid and password are valid
            new LoginTask().execute((new String[] { url.toString() }));
            mUserId = userId;
            mPwd = pwd;

        } else {
            Toast.makeText(this, "No network connection available. Cannot authenticate user", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
    }

    public void addUserToLoginFile() {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                    openFileOutput(getString(R.string.LOGIN_FILE), Context.MODE_PRIVATE));
            outputStreamWriter.write("username = " + mUserId + ";");
            outputStreamWriter.write("password = " + mPwd);
            outputStreamWriter.close();
            Toast.makeText(this, "Stored in File Successfully!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mSharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), true).commit();
        mSharedPreferences.edit().putString("userid", mUserId).commit();
    }

    private class LoginTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to download the list of users, Reason: " + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // Something is wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                return;
            }

            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("result");

                if (status.equalsIgnoreCase("success")) {
                    Toast.makeText(getApplicationContext(), "Successfully logged in", Toast.LENGTH_SHORT);

                    addUserToLoginFile();

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();

                } else {
                    String r = jsonObject.getString("error");
                    Toast.makeText(getApplicationContext(), "Failed: " + r, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.d("LoginTask", "Parsing JSON exception" + e.getMessage());
            }
        }
    }

    public void registerUser(String url) {
        RegisterUserTask task = new RegisterUserTask();
        task.execute(new String[] { url.toString() });
    }

    // Inner class that allow us to call the service for registering a user.
    private class RegisterUserTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to register user, Reason: " + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * It checks to see if there was a problem with the URL(Network) which
         * is when an exception is caught. It tries to call the parse Method and
         * checks to see if it was successful. If not, it displays the
         * exception.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            // Something is wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.getString("result");
                if (status.equals("success")) {
                    Toast.makeText(getApplicationContext(), "User successfully registered!", Toast.LENGTH_LONG).show();

                    addUserToLoginFile();

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Failed to register: " + jsonObject.get("error"),
                            Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something is wrong with the data " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

}