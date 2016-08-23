package com.kfidria.sweethome;

import android.app.FragmentManager;
import android.app.ListFragment;

import android.os.Bundle;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.components.XAxis;

import com.kfidria.sweethome.helper.MyValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class OneItemFragment extends ListFragment {

    private static final String ARGUMENT_ARRAY = "ARRAY";
    private static final String ARGUMENT_ID = "group_id";
    private static final String ARGUMENT_IMAGE = "IMAGE";
    private static final String ARGUMENT_INDEX = "INDEX";
    private static final String ARGUMENT_TITLE = "TITLE";
    private static final String ARGUMENT_VALUE = "VALUE";

    private static final String TAG = OneItemFragment.class.getSimpleName();
    private static final String TAG_ID = "group_id";
    private static final String TAG_IMAGE = "IMAGE";
    private static final String TAG_TITLE = "TITLE";
    private static final String TAG_VALUE = "VALUE";

    private final ArrayList<HashMap<String, String>> itemsList
            = new ArrayList<>();

    public OneItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_oneitem,
                container,
                false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        MainFragment nextFragment = new MainFragment();

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, nextFragment)
                .commit();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();

        HashMap<String, String> map = new HashMap<>();
        // adding each child node to HashMap key => value
        map.put(TAG_ID, String.valueOf(bundle.getInt(ARGUMENT_ID)));
        map.put(TAG_TITLE, bundle.getString(ARGUMENT_TITLE));
        map.put(TAG_VALUE, bundle.getString(ARGUMENT_VALUE));
        map.put(TAG_IMAGE, bundle.getString(ARGUMENT_IMAGE));
        // adding HashList to ArrayList
        itemsList.add(map);

        Log.d(TAG, "Array"+ bundle.getString(ARGUMENT_ARRAY) );
        LineChart chart = (LineChart) getActivity().findViewById(R.id.chart1);
        List<String> chartValues = sortArray(bundle.getString(ARGUMENT_ARRAY),
                Integer.valueOf(bundle.getString(ARGUMENT_INDEX)) );

        ArrayList<Entry> values = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();
        Log.d(TAG,  "Array after sort " + String.valueOf(chartValues ));
        double max = 0;
        for (int i = 0; i < chartValues.size(); i++) {
            //Log.d(TAG, "Value" + chartValues.get(i) );
            Entry val =
                    new Entry(Float.valueOf(chartValues.get(i)), i);
            if (Double.valueOf(chartValues.get(i)) > max){
                max = Double.valueOf(chartValues.get(i));
            }
            values.add(val);
            xVals.add(String.valueOf(i));
        }
        xVals.add("end");
        double min = max;
        for (int i = 0; i < chartValues.size(); i++) {
            if (Double.valueOf(chartValues.get(i)) < min){
                min = Double.valueOf(chartValues.get(i));
            }
        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        LineDataSet lds = new LineDataSet(values, "");
        lds.setLineWidth(3f);
        lds.setCircleRadius(3f);
        lds.setDrawCubic(true);
        lds.setDrawCircles(false);
        int color = getResources().getColor(R.color.yellow);
        lds.setValueTextColor(color);
        lds.setValueTextSize(15f);
        lds.setValueFormatter(new MyValueFormatter());
        lds.setColor(color);

        dataSets.add(lds);

        LineData data = new LineData(xVals, dataSets);

        chart.getXAxis()
                .setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis()
                .setAxisMinValue(-0.5f);
        chart.getXAxis()
                .setDrawLabels(false);
        if (max != min) {
            chart.getAxisLeft()
                    .setAxisMinValue((float) (min - 1 / (max - min)));
            chart.getAxisRight()
                    .setAxisMinValue((float) (min - 1 / (max - min)));
            if (min > 0) {
                chart.getAxisLeft()
                        .setAxisMaxValue((float) (max+1/(max-min)));
                chart.getAxisRight()
                        .setAxisMaxValue((float) (max+1/(max-min)));
            } else {
                chart.getAxisLeft()
                        .setAxisMaxValue((float) (max-0.05*min));
                chart.getAxisRight()
                        .setAxisMaxValue((float) (max-0.05*min));
            }
        } else {
            chart.getAxisLeft()
                    .setAxisMinValue((float) (min * 0.95 ));
            chart.getAxisRight()
                    .setAxisMinValue((float) (min * 0.95 ));
            if (min > 0) {
                chart.getAxisLeft()
                        .setAxisMaxValue((float) (min * 1.05));
                chart.getAxisRight()
                        .setAxisMaxValue((float) (min * 1.05));
            } else {
                chart.getAxisLeft()
                        .setAxisMaxValue((float) (0.95*min));
                chart.getAxisRight()
                        .setAxisMaxValue((float) (0.95*min));
            }
        }


        chart.getAxisRight()
                .setTextColor(color);
        chart.getAxisLeft()
                .setTextColor(color);

        Log.d(TAG, "Min" + String.valueOf(min));
        Log.d(TAG, "Max" + String.valueOf(max));
        chart.getAxisLeft()
                .setDrawGridLines(false);

        chart.setNoDataTextDescription("Chart not possible to build");
        chart.setTouchEnabled(false);
        chart.setScaleEnabled(true);
        chart.setDescription("");
        chart.getLegend().setEnabled(false);
        chart.setData(data);
        chart.invalidate(); // refresh

        ListAdapter adapter = new SimpleAdapter(getActivity(),
                itemsList,
                R.layout.list_items,
                new String[] { TAG_ID,
                        TAG_TITLE,
                        TAG_VALUE,
                        TAG_IMAGE},
                new int[] {R.id.text_group_id,
                        R.id.text_title,
                        R.id.text_value,
                        R.id.image_item});

        // updating listview
        setListAdapter(adapter);

        //chart = (LineChart) this.findViewById(R.id.chart1);

    }

    private List<String> sortArray(String param, int tempIndex) {

        List<String> myList =
                new ArrayList<>(Arrays.asList(param.substring(1,
                        param.length() - 1).split(",")));

        List<String> myList2 =
                new ArrayList<>();

        for (int i = tempIndex + 1; i < myList.size(); i++) {
            if (!myList.get(i).contentEquals("\"\"")) {
                myList2.add(myList.get(i));
            }
        }
        for (int i = 0; i <= tempIndex; i++) {
            if (!myList.get(i).contentEquals("\"\"")) {
                myList2.add(myList.get(i));
            }
        }
        return myList2;
    }


}