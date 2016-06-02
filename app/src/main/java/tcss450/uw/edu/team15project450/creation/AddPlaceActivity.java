package tcss450.uw.edu.team15project450.creation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import tcss450.uw.edu.team15project450.MainActivity;
import tcss450.uw.edu.team15project450.R;
import tcss450.uw.edu.team15project450.authenticate.SignInActivity;
import tcss450.uw.edu.team15project450.browse.userTour.ViewCreatedToursActivity;
import tcss450.uw.edu.team15project450.server.Upload;

public class AddPlaceActivity extends AppCompatActivity
        implements AddPlaceFragment.AddPlaceListener
        , AddAudioFragment.AddAudioListener
        , AddImageFragment.AddImageListener {


    private static final String TAG = "AddPlaceActivity";
    private final static String ADD_COORDINATES_URL
            = "http://cssgate.insttech.washington.edu/~_450atm15/addCoordinates.php?";
    private static final int PLACE_PICKER_REQUEST = 0;

    private static final String TYPE = "place";
    private static final String PLACE = "place";
    private static final String PICKER = "place_picker";
    private static final String AUDIO = "audio";
    private static final String IMAGE = "image";

    private SharedPreferences mSharedPreference;
    private String mUserID;
    private String mFragmentType;
    private boolean mHasAudio;
    private boolean mHasImage;
    private Bundle mBundle;
    private String mOutputFilePath;
    private String mTour;
    private String mUploadFileResult;
    private GoogleApiClient mGoogleApiClient;
    private TextView mTextView;
    private LatLng mLatLong;
    private double mLatitude;
    private double mLongitude;
    private String mCreatedBy;
    private String mPlace;
    private String mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mUserID = mBundle.getString("userid");
            mTour = mBundle.getString("tour");
            mBundle.putString("type", TYPE);
        } else {
            Toast.makeText(this, "Unable to add a place: not enough information.", Toast.LENGTH_SHORT)
                    .show();
            callMain();
        }
        mType = TYPE;

        callAddPlaceFragment();
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

    @Override
    protected void onStart() {
        super.onStart();
        if( mGoogleApiClient != null )
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                mFragmentType = PICKER;
                mLatLong = place.getLatLng();
                mLatitude = mLatLong.latitude;
                mLongitude = mLatLong.longitude;
                Log.i(TAG, mLatLong.toString());

                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

                StringBuilder sb = new StringBuilder(ADD_COORDINATES_URL);

                try {
                    sb.append("tour=");
                    sb.append(URLEncoder.encode(mTour, "UTF-8"));

                    sb.append("&userid=");
                    sb.append(URLEncoder.encode(mUserID, "UTF-8"));

                    sb.append("&place=");
                    sb.append(URLEncoder.encode(mPlace, "UTF-8"));

                    sb.append("&latitude=");
                    sb.append(mLatitude);

                    sb.append("&longitude=");
                    sb.append(mLongitude);

                    Log.i(TAG, sb.toString());
                }
                catch(Exception e) {
                    Toast.makeText(this, "Something wrong with the url."
                            + e.getMessage(), Toast.LENGTH_LONG)
                            .show();
                }

                String url = sb.toString();

                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    AddPlaceTask task = new AddPlaceTask();
                    task.execute(new String[]{url.toString()});
                } else {
                    Toast.makeText(this, "No network connection available. Cannot add coordinates.", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
            } else {

            }
        }
    }

    public void callAddPlaceFragment() {
        if (findViewById(R.id.fragment_container) != null) {
            AddPlaceFragment addPlaceFragment = new AddPlaceFragment();
            addPlaceFragment.setArguments(mBundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, addPlaceFragment)
                    .commit();
        }
    }

    public void callPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
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
             * Will execute task in order to add place to the database and at this
             * time calls MainActivity no matter the result.
             *
             * @param url
             * @param hasAudio
             * @param hasImage
             */
    @Override
    public void addPlace(String url, String place, boolean hasAudio, boolean hasImage) {
        mFragmentType = PLACE;
        mPlace = place;
        mBundle.putString("place", mPlace);
        mHasAudio = hasAudio;
        mHasImage = hasImage;

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            AddPlaceTask task = new AddPlaceTask();
            task.execute(new String[]{url.toString()});
        } else {
            Toast.makeText(this, "No network connection available. Cannot add place.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
    }

    /**
     * Not fully implemented, will execute task and call AddImageFragment
     * or AddPlaceFragment upon result depending on boolean hasImage.
     *
     * @param url
     */
    @Override
    public void addAudio(String url, String type, String outputFile) {
        mFragmentType = AUDIO;
        mOutputFilePath = outputFile;
        mBundle.putString("filepath", mOutputFilePath);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            AddPlaceTask task = new AddPlaceTask();
            task.execute(new String[]{url.toString()});
        } else {
            Toast.makeText(this, "No network connection available. Cannot add audio.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
    }

    /**
     * Not fully implemented, will execute task and call AddPlaceFragment
     * upon result.
     *
     * @param url
     */
    @Override
    public void addImage(String url, String type, String outputFile) {
        mFragmentType = IMAGE;
        mOutputFilePath = outputFile;
        mBundle.putString("outputFilePath", mOutputFilePath);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            AddPlaceTask task = new AddPlaceTask();
            task.execute(new String[]{url.toString()});
        } else {
            Toast.makeText(this, "No network connection available. Cannot add image.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
    }

    /**
     *
     */
    private class AddPlaceTask extends AsyncTask<String, Void, String> {

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
                        Log.d(TAG, "Source file: " + mOutputFilePath);

                        // Set the file userid/activity_type/tour_title/place_title)
                        Log.d(TAG, "Type: " + mType);
                        Upload file = new Upload(mUserID, mType, mTour, mPlace);

                        response = file.Send(fis);
                        Log.d(TAG, response);

                    } catch (FileNotFoundException e) {
                        // Error: File not found
                        response = "Unable to create, Reason: "
                                + e.getMessage();
                        Log.d(TAG, response);
                    }
                }
                if (response.equals("success") || mFragmentType.equals(PLACE) || mFragmentType.equals(PICKER)) {
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
                        Log.i(TAG, "Response: " + s);

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
         * If not, it displays the exception. Since this is used by several fragments (only two
         * currently implemented) it has personalized messages based on a type variable set in the
         * php files on the server.
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
                    if (mFragmentType.equals(PLACE)) {
                        Toast.makeText(getApplicationContext(), "Place successfully added!"
                                , Toast.LENGTH_SHORT)
                                .show();

                        callPlacePicker();
                    } else if (mFragmentType.equals(PICKER)) {
                        Toast.makeText(getApplicationContext(), "Coordinates successfully added!"
                                , Toast.LENGTH_SHORT)
                                .show();
                        if (mHasAudio) {
                            callAddAudioFragment();
                        } else if (mHasImage) {
                            callAddImageFragment();
                        } else {
                            callViewCreatedTours();
                        }
                    } else if (mFragmentType.equals(AUDIO)) {
                        Toast.makeText(getApplicationContext(), "Audio successfully added!"
                                , Toast.LENGTH_SHORT)
                                .show();
                        if (mHasImage) {
                            callAddImageFragment();
                        } else {
                            callViewCreatedTours();
                        }
                    } else if (mFragmentType.equals(IMAGE)) {
                        Toast.makeText(getApplicationContext(), "Image successfully added!"
                                , Toast.LENGTH_SHORT)
                                .show();
                        callViewCreatedTours();
                    } else {
                        Toast.makeText(getApplicationContext(), "Successfully added!"
                                , Toast.LENGTH_SHORT)
                                .show();
                        callViewCreatedTours();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to add " + mFragmentType + ": ("
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();

                    if (mFragmentType.equals(PLACE)) {
                        callMain();
                    } else if (mFragmentType.equals(PICKER)) {
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

                if (mFragmentType.equals(PLACE)) {
                    callMain();
                } else if (mFragmentType.equals(PICKER)) {
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

