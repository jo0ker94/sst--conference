package com.example.karlo.sstconference.utility;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.NumberPicker;

import com.example.karlo.sstconference.R;

import net.globulus.easyprefs.EasyPrefs;

public class RadiusPickerUtility {

    private static final String KILOMETERS = "kilometers";
    private static final String METERS = "meters";
    private static final String MILES = "miles";
    private static final String FEET = "feet";

    public interface RadiusPickerListener {
        void onRadiusChanged();
    }

    public static void changeRadiusDialog(Activity context, RadiusPickerListener listener) {
        View view = View.inflate(context, R.layout.radius_picker_dialog, null);
        NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.radius_picker);
        NumberPicker unitsPicker = (NumberPicker) view.findViewById(R.id.units_picker);

        String[] arrayString= new String[]{KILOMETERS, METERS, MILES, FEET};

        unitsPicker.setMinValue(0);
        unitsPicker.setMaxValue(arrayString.length - 1);
        unitsPicker.setDisplayedValues(arrayString);

        float mapRadius = EasyPrefs.getMapRadius(context);
        if (mapRadius < 1000 && mapRadius > 0) {
            unitsPicker.setValue(1);
        }

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(1000);
        numberPicker.setValue(getValueToSelect(context));

        new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle)
                .setTitle(String.format(context.getString(R.string.search_radius),
                        EasyPrefs.getMapRadius(context)))
                .setView(view)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                    long radius = getMetersFromSelection(numberPicker.getValue(), arrayString[unitsPicker.getValue()]);
                    EasyPrefs.putMapRadius(context, radius);
                    AppConfig.MAP_RADIUS = radius;
                    if (listener != null) {
                        listener.onRadiusChanged();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private static int getValueToSelect(Context context) {
        float radius = EasyPrefs.getMapRadius(context);
        if (radius < 1000 && radius > 0) {
            return (int) radius;
        } else {
            return (int) (radius / 1000);
        }
    }

    private static long getMetersFromSelection(int number, String units) {
        switch (units) {
            case KILOMETERS:
                return number * 1000;

            case MILES:
                return number * 1609;

            case FEET:
                return (long) (number * 0.3048);

            default:
                return number;
        }
    }
}
