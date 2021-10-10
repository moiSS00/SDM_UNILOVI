package com.example.unilovi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.unilovi.model.User;

public class UserDetailsActivity extends AppCompatActivity {

    // Atribitos que contendrán una referencia a los componentes usados
    private TextView nombreUsuarioText;
    private TextView emailUsuarioText;
    private TextView edadUsuarioText;
    private TextView escuelaUsuarioText;
    private TextView carreraUsuarioText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        // Obtenemos referencias a los componentes
        nombreUsuarioText = (TextView) findViewById(R.id.nombreUsuarioText);
        emailUsuarioText = (TextView) findViewById(R.id.emailUsuarioText);
        edadUsuarioText = (TextView) findViewById(R.id.edadUsuarioText);
        escuelaUsuarioText = (TextView) findViewById(R.id.escuelaUsuarioText);
        carreraUsuarioText = (TextView) findViewById(R.id.carreraUsuarioText);

        // Recepción de datos
        Intent intent = getIntent();
        User usuarioSeleccionado = intent.getParcelableExtra(UsersRecyclerActivity.USUARIO_SELECCIONADO);
        nombreUsuarioText.setText(usuarioSeleccionado.getName());
        emailUsuarioText.setText(usuarioSeleccionado.getEmail());
        edadUsuarioText.setText(String.valueOf(usuarioSeleccionado.getAge())); // Cuidado con el tipo de la edad
        escuelaUsuarioText.setText(usuarioSeleccionado.getSchool());
        carreraUsuarioText.setText(usuarioSeleccionado.getCareer());
    }
}