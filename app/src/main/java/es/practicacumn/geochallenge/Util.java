package es.practicacumn.geochallenge;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;


public class Util {

        public static int getDistanciaVerticalElementos(Context context, int numElementos) {
            DisplayMetrics medidas = context.getResources().getDisplayMetrics();
            int alturaPantalla = Math.round(medidas.heightPixels / medidas.density + 0.5f) - (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, context.getResources().getDisplayMetrics());
            int distancia = alturaPantalla / numElementos;
            return distancia;
        }
}

