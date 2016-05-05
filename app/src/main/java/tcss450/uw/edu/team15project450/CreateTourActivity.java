package tcss450.uw.edu.team15project450;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import tcss450.uw.edu.team15project450.authenticate.SignInActivity;

public class CreateTourActivity extends AppCompatActivity
        implements CreateTourFragment.CreateBasicTourListener
        , AddAudioFragment.AddAudioListener
        , AddImageFragment.AddImageListener
        , AddPlaceFragment.AddPlaceListener {

    private SharedPreferences mSharedPreference;
    private String mUserID;
    private String mTourTitle;

    /**
     *
     * @param savedInstanceState
     */
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_tour);

        mSharedPreference = getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        String mUserID = mSharedPreference.getString("userid", null);

        if (findViewById(R.id.fragment_container) != null) {
            Bundle bundle = new Bundle();
            bundle.putString("userid", mUserID);
            CreateTourFragment createTourFragment = new CreateTourFragment();
            createTourFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, createTourFragment)
                    .commit();
        }
    }

    /**
     *
     * @param menu
     * @return
     */
     @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     *
     * @param item
     * @return
     */
     @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_logout) {
            SharedPreferences sharedPreferences =
                    getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), false)
                    .commit();

            Intent i = new Intent(this, SignInActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @param url
     * @param hasAudio
     * @param hasImage
     */
     @Override
    public void createBasicTour(String url, boolean hasAudio, boolean hasImage) {

        CreateTourTask task = new CreateTourTask();
        task.execute(new String[]{url.toString()});

        if (hasAudio) {
            if (findViewById(R.id.fragment_container) != null) {
                AddAudioFragment addAudioFragment = new AddAudioFragment();
                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, addAudioFragment)
                        .addToBackStack(null);
                transaction.commit();
            }
        } else if (hasImage) {
            if (findViewById(R.id.fragment_container) != null) {
                AddImageFragment addImageFragment = new AddImageFragment();
                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, addImageFragment)
                        .addToBackStack(null);
                transaction.commit();
            }
        } else {
            if (findViewById(R.id.fragment_container) != null) {
                Bundle bundle = new Bundle();
                bundle.putString("userid", mUserID);
                bundle.putString("tourTitle", mTourTitle);
                AddPlaceFragment addPlaceFragment = new AddPlaceFragment();
                addPlaceFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, addPlaceFragment)
                        .commit();
            }
        }

    }

    /**
     *
     * @param url
     * @param hasImage
     */
     @Override
    public void addTourAudio(String url, boolean hasImage) {

        CreateTourTask task = new CreateTourTask();
        task.execute(new String[]{url.toString()});

        if (hasImage) {
            if (findViewById(R.id.fragment_container) != null) {
                AddImageFragment addImageFragment = new AddImageFragment();
                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, addImageFragment)
                        .addToBackStack(null);
                transaction.commit();
            }
        } else {
            if (findViewById(R.id.fragment_container) != null) {
                Bundle bundle = new Bundle();
                bundle.putString("userid", mUserID);
                bundle.putString("tourTitle", mTourTitle);
                AddPlaceFragment addPlaceFragment = new AddPlaceFragment();
                addPlaceFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, addPlaceFragment)
                        .commit();
            }
        }
    }

    /**
     *
     * @param url
     */
     @Override
    public void addTourImage(String url) {

        CreateTourTask task = new CreateTourTask();
        task.execute(new String[]{url.toString()});

         if (findViewById(R.id.fragment_container) != null) {
             Bundle bundle = new Bundle();
             bundle.putString("userid", mUserID);
             bundle.putString("tourTitle", mTourTitle);
             AddPlaceFragment addPlaceFragment = new AddPlaceFragment();
             addPlaceFragment.setArguments(bundle);
             getSupportFragmentManager().beginTransaction()
                     .replace(R.id.fragment_container, addPlaceFragment)
                     .commit();
         }
    }

    /**
     *
     * @param url
     * @param hasAudio
     * @param hasImage
     */
     @Override
    public void addPlace(String url, boolean hasAudio, boolean hasImage) {

         CreateTourTask task = new CreateTourTask();
         task.execute(new String[]{url.toString()});

         Intent viewCreatedIntent = new Intent(this, ViewCreatedToursActivity.class);
         startActivity(viewCreatedIntent);
     }

    /**
     *
     */
     private class CreateTourTask extends AsyncTask<String, Void, String> {

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
                    response = "Unable to create, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }


        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                String type = (String) jsonObject.get("type");

                if(status.equals("success") && type.equals("createBasicTour")) {
                    mTourTitle = (String) jsonObject.get("tourTitle");
                }
                if (status.equals("success")) {
                    String toastText;
                    if(type.equals("createBasicTour")) {
                        toastText = "Basic tour successfully added!";
                    } else if (type.equals("addPlace")) {
                        toastText = "Place successfully added!";
                    } else {
                        toastText = "Successfully added!";
                    }
                    Toast.makeText(getApplicationContext(), toastText
                            , Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to add: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data " +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
