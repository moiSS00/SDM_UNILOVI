package com.example.unilovi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.unilovi.utils.callbacks.CallBack;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    // Atribitos que contendrán una referencia a los componentes usados
    private EditText editEmail;
    private EditText editPassword;
    private Button signUpButton;

    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Obtenemos referencias a los componentes
        editEmail = (EditText) findViewById(R.id.emailSignUpEdit);
        editPassword = (EditText) findViewById(R.id.passwordSignUpEdit);
        signUpButton = (Button) findViewById(R.id.signUpButton);

        // Asignamos listeners

        // -- Botón para registrarse --
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validacionEntrada()) {
                    String emailContent = editEmail.getText().toString();
                    String passwordContent = editPassword.getText().toString();
                    registrarUsuario(emailContent, passwordContent);
                }
            }
        });

    }

    private boolean validacionEntrada() {
        String emailContent = editEmail.getText().toString();
        String passwordContent = editPassword.getText().toString();
        if (emailContent.isEmpty() || passwordContent.isEmpty()) {
            showAlert("Debe rellenar todos los campos para iniciar sesión");
            return false;
        }
        return true;
    }


    private void showHome() {
        Intent mainIntent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(message);
        builder.setPositiveButton("Aceptar", null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void registrarUsuario(String email, String password) {
        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { // El registro fue un exito
                            showHome();
                        } else { // Hubo algun error
                            showAlert("Hubo un error al registrarse");
                        }
                    }
                });
        createUser(email);
    }

    public void createUser(String email) {

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