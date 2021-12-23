package com.example.unilovi.database;


import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.unilovi.model.Preferences;
import com.example.unilovi.model.User;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Firebase {

    // Base de datos
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Módulo de autentificación
    private static FirebaseAuth fAuth = FirebaseAuth.getInstance();

    // Módulo de multimedia
    private static StorageReference storage = FirebaseStorage.getInstance().getReference();


    // ---- Autenticación ---

    /**
     * Registra a un usuario SOLO en el módulo de autenticación
     * @param email Email del usuario a registrar
     * @param password Contraseña del usuario a registrar
     * @param callBack CallBack a ejecutar
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

    /**
     * Crea un usuario SOLO en la base de datos (NO EN EL SERVICIO DE AUTENTIFICACIÓN)
     * @param user Usuario a crear
     * @param preferences Preferencias del usuario a crear
     * @param uriFoto La uri que indica donde se encuentra la foto de perfil del usuario a crear
     * @param callBack CallBack a ejecutar
     */
    public static void createUser(User user, Preferences preferences, String uriFoto, CallBack callBack) {

        // Create a new user
        Map<String, Object> userParams = new HashMap<>();
        userParams.put("nombre", user.getNombre());
        userParams.put("apellidos", user.getApellidos());
        userParams.put("fechaNacimiento", user.getFechaNacimiento());
        userParams.put("sexo", user.getSexo());
        userParams.put("facultad", user.getFacultad());
        userParams.put("carrera", user.getCarrera());
        userParams.put("sobreMi", user.getSobreMi());
        userParams.put("contacto", user.getFormaContacto());
        userParams.put("solicitudes", user.getSolicitudes());
        userParams.put("matches", user.getMatches());

        // Create preferences for user
        Map<String, Object> userPreferences = new HashMap<>();
        userPreferences.put("edadMinima", preferences.getEdadMinima());
        userPreferences.put("edadMaxima", preferences.getEdadMaxima());
        userPreferences.put("sexoBusqueda", preferences.getSexos());
        userPreferences.put("facultad", preferences.getFacultad());
        userPreferences.put("carrera", preferences.getCarrera());

        // Añadimos al usuario
        db.collection("usuarios").document(user.getEmail()).set(userParams)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        db.collection("preferencias").document(user.getEmail()).set(userPreferences)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        uploadImage(Uri.parse(uriFoto));
                                        callBack.methodToCallBack(true);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                callBack.methodToCallBack(false);
                            }
                        });
                    }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callBack.methodToCallBack(false);
                    }
                });
    }

    /**
     * Método para iniciar sesión
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @param callBack CallBack a ejecutar
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
     * Comprueba si es la primera vez que el usuario actual inicia sesion
     * @param callBack Callback a ejecutar
     */
    public static void existUser(CallBack callBack) {
        db.collection("usuarios").document(getUsuarioActual().getEmail()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            callBack.methodToCallBack(true);
                        }
                        else {
                            callBack.methodToCallBack(false);
                        }
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
     * Devuelve la información de un usuario en concreto
     * @param email Email del usuario
     */
    public static void getUsuarioByEmail(String email, CallBack callBack) {
        // Buscamos el documento para ese email
        db.collection("usuarios").document(email).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) { // Si existe el email
                            Map<String, Object> datos = documentSnapshot.getData();
                            User user = new User();
                            user.setEmail(documentSnapshot.getId());
                            user.setNombre(datos.get("nombre").toString());
                            user.setApellidos(datos.get("apellidos").toString());
                            user.setFechaNacimiento(datos.get("fechaNacimiento").toString());
                            user.setSexo(datos.get("sexo").toString());
                            user.setFacultad(datos.get("facultad").toString());
                            user.setCarrera(datos.get("carrera").toString());
                            user.setSobreMi(datos.get("sobreMi").toString());
                            user.setFormaContacto(datos.get("contacto").toString());
                            callBack.methodToCallBack(user);
                        }
                        else { // Si el email no existe
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
     * Devuelve la lista de ciudades almacenadas en la base de datos
     * @param callBack CallBack a ejecutar
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
     * @param callBack CallBack a ejecutar
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
     * @param callBack CallBack a ejecutar
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
     * Devuelve las preferencias de un usuario concreto
     * @param email Email del usuario
     */
    public static void getPreferencesByEmail(String email, CallBack callBack) {
        // Buscamos el documento para ese email
        db.collection("preferencias").document(email).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) { // Si existe el email
                            Map<String, Object> datos = documentSnapshot.getData();
                            Preferences preferences = new Preferences();
                            preferences.setFacultad(datos.get("facultad").toString());
                            preferences.setCarrera(datos.get("carrera").toString());
                            preferences.setEdadMaxima(Integer.parseInt(datos.get("edadMaxima").toString()));
                            preferences.setEdadMinima(Integer.parseInt(datos.get("edadMinima").toString()));
                            preferences.setSexos((ArrayList<String>) datos.get("sexoBusqueda"));
                            callBack.methodToCallBack(preferences);
                        }
                        else { // Si el email no existe
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
     * Actualiza las preferencias de un usuario concreto
     * @param email Email del usuario al que queremos cambiarle las preferencias
     * @param callBack CallBack a ejecutar
     */
    public static void updatePreferences(String email, Preferences preferences, CallBack callBack) {

        Map<String, Object> newPreferences = new HashMap<>();
        newPreferences.put("edadMinima", preferences.getEdadMinima());
        newPreferences.put("edadMaxima", preferences.getEdadMaxima());
        newPreferences.put("sexoBusqueda", preferences.getSexos());
        newPreferences.put("facultad", preferences.getFacultad());
        newPreferences.put("carrera", preferences.getCarrera());

        db.collection("preferencias").document(email).update(newPreferences).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                callBack.methodToCallBack(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.methodToCallBack(false);
            }
        });
    }

    /**
     * Devuelve una lista con los emails de los pretendientes en funcion de las preferencias seleccionadas
     * @param  preferences Preferencias que se usarán para buscar pretendientes
     * @param  callBack Callback a ejecutar
     */
    public static void getPretendientesByPreferences(Preferences preferences, CallBack callBack) { }

    // ---- Interactuar directamente con el módulo multimedia ----

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
                    callBack.methodToCallBack(null);
                }
            }
        });
    }


}
