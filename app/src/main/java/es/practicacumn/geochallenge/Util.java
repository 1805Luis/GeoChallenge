package es.practicacumn.geochallenge;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.WindowManager;


public class Util {

    public static int getDistanciaVerticalElementos(Context context, int numElementos){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int screenSize = context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        int distancia, alturaPantalla;

        int heightPx = metrics.heightPixels;
        float density = metrics.density;
        alturaPantalla =  (int) (heightPx / density);

        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                distancia = alturaPantalla / numElementos;
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                distancia = alturaPantalla / numElementos;
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                distancia = alturaPantalla / numElementos;
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                distancia = alturaPantalla / numElementos;
                break;
            default:
                distancia = 20;
        }
        return distancia;
}
}
