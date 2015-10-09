package com.tingtongg.tingtongg;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */

public class LocalitySelectorFragment extends Fragment {

    private ArrayAdapter<String> forecastAdapter;
    public LocalitySelectorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String[] forecastArray = {
                "loading.."
        };
        FetchWeatherTask weatherTask = new FetchWeatherTask();
        weatherTask.execute("1");

        //This is a Comment
        List<String> weekForecast = new ArrayList<String>(Arrays.asList(forecastArray));
        forecastAdapter = new ArrayAdapter<String> (
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                weekForecast
        );
        View rootView = inflater.inflate(R.layout.fragment_locality_selector, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(forecastAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String locality=forecastAdapter.getItem(position);
                Intent loc_detail = new Intent(getActivity(),LocalityDetails.class).putExtra(Intent.EXTRA_TEXT,locality);
                startActivity(loc_detail);
            }
        });
        return rootView;
    }

    // Inner Class
    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {
        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        private String[] getLocalityDataFromJson(String forecastJsonStr)
                throws JSONException {
            final String OWM_LIST = "Locations";
            final String OWM_DESCRIPTION = "location_nm";


            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

            int len = weatherArray.length();
            String[] resultStrs = new String[len];
            int[] idArray=new int[len];
            for(int i =0; i < len; i++) {
                JSONObject location = weatherArray.getJSONObject(i);
                String location_nm = location.getString(OWM_DESCRIPTION);
                int location_id=location.getInt("location_id");
                resultStrs[i] = location_nm ;//+ "\n id=" + location_id;
                idArray[i] = location_id;
            }
            for (String s : resultStrs) {
                Log.v(LOG_TAG, "Forecast entry: " + s);
            }

            Log.v(LOG_TAG, "Forecast entry: Reached here");
            return   resultStrs ;
        }

        @Override
        protected String[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            // String format = "JSON";
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                //URL url = new URL("http://tej227.pythonanywhere.com/location/1/JSON");
                final String FORECAST_BASE_URL = "http://tej227.pythonanywhere.com/location/1/JSON";
              final String QUERY_PARAM = "q";
                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .build();
                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    forecastJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    forecastJsonStr = null;
                }
                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG,"Forecast JSON String:" + forecastJsonStr);
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                forecastJsonStr = null;
                } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    }
                    catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            try {
                return getLocalityDataFromJson(forecastJsonStr);
            } catch(JSONException e) {
                Log.e(LOG_TAG,e.getMessage(),e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null){
                forecastAdapter.clear();
                for(String resultStr : result){
                    forecastAdapter.add(resultStr);
                }
            }
        }
    }
}
