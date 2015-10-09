package com.tingtongg.tingtongg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class LocalitySelector extends AppCompatActivity {
    private static String VALUE = "myValue";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locality_selector);
        String value = getIntent().getExtras().getString(VALUE);
        Toast.makeText(this, value, Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_locality_selector, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
//            LocalitySelectorFragment myOuter = new LocalitySelectorFragment();
//            LocalitySelectorFragment.FetchWeatherTask weatherTask = myOuter.new FetchWeatherTask();
//            weatherTask.execute("1");
            return true;
        }
        if (id == R.id.action_settings){
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
