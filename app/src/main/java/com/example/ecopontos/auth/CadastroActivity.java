package com.example.ecopontos.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class CadastroActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirestoreManager fManager;
    private EditText etUsername, etEmail, etPassword, etConfirmPassword;
    private TextView tvEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        etUsername = findViewById(R.id.usernameEditText);
        etEmail = findViewById(R.id.emailEditText);
        etPassword = findViewById(R.id.passwordEditText);
        etConfirmPassword = findViewById(R.id.confirmPasswordEditText);
        tvEntrar = findViewById(R.id.tvEntrar);
        fManager = new FirestoreManager();

        tvEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void limparCampos() {
        etUsername.setText("");
        etEmail.setText("");
        etPassword.setText("");
        etConfirmPassword.setText("");
    }

    public void abrirPerfil(Usuario user) {
        Intent intent = new Intent(this, PerfilActivity.class);
        intent.putExtra("usuario", user);
        startActivity(intent);
        finish();
    }

    public void abrirTarefas(Usuario user) {
        Intent intent = new Intent(this, TarefasActivity.class);
        intent.putExtra("usuario", user);
        startActivity(intent);
        finish();
    }

    public void criarUsuario(View view) {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    String id = user.getUid();
                    String municipio = "Botucatu-SP";
                    fManager.salvarUsuario(id, username, municipio, 0, 0);
                    limparCampos();
                    Usuario usuario = new Usuario(id, username, municipio, 0, 0);
                    abrirTarefas(usuario);
                }
            } else {
                String error = task.getException() != null ? task.getException().getMessage() : "Erro desconhecido";
                Toast.makeText(CadastroActivity.this, "Erro ao criar usuário: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
