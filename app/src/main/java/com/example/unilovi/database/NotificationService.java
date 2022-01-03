package com.example.unilovi.database;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.unilovi.MainActivity;
import com.example.unilovi.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Map;

public class NotificationService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Firebase.db.collection("usuarios").document(Firebase.getUsuarioActual().getEmail()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.i("ErrorListener", "Listen failed.", error);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.i("OkListener", "Datos: " + snapshot.getData());
                    Map<String, Object> datos = snapshot.getData();

                    for (String solicitud : (ArrayList<String>) datos.get("solicitudes")) {
                        if (!Firebase.solicitudesUsuarioActual.contains(solicitud)) {
                            // Creamos notificación
                            if (Firebase.context != null) {
                                Intent intent = new Intent(Firebase.context, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                PendingIntent pendingIntent = PendingIntent.getActivity(Firebase.context, 0, intent, 0);

                                Notification notification = new NotificationCompat.Builder(Firebase.context, "canal")
                                        .setSmallIcon(R.drawable.icono_432)
                                        .setContentTitle("Unilovi")
                                        .setContentText("Tienes una nueva solicitud")
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                        .setColor(Color.GREEN)
                                        .setContentIntent(pendingIntent)
                                        .setAutoCancel(true)
                                        .build();
                                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Firebase.context);

                                notificationManager.notify(1, notification);

                                Firebase.solicitudesUsuarioActual.add(solicitud);
                            }
                        }
                    }

                    for (String match : (ArrayList<String>) datos.get("matches")) {
                        if (!Firebase.matchesUsuarioActual.contains(match)) {
                            // Creamos notificación
                            if (Firebase.context != null) {
                                Intent intent = new Intent(Firebase.context, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                PendingIntent pendingIntent = PendingIntent.getActivity(Firebase.context, 0, intent, 0);

                                Notification notification = new NotificationCompat.Builder(Firebase.context, "canal")
                                        .setSmallIcon(R.drawable.icono_432)
                                        .setContentTitle("Unilovi")
                                        .setContentText("Tienes un nuevo match")
                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                        .setColor(Color.GREEN)
                                        .setContentIntent(pendingIntent)
                                        .setAutoCancel(true)
                                        .build();
                                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Firebase.context);

                                notificationManager.notify(1, notification);

                                Firebase.matchesUsuarioActual.add(match);
                            }
                        }
                    }

                } else {
                    Log.i("NullListener", "Datos devuelto por el listener: null");
                }
            }
        });

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
