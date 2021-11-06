package com.example.unilovi.firebase;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DBUtils {

    // Base de datos
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void createUser(String email) {

        // Create a new user
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);

        // Añadimos al usuario
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Usuario", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Usuario", "Error adding document", e);
                    }
                });
    }
}
