package es.practicacumn.geochallenge.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Prueba;
import es.practicacumn.geochallenge.R;

public class GenerarPdf extends Service {
    private static final String CHANNEL_ID = "PDF_CHANNEL";
    private static final int NOTIFICATION_ID = 1;
    private NotificationManager notificationManager;
    private String Nombre,Nombrearchivo;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }
    private List<Prueba> listaPruebas;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        listaPruebas= (List<Prueba>) intent.getSerializableExtra("Pruebas");
        Nombre=intent.getStringExtra("Nombre");
        createNotificationChannel();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Generando PDF")
                .setContentText("Progreso...")
                .setSmallIcon(R.mipmap.icono_app_round)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setProgress(listaPruebas.size(), 0, false)
                .setOngoing(true);

        startForeground(NOTIFICATION_ID, notificationBuilder.build());

        Thread thread =new Thread(new Runnable() {
            @Override
            public void run() {
                PdfDocument pdfDocument=new PdfDocument();
                Paint myPaint=new Paint();


                for (int i = 0; i < listaPruebas.size(); i++) {
                    String imageUrl = listaPruebas.get(i).getCodigoQr();
                    Bitmap bitmap = null;
                    try {
                        bitmap = Picasso.get().load(imageUrl).get();

                    } catch (IOException e) {

                        throw new RuntimeException(e);
                    }

                    Bitmap escala = Bitmap.createScaledBitmap(bitmap, 175, 175, false);
                    PdfDocument.PageInfo Page = new PdfDocument.PageInfo.Builder(250, 400,i+1).create();
                    PdfDocument.Page mypage = pdfDocument.startPage(Page);
                    Canvas canvas = mypage.getCanvas();
                    int titleX = 40;
                    int titleY = 50;

                    // Escribir el título en la página
                    String title = "Pista " + (i + 1);
                    canvas.drawText(title, titleX, titleY, myPaint);
                    int imageX = 50;
                    int imageY = 100;
                    canvas.drawBitmap(escala, imageX, imageY, myPaint);
                    pdfDocument.finishPage(mypage);

                    notificationBuilder.setProgress(listaPruebas.size(), i + 1, false);
                    notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
                }

                Nombrearchivo=Nombre+".pdf";
                File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"/"+Nombrearchivo);

                try {
                   if(file.exists()){
                       String nuevoNombre = generarNuevoNombre(Nombrearchivo);
                       File archivoModificado = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), nuevoNombre);
                       pdfDocument.writeTo(new FileOutputStream(archivoModificado));
                       showToast("Creado con exito");
                    }else{
                       pdfDocument.writeTo(new FileOutputStream(file));
                       showToast("Creado con exito");
                   }
                }catch (Exception e){
                    e.printStackTrace();
                    showToast("Error");
                }
                pdfDocument.close();
                notificationBuilder.setContentText("PDF generado")
                        .setProgress(0, 0, false)
                                .setOngoing(true);
                notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
                stopForeground(true);
                stopSelf();
            }
        });
        thread.start();

        return Service.START_STICKY;
    }
    private String generarNuevoNombre(String nombreOriginal) {
        int i = 1;
        String nuevoNombre = nombreOriginal;
        String extension = "";

        int extensionIndex = nombreOriginal.lastIndexOf(".");
        if (extensionIndex != -1) {
            extension = nombreOriginal.substring(extensionIndex);
            nuevoNombre = nombreOriginal.substring(0, extensionIndex);
        }

        // Verifica si el archivo con el nombre modificado ya existe
        File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"/"+nuevoNombre+"("+i+")"+ extension);

        while (file.exists()) {
            i++;
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), nombreOriginal +"(" + i + ")"+ extension);
        }

        return nuevoNombre + "(" + i + ")"+ extension;
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "PDF Channel",
                    NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("Notificación de generación de PDF");
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(false);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showToast(final String message) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationManager.cancel(NOTIFICATION_ID);
    }

}
