package com.example.ecopontos.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecopontos.R;
import com.example.ecopontos.database.FirestoreManager;
import com.example.ecopontos.model.Usuario;
import com.example.ecopontos.profile.PerfilActivity;
import com.example.ecopontos.tasks.TarefasActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirestoreManager fManager;
    private EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        fManager = new FirestoreManager();

        etEmail = findViewById(R.id.emailEditText);
        etPassword = findViewById(R.id.passwordEditText);
        Button btnLogin = findViewById(R.id.loginButton);
        TextView tvRegisterLink = findViewById(R.id.registerLinkTextView);

        btnLogin.setOnClickListener(v -> fazerLogin());
        tvRegisterLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
            startActivity(intent);
        });
    }

    private void fazerLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            buscarDadosUsuario(user.getUid());
                        }
                    } else {
                        String error = task.getException() != null ? task.getException().getMessage() : "Erro desconhecido";
                        Toast.makeText(LoginActivity.this, "Erro ao fazer login: " + error,
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void buscarDadosUsuario(String uid) {
        fManager.buscarUsuario(uid).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    String nome = document.getString("nome");
                    String municipio = document.getString("municipio");
                    Long pontosLong = document.getLong("pontos");
                    Long pontosTotaisLong = document.getLong("pontosTotais");

                    int pontos = (pontosLong != null) ? pontosLong.intValue() : 0;
                    int pontosTotais = (pontosTotaisLong != null) ? pontosTotaisLong.intValue() : 0;

                    Usuario usuario = new Usuario(uid, nome, municipio, pontos, pontosTotais);
                    abrirTarefas(usuario);
                } else {
                    Toast.makeText(LoginActivity.this, "Dados do usuário não encontrados", Toast.LENGTH_SHORT).show();
                }
            } else {
                String error = task.getException() != null ? task.getException().getMessage() : "Erro desconhecido";
                Toast.makeText(LoginActivity.this, "Erro ao buscar dados: " + error,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void abrirTarefas(Usuario user) {
        Intent intent = new Intent(this, TarefasActivity.class);
        intent.putExtra("usuario", user);
        startActivity(intent);
        finish();
    }
}
