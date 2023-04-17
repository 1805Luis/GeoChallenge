package es.practicacumn.geochallenge.Model;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Comun {
    public static void InicializarFecha(EditText fechaImportante, Context pantalla) {
        Calendar calendar=Calendar.getInstance();
        //cuadro de diálogo simple que contiene un archivo DatePicker
        DatePickerDialog.OnDateSetListener date=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                updateCalendar();
            }
            private void updateCalendar(){
                String Format="dd/MM/yyyy";
                SimpleDateFormat sdf= new SimpleDateFormat(Format);
                fechaImportante.setText(sdf.format(calendar.getTime()));
            }
        };
        fechaImportante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(pantalla,date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

}

