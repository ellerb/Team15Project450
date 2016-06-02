package tcss450.uw.edu.team15project450;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import tcss450.uw.edu.team15project450.authenticate.LoginFragment;
import tcss450.uw.edu.team15project450.authenticate.SignInActivity;
import tcss450.uw.edu.team15project450.browse.BrowseToursActivity;
import tcss450.uw.edu.team15project450.browse.userTour.ViewCreatedToursActivity;
import tcss450.uw.edu.team15project450.creation.CreateTourActivity;

/**
 * This activity serves as the homepage for the app. From here you can launch
 * the three major activities: CreateTourActivity, BrowseToursActivity, and
 * ViewCreatedToursActivity.
 *
 * @author Gabrielle Bly, Gabrielle Glynn
 * @version May 4, 2016
 */
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_AUDIO_MIC_AND_STORAGE_PERMISSIONS = 0;
    private static final int MY_PERMISSIONS_LOCATIONS = 1;

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO
                            , Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_AUDIO_MIC_AND_STORAGE_PERMISSIONS);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                            , Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_LOCATIONS);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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
            case MY_PERMISSIONS_LOCATIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Please provide permission in order to choose a location."
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
     * Launches CreateTourActivity.
     *
     * @param view
     */
    public void goCreateTour(View view) {
        Intent createIntent = new Intent(this, CreateTourActivity.class);
        startActivity(createIntent);
    }

    /**
     * Launches ViewCreatedToursActivity.
     *
     * @param view
     */
    public void goViewCreatedTours(View view) {
        Intent viewCreatedIntent = new Intent(this, ViewCreatedToursActivity.class);
        startActivity(viewCreatedIntent);
    }

    /**
     * Launches BrowseToursActivity.
     *
     * @param view
     */
    public void goBrowseTours(View view) {
        Intent browseIntent = new Intent(this, BrowseToursActivity.class);
        startActivity(browseIntent);
    }
}
