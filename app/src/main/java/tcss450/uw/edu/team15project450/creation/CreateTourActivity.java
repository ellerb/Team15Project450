package tcss450.uw.edu.team15project450.creation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import tcss450.uw.edu.team15project450.MainActivity;
import tcss450.uw.edu.team15project450.R;
import tcss450.uw.edu.team15project450.authenticate.SignInActivity;
import tcss450.uw.edu.team15project450.browse.userTour.ViewCreatedToursActivity;
import tcss450.uw.edu.team15project450.server.Upload;

/**
 * This class is an Activity that allows a user to create their own tour.
 * It may call up to three different fragments including: CreateTourFragment,
 * AddAudioFragment, and AddImageFragment (not implemented). AddPlaceActivity
 * ViewCreatedTours, and Main can also be called from here.
 *
 * @author Gabrielle Bly, Gabrielle Glynn
 * @version May 4, 2016
 */
public class CreateTourActivity extends AppCompatActivity
        implements CreateTourFragment.CreateBasicTourListener
        , AddAudioFragment.AddAudioListener
        , AddImageFragment.AddImageListener {

    private static final String TAG = "CreateTourActivity";
    private static final int REQUEST_AUDIO_MIC_AND_STORAGE_PERMISSIONS = 0;
    static final int UPLOAD_FILE_REQUEST = 1;  // The request code

    private static final String TYPE = "tour";
    private static final String BASIC = "tour";
    private static final String AUDIO = "audio";
    private static final String IMAGE = "image";

    private SharedPreferences mSharedPreference;
    private String mUserID;
    private String mFragmentType;
    private boolean mHasAudio;
    private boolean mHasImage;
    private Bundle mBundle;
    String mOutputFilePath;
    String mTour;
    String mUploadFileResult;

    /**
     * This initial onCreate method will get and set userid, which is passed
     * to other fragments in order to add to database, and calls the iniital fragment,
     * CreateTourFragment.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tour);

        mSharedPreference = getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        mUserID = mSharedPreference.getString("userid", null);

        mBundle = new Bundle();
        mBundle.putString("userid", mUserID);

         callCreateBasicTourFragment();
    }

    /**
     * Adds menu options.
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
     * Method adds a logout button, changes shared preferences when clicked
     * and calls new SignInActivity.
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
     * Will handle the users choices for permissions, informing them of
     * consequences should permissions not be given.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_AUDIO_MIC_AND_STORAGE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Please provide permission in order to record audio."
                            , Toast.LENGTH_SHORT)
                            .show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * Method will look for code to indicate whether or not Upload class has
     * succeeded or failed when called.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == UPLOAD_FILE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                mUploadFileResult = "success";
            } else {
                mUploadFileResult = "fail";
            }
        }
    }

    public void callCreateBasicTourFragment() {
        if (findViewById(R.id.fragment_container) != null) {
            CreateTourFragment createTourFragment = new CreateTourFragment();
            createTourFragment.setArguments(mBundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, createTourFragment)
                    .commit();
        }
    }

    public void callAddAudioFragment() {
        if (findViewById(R.id.fragment_container) != null) {
            AddAudioFragment addAudioFragment = new AddAudioFragment();
            addAudioFragment.setArguments(mBundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, addAudioFragment)
                    .commit();
        }
    }

    public void callAddImageFragment() {
        if (findViewById(R.id.fragment_container) != null) {
            AddImageFragment addImageFragment = new AddImageFragment();
            addImageFragment.setArguments(mBundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, addImageFragment)
                    .commit();
        }
    }

    public void callAddPlaceActivity() {
        if (findViewById(R.id.fragment_container) != null) {
            Intent i = new Intent(getApplicationContext(), AddPlaceActivity.class);
            i.putExtras(mBundle);
            startActivity(i);
            finish();
        }
    }

    public void callViewCreatedTours() {
        if (findViewById(R.id.fragment_container) != null) {
            Intent i = new Intent(getApplicationContext(), ViewCreatedToursActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void callMain() {
        if (findViewById(R.id.fragment_container) != null) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    /**
     * Will set globals base on user choices in the CreateTourFragment an call the
     * asynctask to handle those choices and send responses to the database.
     *
     * @param url
     * @param hasAudio
     * @param hasImage
     * @param tourTitle
     */
     @Override
    public void createBasicTour(String url, boolean hasAudio, boolean hasImage, String tourTitle) {

         mFragmentType = BASIC;
         mHasAudio = hasAudio;
         mHasImage = hasImage;
         mTour = tourTitle;

         mBundle.putString("tour", mTour);
         mBundle.putString("type", TYPE);
         mBundle.putString("place", null);

         ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

         if (networkInfo != null && networkInfo.isConnected()) {
             CreateTourTask task = new CreateTourTask();
             task.execute(new String[]{url.toString()});
         } else {
             Toast.makeText(this, "No network connection available. Cannot add tour.", Toast.LENGTH_SHORT)
                     .show();
             return;
         }
    }

    /**
     * Will execute task and set globals depending on user
     * choices and where the fragment was called from.
     *
     * @param url
     * @param type
     * @param outputFile
     */
    @Override
    public void addAudio(String url, String type, String outputFile) {
        mFragmentType = AUDIO;
        mOutputFilePath = outputFile;
        mBundle.putString("filepath", mOutputFilePath);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            CreateTourTask task = new CreateTourTask();
            task.execute(new String[]{url.toString()});
        } else {
            Toast.makeText(this, "No network connection available. Cannot add audio.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
    }

    /**
     * Not fully implemented. Will set globals and execute task.
     *
     * @param url
     * @param type
     * @param outputFile
     */
    @Override
    public void addImage(String url, String type, String outputFile) {
        mFragmentType = IMAGE;
        mOutputFilePath = outputFile;
        mBundle.putString("outputFilePath", mOutputFilePath);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            CreateTourTask task = new CreateTourTask();
            task.execute(new String[]{url.toString()});
        } else {
            Toast.makeText(this, "No network connection available. Cannot add image.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
    }

    /**
     * AsyncTask handles all the listeners in CreateTourActivity. If the url is coming
     * from an audio or image file, the Upload class is used.
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
                if (mFragmentType.equals(AUDIO) || mFragmentType.equals(IMAGE)) {
                    try {

                        // Set your file path here
                        FileInputStream fis = new FileInputStream(mOutputFilePath);

                        // Set the file userid/activity_type/tour_title/place_title)
                        Upload file = new Upload(mUserID, TYPE, mTour, null);

                        response = file.Send(fis);
                        Log.d(TAG, response);
                    } catch (FileNotFoundException e) {
                        // Error: File not found
                        response = "Unable to create, Reason: "
                                + e.getMessage();
                        Log.e(TAG, response);
                    }
                }
                if (response.equals("success") || mFragmentType.equals(BASIC)) {
                    response = "";
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
            }
            return response;
        }

        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception. Since this is used by several fragments it has
         * personalized messages based on a type variable set in the php files on the server.
         * It also calls the next fragment or activity based on the globals set right before it
         * is called by each listener.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                Log.i(TAG, "Status: " + status);
                if (status.equals("success")) {
                    if (mFragmentType.equals(BASIC)) {
                        Toast.makeText(getApplicationContext(), "Tour successfully added!"
                                , Toast.LENGTH_SHORT)
                                .show();

                        if (mHasAudio) {
                            callAddAudioFragment();
                        } else if (mHasImage) {
                            callAddImageFragment();
                        } else {
                            callAddPlaceActivity();
                        }
                    } else if (mFragmentType.equals(AUDIO)) {
                        Toast.makeText(getApplicationContext(), "Audio successfully added!"
                                , Toast.LENGTH_SHORT)
                                .show();

                        if (mHasImage) {
                            callAddImageFragment();
                        } else {
                            callAddPlaceActivity();
                        }
                    } else if (mFragmentType.equals(IMAGE)) {
                        Toast.makeText(getApplicationContext(), "Image successfully added!"
                                , Toast.LENGTH_SHORT)
                                .show();
                        callAddPlaceActivity();
                    } else {
                        Toast.makeText(getApplicationContext(), "Successfully added!"
                                , Toast.LENGTH_SHORT)
                                .show();

                        callViewCreatedTours();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to add " + mFragmentType + ": "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();

                    if (mFragmentType.equals(BASIC)) {
                        callMain();
                    } else if (mFragmentType.equals(AUDIO)) {
                        if (mHasImage) {
                            callAddImageFragment();
                        } else {
                            callViewCreatedTours();
                        }
                    } else if (mFragmentType.equals(IMAGE)) {
                        callViewCreatedTours();
                    } else {
                        callViewCreatedTours();
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data: " +
                        e.getMessage(), Toast.LENGTH_LONG).show();

                if (mFragmentType.equals(BASIC)) {
                    callMain();
                } else if (mFragmentType.equals(AUDIO)) {
                    if (mHasImage) {
                        callAddImageFragment();
                    } else {
                        callViewCreatedTours();
                    }
                } else if (mFragmentType.equals(IMAGE)) {
                    callViewCreatedTours();
                } else {
                    callViewCreatedTours();
                }
            }
        }
    }
}
