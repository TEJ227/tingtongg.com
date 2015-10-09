package com.tingtongg.tingtongg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by TEJ's on 10/8/2015.
 */
public class LocalityDetails extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locality_details);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new LocalityFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.locality_detail, menu);
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
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
    public static class LocalityFragment extends Fragment{

        public LocalityFragment(){

        }
        @Override
        public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){

            View rootView= inflater.inflate(R.layout.fragment_locality_details,container,false);

            Intent loc_detail= getActivity().getIntent();
            if (loc_detail !=null && loc_detail.hasExtra(Intent.EXTRA_TEXT)){
                String resultStr=loc_detail.getStringExtra(Intent.EXTRA_TEXT);
                ((TextView) rootView.findViewById(R.id.locality_details))
                        .setText(resultStr);
            }

            return rootView;
        }
    }
}

