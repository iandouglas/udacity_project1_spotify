package com.iandouglas.spotifystreamer.app;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class TracksActivity extends AppCompatActivity {

    public final static String LOG_TAG = "TracksActivity";
    private String artistId;
    private String artistName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);

        if (getIntent().hasExtra(getString(R.string.artist_id))) {
            artistId = getIntent().getStringExtra(getString(R.string.artist_id));
        } else {
            Log.e(LOG_TAG, "failed to get artistId");
            finish();
        }

        if (getIntent().hasExtra(getString(R.string.artist_name))) {
            artistName = getIntent().getStringExtra(getString(R.string.artist_name));
        }

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            TracksFragment tracksFragment = new TracksFragment();
            fragmentTransaction.add(R.id.tracks_container, tracksFragment);
            fragmentTransaction.commit();
        }
    }

    public String getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
