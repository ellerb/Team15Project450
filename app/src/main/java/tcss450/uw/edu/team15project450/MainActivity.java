package tcss450.uw.edu.team15project450;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import tcss450.uw.edu.team15project450.authenticate.SignInActivity;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

//    @Override
//    public void onResume() {
//        super.onResume();
//        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
//        if (mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
//            Intent i = new Intent(this, SignInActivity.class);
//            startActivity(i);
//            finish();
//        }
//    }
//
//    @Override
//    public void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
//        if (mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
//            Intent i = new Intent(this, SignInActivity.class);
//            startActivity(i);
//            finish();
//        }
//    }

    public void goCreateTour(View view) {
        Intent createIntent = new Intent(this, CreateTourActivity.class);
        startActivity(createIntent);
    }

    public void goViewCreatedTours(View view) {
        Intent viewCreatedIntent = new Intent(this, ViewCreatedToursActivity.class);
        startActivity(viewCreatedIntent);
    }

    public void goBrowseTours(View view) {
        Intent browseIntent = new Intent(this, BrowseToursActivity.class);
        startActivity(browseIntent);
    }
}
