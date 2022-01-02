package com.example.unilovi.database;


import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.unilovi.R;
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
import com.google.firebase.firestore.Query;
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
        Map<String, Object> userParams = createMapFromUser(user);

        // Create preferences for user
        Map<String, Object> userPreferences = createMapFromPrefernces(preferences);

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
                            datos.put("email",documentSnapshot.getId());
                            callBack.methodToCallBack(createUserFromMap(datos));
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
                            Preferences preferences = createPreferencesFromMap(datos);
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
     * Actualiza la información de un usuario en concreto
     * @param email Email del usuario al que queremos cambiarle las preferencias
     * @param user Objeto user que contiene la nueva información
     * @param callBack CallBack a ejecutar
     */
    public static void updateUser(String email, User user, CallBack callBack) {

        Map<String, Object> newUser = createMapFromUser(user);

        db.collection("usuarios").document(email).update(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
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
     * Actualiza las preferencias de un usuario concreto
     * @param email Email del usuario al que queremos cambiarle las preferencias
     * @param preferences Objeto preferences que contiene la nueva información
     * @param callBack CallBack a ejecutar
     * Firebase no es bueno con operaciones con arrays. Las operaciones notIn y Contains solo admiten
     * hasta 10 valores de comparación, no se pueden anidar ciertas consultas y no se da soporte para la
     * operación ArrayNotContains.
     */
    public static void updatePreferences(String email, Preferences preferences, CallBack callBack) {

        Map<String, Object> newPreferences = createMapFromPrefernces(preferences);

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
    public static void getPretendientesByPreferences(Preferences preferences, CallBack callBack) {

        // Obtenemos el email del usuario actual
        String emailUsuarioActual = Firebase.getUsuarioActual().getEmail();

        // Recuperamos la información del usuario actual
        Firebase.getUsuarioByEmail(emailUsuarioActual, new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                if (object != null) {

                    User usuarioActual = (User) object;

                    // Preparamos las consultas para filtrar.
                    Query consulta = db.collection("usuarios")
                            .whereIn("sexo", preferences.getSexos());

                    if (preferences.getCarrera().isEmpty()) {
                        consulta = consulta
                                .whereEqualTo("facultad",preferences.getFacultad());
                        // Todas las preferencias están configuradas
                    } else {
                        consulta = consulta
                                .whereEqualTo("facultad", preferences.getFacultad())
                                .whereEqualTo("carrera", preferences.getCarrera());
                    }

                    // Realizamos la consulta
                    consulta.get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                    boolean found = false;
                                    ArrayList<User> pretendientes = new ArrayList<>();

                                    // Para cada pretendiente encontrado
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                                        // Se comprueba que no sea el usuario actual
                                        if (!document.getId().equals(emailUsuarioActual)) {

                                            // Un email puede estar en
                                            // Matches -> Si hemos tenido match con el
                                            // SolicitudesRecibidas -> Si nos ha mandado una solicitud
                                            // NoVisibles -> Si lo hemos rechazado o le hemos mandado una petición
                                            if (!usuarioActual.getMatches().contains(document.getId())
                                                    && !usuarioActual.getSolicitudes().contains(document.getId())
                                                    && !usuarioActual.getRechazados().contains(document.getId())) {

                                                // Obtenemos los datos del posible pretendiente
                                                Map<String, Object> datos = document.getData();
                                                datos.put("email", document.getId());
                                                User pretendiente = createUserFromMap(datos);
                                                Log.d("pretendiente", pretendiente.toString());

                                                // Se comprueba si su edad este en el intervalo deseado
                                                if (pretendiente.getEdad() >= preferences.getEdadMinima() &&
                                                        pretendiente.getEdad() <= preferences.getEdadMaxima()) {
                                                    pretendientes.add(pretendiente);
                                                    found = true;
                                                }
                                            }
                                        }
                                    }
                                    if (found) {
                                        callBack.methodToCallBack(pretendientes);
                                    } else {
                                        callBack.methodToCallBack(null);
                                    }
                                }})
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    callBack.methodToCallBack(null);
                                }});
                }
                else {
                    callBack.methodToCallBack(null);
                }
            }
        });
    }

    /**
     * Devuelve la lista de matches del usuario actual
     * @param callBack Callback a ejecutar
     */
    public static void getMatches(CallBack callBack) {
        Firebase.getUsuarioByEmail(Firebase.getUsuarioActual().getEmail(), new CallBack() {
            @Override
            public void methodToCallBack(Object object) {
                if (object != null) {
                    User usuarioActual = (User) object;
                    List<String> matchesEmails = usuarioActual.getMatches();
                    ArrayList<User> matches = new ArrayList<User>();
                    Query consulta = db.collection("usuarios").whereIn("email", matchesEmails);
                    consulta.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                Map<String, Object> datos = document.getData();
                                User match = createUserFromMap(datos);
                                matches.add(match);
                            }
                            callBack.methodToCallBack(matches);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            callBack.methodToCallBack(new ArrayList<User>());
                        }
                    });
                }
                else {
                    callBack.methodToCallBack(new ArrayList<User>());
                }
            }
        });
    }

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


    // ---- Métodos auxiliares ----

    /**
     * Método que convierte de User a map
     * @param user Usuario a convertir
     * @return Map que representa al usuario
     */
    private static Map<String, Object> createMapFromUser(User user) {
        Map<String, Object> mapUser = new HashMap<>();
        mapUser.put("email", user.getEmail());
        mapUser.put("nombre", user.getNombre());
        mapUser.put("apellidos", user.getApellidos());
        mapUser.put("fechaNacimiento", user.getFechaNacimiento());
        mapUser.put("sexo", user.getSexo());
        mapUser.put("facultad", user.getFacultad());
        mapUser.put("carrera", user.getCarrera());
        mapUser.put("sobreMi", user.getSobreMi());
        mapUser.put("contacto", user.getFormaContacto());
        mapUser.put("solicitudes", user.getSolicitudes());
        mapUser.put("matches", user.getMatches());
        mapUser.put("rechazados", user.getRechazados());
        return mapUser;
    }

    /**
     * Método que convierte de map a user
     * @param datos Datos del usuario
     * @return Objeto usuario con los datos
     */
    private static User createUserFromMap(Map<String, Object> datos) {
        User user = new User();
        user.setEmail(datos.get("email").toString());
        user.setNombre(datos.get("nombre").toString());
        user.setApellidos(datos.get("apellidos").toString());
        user.setFechaNacimiento(datos.get("fechaNacimiento").toString());
        user.setSexo(datos.get("sexo").toString());
        user.setFacultad(datos.get("facultad").toString());
        user.setCarrera(datos.get("carrera").toString());
        user.setSobreMi(datos.get("sobreMi").toString());
        user.setFormaContacto(datos.get("contacto").toString());
        user.setSolicitudes((List<String>) datos.get("solicitudes"));
        user.setMatches((List<String>) datos.get("matches"));
        user.setRechazados((List<String>) datos.get("rechazados"));
        return user;
    }

    /**
     * Método que convierte de preferences a map
     * @param preferences Preferencias a convertir
     * @return Map que representa a las preferencias
     */
    private static Map<String, Object> createMapFromPrefernces(Preferences preferences) {
        Map<String, Object> mapPreferences = new HashMap<>();
        mapPreferences.put("edadMinima", preferences.getEdadMinima());
        mapPreferences.put("edadMaxima", preferences.getEdadMaxima());
        mapPreferences.put("sexoBusqueda", preferences.getSexos());
        mapPreferences.put("facultad", preferences.getFacultad());
        mapPreferences.put("carrera", preferences.getCarrera());
        return mapPreferences;
    }

    /**
     * Método que convierte de map a preferences
     * @param datos Datos de las preferencias
     * @return Objeto preferences con los datos
     */
    private static Preferences createPreferencesFromMap(Map<String, Object> datos) {
        Preferences preferences = new Preferences();
        preferences.setFacultad(datos.get("facultad").toString());
        preferences.setCarrera(datos.get("carrera").toString());
        preferences.setEdadMaxima(Integer.parseInt(datos.get("edadMaxima").toString()));
        preferences.setEdadMinima(Integer.parseInt(datos.get("edadMinima").toString()));
        preferences.setSexos((ArrayList<String>) datos.get("sexoBusqueda"));
        return preferences;
    }
}
