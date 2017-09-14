package com.pingxundata.pxcore.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Locale;

/**
 * Author:Cow Shed<p/>
 * Created time:2017-03-17 12:45<p/>
 * Description:The date utils helpful
 */

public class DateUtils {

    /**
     * The callback interface of choosing date
     */
    public interface OnChooseDateListener{
        void chooseDate(String value);
    }

    public static void showChooseDateDialog(Context context, final OnChooseDateListener listener){
        final Calendar calendar= Calendar.getInstance();
        DatePickerDialog dialog=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String result= String.format(Locale.US,"%04d-%02d-%02d",year,month+1,dayOfMonth);
                if (listener!=null)
                    listener.chooseDate(result);
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setTitle("请选择日期");
        dialog.show();
    }
}
