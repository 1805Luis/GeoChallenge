package es.practicacumn.geochallenge.Service;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.practicacumn.geochallenge.HubJugando;
import es.practicacumn.geochallenge.Model.UsuarioGymkhana.Gymkhana.Gymkhana;
import es.practicacumn.geochallenge.R;

public class GymkhanaService extends Service {
    private Context thisContext=this;
    private List<Gymkhana> gymkhanasList;
    private FirebaseUser user;
    private String userId;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;
    private PendingIntent pendingIntent;
    private Handler handler;
    private Runnable runnable;

    @Override
    public void onCreate() {
        super.onCreate();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        gymkhanasList = new ArrayList<>();
        handler=new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {
                estaApuntado();
                //Repetir el proceso cada 1 minuto
                    handler.postDelayed(runnable, 1*60*1000);
                }
            };
        }

    private void estaApuntado() {
        List<Gymkhana> gymkhanasList = new ArrayList<>();

        DatabaseReference gymkhanaReference=FirebaseDatabase.getInstance().getReference();
        Query query=gymkhanaReference.child("Gymkhana").orderByChild("participantes/"+userId).equalTo(true);

       query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gymkhanasList.clear();
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Gymkhana gymkhana = dataSnapshot.getValue(Gymkhana.class);
                        gymkhanasList.add(gymkhana);
                    }
                    if(gymkhanasList.size()>0){
                        for (Gymkhana gymkhana: gymkhanasList){
                            String FechaInicio=gymkhana.getDiaInicio();
                            String HoraInicio=gymkhana.getHoraInicio();
                            String gymkhanaID=gymkhana.getId();
                            if(esLaHora(FechaInicio,HoraInicio)){

                                Actualizar(gymkhanaID);
                                Estado(gymkhana.getId());

                            }
                        }

                    }

                }else{
                    onDestroy();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void Actualizar(String gymkhanaID) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Gymkhana/" + gymkhanaID +"/participantes");
        ref.child(userId).setValue(false);
    }

    private void Estado(String id){
        if (AppInForeground()) {
            lanzarAlerta(id);

        } else {
            crearCanal();
        }
    }

    private void lanzarAlerta(String id) {
        Intent intent=new Intent("LanzarAlerta");
        intent.putExtra("IdGymkhana",id);
        sendBroadcast(intent);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.postDelayed(runnable,0);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);

    }


    private boolean esLaHora(String diaInicio, String horaInicio) {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Calendar fechaHoraActual = Calendar.getInstance();
        try {
            Date fechaHoraInicio = formatoFecha.parse(diaInicio + " " + horaInicio);
            Calendar calFechaHoraInicio = Calendar.getInstance();
            calFechaHoraInicio.setTime(fechaHoraInicio);
            calFechaHoraInicio.set(Calendar.SECOND, 0);
            return calFechaHoraInicio.getTime().compareTo(fechaHoraActual.getTime())<=0;
        } catch (ParseException e) {
            return false;
        }

    }

    private void crearCanal() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel =new NotificationChannel(CHANNEL_ID,"Notificacion", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            crearNotificación();
        }else{
            crearNotificación();
        }
    }
    private void crearNotificación() {
        setPendingIntent(HubJugando.class);
        Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(getApplicationContext(), CHANNEL_ID);
        }
        builder.setSmallIcon(R.mipmap.icono_app_round);
        builder.setContentTitle("LA GYMKHANA A COMENZADO");
        builder.setContentText("¡Únete ahora!");
        builder.setColor(Color.BLUE);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setLights(Color.MAGENTA, 1000, 1000);
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
    }
    private void setPendingIntent(Class<?> classActivity) {
        Intent intent = new Intent(this,classActivity);
        TaskStackBuilder stackBuilder=TaskStackBuilder.create(this);
        stackBuilder.addParentStack(classActivity);
        stackBuilder.addNextIntent(intent);
        pendingIntent=stackBuilder.getPendingIntent(1,PendingIntent.FLAG_IMMUTABLE);
    }


    //Primer plano o segundo plano
    private boolean AppInForeground() {
        ActivityManager activityManager=(ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfo=activityManager.getRunningTasks(1);
        if(!runningTaskInfo.isEmpty()){
            ComponentName topActivity=runningTaskInfo.get(0).topActivity;
            return topActivity.getPackageName().equals(getPackageName());
        }
        return false;
    }





    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
