package es.practicacumn.geochallenge.Model;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Comun {
    //OpenWeatherMap
    public static String Base_URL_Weather = "https://api.openweathermap.org/data/2.5/";
    public static String apikey_Weather ="20e3abf61e79685b67d46434c32c00c6";
    public static String units = "metric" ;
    public static String lang="es";

    public static String conversorUnixaHoras(int valor) {
        Date date=new Date(valor*1000L); // Obligo a que se trate como un largo
        SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
        String tiempo = sdf.format(date);
        return tiempo;
    }

    public static int cast(double value){
        int i_val = (int) value;
        return i_val;
    }

    public static String direccionViento(Integer deg) {
        Integer direcciones[] = {0, 90, 180, 270, 360};
        String PuntosCardinales[] = {"N", "E", "S", "W", "N"};
        String Cardinal = null;
        int i = 0;
        boolean encontrado = false;

        while (i < direcciones.length && !encontrado) {
            if (direcciones[i] == deg) {
                Cardinal = PuntosCardinales[i];
                encontrado = true;
            }

            if (direcciones[i] < deg) {
                if (direcciones[i + 1] > deg) {

                    if (PuntosCardinales[i] == "N") {
                        if (PuntosCardinales[i + 1] == "E") {
                            Cardinal = "NE";
                            encontrado = true;
                        }

                    } else
                    if (PuntosCardinales[i] == "E") {
                        if (PuntosCardinales[i + 1] == "S") {
                            Cardinal = "SE";
                            encontrado = true;
                        }
                    } else
                    if (PuntosCardinales[i] == "S") {
                        if (PuntosCardinales[i + 1] == "W") {
                            Cardinal = "SW";
                            encontrado = true;
                        }
                    } else
                    if (PuntosCardinales[i] == "W") {
                        if (PuntosCardinales[i + 1] == "N") {
                            Cardinal = "NW";
                            encontrado = true;
                        }
                    }
                }
                i++;
            }
        }
        return Cardinal;
    }
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

