package com.zse233.classtable.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.zse233.classtable.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FirstDayChooseDialog extends DialogFragment {
    private CalendarView calendar;
    private Calendar c;
    private SharedPreferences shp;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View inflater = requireActivity().getLayoutInflater().inflate(R.layout.date_picker, null);
        shp = getActivity().getSharedPreferences("first_day", Context.MODE_PRIVATE);
        calendar = inflater.findViewById(R.id.calendarView);
        String firstDay = shp.getString("start", "1970-01-01");
        Date start;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            start = dateFormat.parse(firstDay);
        } catch (ParseException e) {
            start = new Date();
            Log.d("TError", "" + e.getMessage());
        }
        c = Calendar.getInstance();
        c.setTime(start);
        calendar.setDate(c.getTimeInMillis());
        final SharedPreferences.Editor editor = shp.edit();
        final int[] chosenDate = {c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)};
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                chosenDate[0] = year;
                chosenDate[1] = month + 1;
                chosenDate[2] = dayOfMonth;
            }
        });

        builder.setView(inflater);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editor.putString("start", "" + chosenDate[0] + "-" + chosenDate[1] + "-" + chosenDate[2]);
                editor.apply();
            }
        });
        return builder.create();
    }
}
