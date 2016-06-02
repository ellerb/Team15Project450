package tcss450.uw.edu.team15project450.browse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import tcss450.uw.edu.team15project450.R;
import tcss450.uw.edu.team15project450.authenticate.SignInActivity;
import tcss450.uw.edu.team15project450.model.Place;
import tcss450.uw.edu.team15project450.model.Tour;

/**
 * This activity is currently a shell to be implemented later. Will allow
 * a user to browse and select available tours to download and take.
 *
 * @author Gabrielle Bly, Gabrielle Glynn
 * @version May 4, 2016
 */
public class BrowseToursActivity extends AppCompatActivity implements TourListFragment.OnListFragmentInteractionListener,
    PlaceFragment.OnPlaceListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_tours);

        if (savedInstanceState == null || getSupportFragmentManager().findFragmentById(R.id.list) == null) {
            TourListFragment tourListFragment = new TourListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.browse_tours_container, tourListFragment)
                    .commit();
        }

        // to hide the floating action button
        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.fabMap);
        floatingActionButton.hide();
    }

    @Override
    public void onListFragmentInteraction(Tour item) {
        PlaceFragment placeFragment = new PlaceFragment();
        Bundle args = new Bundle();
        args.putSerializable(PlaceFragment.TOUR_ITEM_SELECTED, item);
        placeFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.browse_tours_container, placeFragment)
                .addToBackStack(null)
                .commit();

    }

    //@Override
    public void onPlaceListFragmentInteraction(Place item) {
        PlaceDetailFragment placeDetailFragment = new PlaceDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(PlaceDetailFragment.PLACE_ITEM_SELECTED, item);
        placeDetailFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.browse_tours_container, placeDetailFragment) //fragment_container?
                .addToBackStack(null)
                .commit();

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
}
