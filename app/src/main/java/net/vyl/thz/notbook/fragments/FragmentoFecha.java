package net.vyl.thz.notbook.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import net.vyl.thz.notbook.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class FragmentoFecha extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    TextView textView;
    Spinner spinner;
    MainActivity mainActivity;

    public FragmentoFecha(View view, Context context) {
        if (view instanceof TextView){
            this.textView = (TextView) view;
        } else{
            this.spinner = (Spinner) view;
        }
        if (context instanceof MainActivity){
            mainActivity = (MainActivity) context;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        FragmentoHora fragmentoHora;
        month += 1;
        String fecha = year + "-" + (month<10?"0"+month:month+"") + "-" + (day<10?"0"+day:day+"");
        if (textView != null) {
            textView.setText(fecha);
            fragmentoHora = new FragmentoHora(textView);
        }else{
            ((ArrayAdapter)spinner.getAdapter()).add(fecha);
            Log.d("Prueba", "onDateSet: " + ((ArrayAdapter)spinner.getAdapter()).getCount());
            fragmentoHora = new FragmentoHora(spinner);
        }
        fragmentoHora.show(mainActivity.getSupportFragmentManager(), "timePicker");
    }

}
