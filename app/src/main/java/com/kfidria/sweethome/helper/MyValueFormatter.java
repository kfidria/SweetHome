package com.kfidria.sweethome.helper;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by kfidria on 19.07.2016.
 */
public class MyValueFormatter implements ValueFormatter {

    private final DecimalFormat mFormat;

    public MyValueFormatter() {
        mFormat = new DecimalFormat("#0.#"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        // write your logic here
        return mFormat.format(value);
    }
}
