package com.kfidria.sweethome;

import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.ProgressDialog;

import android.content.SharedPreferences;

import android.os.AsyncTask;
import android.os.Bundle;

import android.preference.PreferenceManager;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.kfidria.sweethome.helper.AlertManagerDialog;
import com.kfidria.sweethome.helper.ConnectionDetector;
import com.kfidria.sweethome.helper.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends ListFragment {

    private static final String ARGUMENT_ARRAY = "ARRAY";
    private static final String ARGUMENT_ID = "group_id";
    private static final String ARGUMENT_IMAGE = "IMAGE";
    private static final String ARGUMENT_INDEX = "INDEX";
    private static final String ARGUMENT_TITLE = "TITLE";
    private static final String ARGUMENT_VALUE = "VALUE";

    private static final String OWN_12HOURS = "-Last12Hours";
    private static final String OWN_BALKONY = "-balkony";
    private static final String OWN_HUMIDITY = "Humidity";
    private static final String OWN_INDEX = "-index";
    private static final String OWN_LIGHT = "Light_status";
    private static final String OWN_LIST = "Home_Telemetry";
    private static final String OWN_OUTSIDE = "-outside";
    private static final String OWN_PRESSURE = "Pressure";
    private static final String OWN_TEMPERATURE = "Temperature";

    private static final String TAG = MainFragment.class.getSimpleName();
    private static final String TAG_ARRAY = "ARRAY";
    private static final String TAG_ID = "group_id";
    private static final String TAG_IMAGE = "IMAGE";
    private static final String TAG_INDEX = "INDEX";
    private static final String TAG_TITLE = "TITLE";
    private static final String TAG_VALUE = "VALUE";

    private static final String URL_HOME_TELEMETRY = "http://kfidria.homeip.net:25000/telemetry";
    private static final String URL_LIGHT_CHANGE = "http://kfidria.homeip.net:25000/balkony_ligh";
    private static final String URL_LIGHT_STATUS = "http://kfidria.homeip.net:25000/light_status";

    protected ArrayList<HashMap<String, String>> itemsList = new ArrayList<>();

    private int theme;
    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();
    private ConnectionDetector cd;
    private AlertManagerDialog alert = new AlertManagerDialog();

    public MainFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        new LoadHomeTelemetry().execute();

        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(this.getActivity());

        String name1 = prefs.getString("Username", "");
        Log.d(TAG, "NAME "+ name1);
        String theme = prefs.getString("Theme", "");
        Log.d(TAG, "Theme " + theme);

        cd = new ConnectionDetector(getActivity().getApplicationContext());

        // Check for internet connection
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(getActivity(), "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main,
                container,
                false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        super.onListItemClick(l, v, position, id);

        if (position < 3)
        {
            Log.d (TAG, "Position" + String.valueOf(position));
            OneItemFragment nextFragment = new OneItemFragment();

            Bundle args = new Bundle();
            args.putInt(ARGUMENT_ID,position);
            args.putString(ARGUMENT_INDEX,itemsList.get(position).get(TAG_INDEX));
            args.putString(ARGUMENT_TITLE, itemsList.get(position).get(TAG_TITLE));
            args.putString(ARGUMENT_VALUE, itemsList.get(position).get(TAG_VALUE));
            args.putString(ARGUMENT_IMAGE, itemsList.get(position).get(TAG_IMAGE));
            args.putString(ARGUMENT_ARRAY, itemsList.get(position).get(TAG_ARRAY));

            nextFragment.setArguments(args);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,
                            nextFragment,
                            "Item")
                    .addToBackStack("Fragment")
                    .commit();

        }
        if (position == 3){
            new ChangeLight().execute();
        }

    }

    /**
     * Background Async Task to Load by making http request
     * */
    class LoadHomeTelemetry extends AsyncTask<String, String, List<String>> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Listing telemetry ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting JSON
         * */
        protected List<String> doInBackground(String... args) {

            // getting JSON string from URL
            String json = jsonParser.makeHttpRequest(URL_HOME_TELEMETRY, "GET");

            // Check your log cat for JSON reponse
            //Log.d(TAG, "Home Telemetry JSON: "+ "> " + json);

            try {
                JSONObject homeJson = new JSONObject(json).getJSONObject(OWN_LIST);

                JSONObject homeTemperature = homeJson.getJSONObject(OWN_TEMPERATURE);
                double tempOutside = homeTemperature.getDouble(OWN_OUTSIDE);
//                Log.d("Home temperature: ", String.valueOf(tempOutside));

                putLineItem(itemsList,
                        1,
                        OWN_TEMPERATURE,
                        String.valueOf(tempOutside),
                        R.drawable.ic_temp_outside,
                        homeTemperature.getString(OWN_12HOURS),
                        homeTemperature.getString(OWN_INDEX));

                JSONObject homeHumidity = homeJson.getJSONObject(OWN_HUMIDITY);
                int humidityOutside = homeHumidity.getInt(OWN_OUTSIDE);
//                Log.d("Home Humidity: ", String.valueOf(humidityOutside));
                putLineItem(itemsList,
                        2,
                        OWN_HUMIDITY,
                        String.valueOf(humidityOutside),
                        R.drawable.ic_humidity,
                        homeHumidity.getString(OWN_12HOURS),
                        homeHumidity.getString(OWN_INDEX));

                JSONObject homePressure = homeJson.getJSONObject(OWN_PRESSURE);
                int pressureOutside = homePressure.getInt(OWN_OUTSIDE);
//                Log.d("Home Pressure: ", String.valueOf(pressureOutside));
                putLineItem(itemsList,
                        3,
                        OWN_PRESSURE,
                        String.valueOf(pressureOutside),
                        R.drawable.ic_pressure,
                        homePressure.getString(OWN_12HOURS),
                        homePressure.getString(OWN_INDEX));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            json = jsonParser.makeHttpRequest(URL_LIGHT_STATUS, "GET");

            // Check your log cat for JSON reponse
//            Log.d("Home Telemetry JSON: ", "> " + json);

            try {

                JSONObject homeJson =
                        new JSONObject(json)
                                .getJSONObject("Lights")
                                .getJSONObject(OWN_LIGHT);

                double light = homeJson.getDouble(OWN_BALKONY);

                if (light == 0) {
                    putLineItem(itemsList,
                            4,
                            "Light is ",
                            "OFF",
                            R.drawable.ic_light_off,
                            "",
                            "");
                } else {
                    putLineItem(itemsList,
                            4,
                            "Light is ",
                            "ON",
                            R.drawable.ic_light_on,
                            "",
                            "");
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

            return new ArrayList<>();
        }

        protected void putLineItem(ArrayList<HashMap<String, String>> mainList,
                                   int group_id,
                                   String title,
                                   String value,
                                   int image,
                                   String array,
                                   String index) {

            // creating new HashMap
            HashMap<String, String> map = new HashMap<>();

            // adding each child node to HashMap key => value
            map.put(TAG_ARRAY, array);
            map.put(TAG_ID, String.valueOf(group_id));
            map.put(TAG_IMAGE, String.valueOf(image));
            map.put(TAG_TITLE, title);
            map.put(TAG_VALUE, value);
            map.put(TAG_INDEX, index);
            // adding HashList to ArrayList
            mainList.add(map);
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(List<String> result) {
            // dismiss the dialog after getting all albums
            pDialog.dismiss();
            super.onPostExecute(result);
            //SharedPreferences preferences = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);

            ListAdapter adapter = new SimpleAdapter(getActivity(),
                    itemsList,
                    R.layout.list_items,
                    new String[] { TAG_ID,
                            TAG_TITLE,
                            TAG_VALUE,
                            TAG_IMAGE },
                    new int[] {R.id.text_group_id,
                            R.id.text_title,
                            R.id.text_value,
                            R.id.image_item}
            );

            // updating listview
            setListAdapter(adapter);
        }
    }

    /**
     * Background Async Task to Load by making http request
     * */
    class ChangeLight extends AsyncTask<String, String, List<String>> {

        /**
         * getting JSON
         * */
        protected List<String> doInBackground(String... args) {

            // getting JSON string from URL
            String json = jsonParser.makeHttpRequest(URL_LIGHT_CHANGE, "GET");
            try {
                JSONObject homeJson =
                        new JSONObject(json)
                                .getJSONObject("Lights")
                                .getJSONObject(OWN_LIGHT);

                double light = homeJson.getDouble(OWN_BALKONY);

                Log.d(TAG, "Light" + String.valueOf(itemsList.get(3)));

                if (light == 0) {
                    itemsList.get(3)
                            .put(TAG_IMAGE, String.valueOf(R.drawable.ic_light_off));
                    itemsList.get(3)
                            .put(TAG_VALUE, "OFF");

                } else {
                    itemsList.get(3)
                            .put(TAG_IMAGE, String.valueOf(R.drawable.ic_light_on));
                    itemsList.get(3)
                            .put(TAG_VALUE, "ON");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return new ArrayList<>();
        }

         /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(List<String> result) {
            // dismiss the dialog after getting all albums
            super.onPostExecute(result);

            ListAdapter adapter = new SimpleAdapter(getActivity(),
                    itemsList,
                    R.layout.list_items,
                    new String[] { TAG_ID,
                            TAG_TITLE,
                            TAG_VALUE,
                            TAG_IMAGE },
                    new int[] {
                            R.id.text_group_id,
                            R.id.text_title,
                            R.id.text_value,
                            R.id.image_item}
            );

            // updating listview
            setListAdapter(adapter);
        }
    }

}
