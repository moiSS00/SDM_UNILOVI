package com.example.unilovi.database;


import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.unilovi.utils.CallBack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Firebase {

    // Base de datos
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Modulo de autentificación
    private static FirebaseAuth fAuth = FirebaseAuth.getInstance();

    //Storage de fotos
    private static StorageReference storage = FirebaseStorage.getInstance().getReference();


    // ---- Autenticación ---

    /**
     * Cierra la sesión del usuario actual
     */
    public static void cerrarSesion() {
        fAuth.signOut();
    }

    /**
     * Devuelve el usuario actual
     * @return usuario que esta actualmente logeado
     */
    public static FirebaseUser getUsuarioActual() {
        return fAuth.getCurrentUser();
    }

    /**
     * Método para iniciar sesión
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @param callBack CallBack a ejecutar, recibirá true si no hubo errores  o
     * false si hubo algún error.
     */
    public static void iniciarSesion(String email, String password, CallBack callBack) {
        fAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                callBack.methodToCallBack(true);
            }})
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.methodToCallBack(false);
            }
        });
    }

    /**
     * Registra un usuario en la base de datos
     * @param email Email del usuario a registrar
     * @param password Contraseña del usuario
     * @param callBack CallBack a ejecutar, recibirá true si no hubo errores  o
     * false si hubo algún error.
     */
    public static void registrarUsuario(String email, String password, CallBack callBack) {
        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                callBack.methodToCallBack(true);
            }})
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.methodToCallBack(false);
            }
        });
    }


    // ---- Interactuar directamente con la base de datos ----

    /**
     * Devuelve la lista de ciudades almacenadas en la base de datos
     * @param callBack CallBack a ejecutar, recibirá la lista de ciudades si no hubo errores  o
     * null si hubo algún error.
     */
    public static void getCiudades(CallBack callBack) {
        db.collection("ciudades").document("ciudadesFormularioUsuario").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) { // Si se encontró el documento que contiene a las ciudades
                    callBack.methodToCallBack(documentSnapshot.getData().get("Ciudades"));
                }
                else {
                    callBack.methodToCallBack(null);
                }
            }})
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.methodToCallBack(null);
            }
        });
    }

    /**
     * Devuelve la lista de facultades almacenadas en la base de datos
     * @param callBack CallBack a ejecutar, recibirá la lista de facultades si no hubo errores  o
     * null si hubo algún error.
     */
    public static void getFacultades(CallBack callBack) {
        db.collection("facultades").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) { // Si se ha logrado recolectar todos los documentos de las facultades
                            List<String> facultades = new ArrayList<String>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Para cada documento obtenemos su id (nombre de la facultad)
                                facultades.add(document.getId());
                            }
                            callBack.methodToCallBack(facultades);
                        }
                        else {
                            callBack.methodToCallBack(null);
                        }
                    }})
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.methodToCallBack(null);
            }
        });
    }

    /**
     * Devuelve la lista de carreras que se dan en una facultad específica
     * @param facultad Facultad por la que se quiere filtrar
     * @param callBack CallBack a ejecutar, recibirá la lista de carreras si no hubo errores  o
     * null si hubo algún error.
     */
    public static void getCarrerasByFacultad(String facultad, CallBack callBack) {
        // Buscamos el documento para esa facultad
        db.collection("facultades").document(facultad).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) { // Si existe la facultad
                    callBack.methodToCallBack(documentSnapshot.getData().get("carreras"));
                }
                else { // Si no existe la facultad mostramos valor por defecto
                    callBack.methodToCallBack(null);
                }
            }})
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.methodToCallBack(null);
            }
        });
    }


    /**
     * Crea un usuario en la base de datos (NO EN EL SERVICIO DE AUTENTIFICACIÓN)
     * @param email Email del usuario a crear
     * @param callBack CallBack a ejecutar, recibirá true si no hubo errores  o
     * false si hubo algún error.
     */
    public static void createUser(String email, String nombre, String apellidos, Uri foto, CallBack callBack) {

        // Create a new user
        Map<String, Object> user = new HashMap<>();
        user.put("nombre", nombre);
        user.put("apellidos", apellidos);

        // Añadimos al usuario
        db.collection("usuarios").document(email).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                uploadImage(foto);
                callBack.methodToCallBack(true);
            }})
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.methodToCallBack(false);
            }
        });
    }

    /**
     * Sube una foto al storage
     * @param uri Uri donde está la imagen
     */
    public static void uploadImage(Uri uri) {
        StorageReference filePath = storage.child("images").child(getUsuarioActual().getEmail());

        filePath.putFile(uri);
    }

    /**
     * Descarga la foto del storage
     * @param email Email del usuario que descarga la foto
     */
    public static void downloadImage(String email, CallBack callBack) {
        storage.child("images").child(email).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    callBack.methodToCallBack(task.getResult().toString());
                } else {
                    callBack.methodToCallBack(task.getResult());
                }
            }
        });
    }


}
