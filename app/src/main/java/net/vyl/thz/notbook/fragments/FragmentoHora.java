package net.vyl.thz.notbook.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class FragmentoHora extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    TextView textView;
    Spinner spinner;

    public FragmentoHora(View view) {
        if (view instanceof TextView){
            this.textView = (TextView) view;
        } else{
            this.spinner = (Spinner) view;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String hora = (hourOfDay<10?"0"+hourOfDay:hourOfDay+"") + ":" + (minute<10?"0"+minute:minute+"");
        if (textView != null) {
            textView.setText(textView.getText() + " " + hora);
        }else{
            Log.d("Prueba", "onTimeSet: " + ((ArrayAdapter)spinner.getAdapter()).getCount());
            String a = ((ArrayAdapter)spinner.getAdapter()).getItem(((ArrayAdapter)spinner.getAdapter()).getCount()-1).toString();
            ((ArrayAdapter)spinner.getAdapter()).remove(((ArrayAdapter)spinner.getAdapter()).getItem(((ArrayAdapter)spinner.getAdapter()).getCount()-1));
            ((ArrayAdapter)spinner.getAdapter()).add(a + " " + hora);
        }
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        if (textView != null) {
            textView.setText("Fecha de cumplimiento");
        }else{
            ((ArrayAdapter)spinner.getAdapter()).remove(((ArrayAdapter)spinner.getAdapter()).getItem(((ArrayAdapter)spinner.getAdapter()).getCount()-1));
        }
    }
}
