package com.example.unilovi;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.unilovi.database.Firebase;
import com.squareup.picasso.Picasso;

public class PostSignUpActivity extends AppCompatActivity {

    private ImageView imagen;
    private Button btnSubirFoto;

    private static final int GALLERY_INTENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_sign_up);

        imagen = (ImageView) findViewById(R.id.imagenPostSignUp);
        btnSubirFoto = (Button) findViewById(R.id.btnSubirFoto);

        btnSubirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCargarFotoActivityForResult();
            }
        });
    }

    public void openCargarFotoActivityForResult() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        cargarFotoActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> cargarFotoActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            Picasso.get().load(intent.getData()).into(imagen);
                            Firebase.uploadImage(intent.getData());
                        }
                    }
                }
            }
    );

}