package com.example.ecopontos.leaderboard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecopontos.R;
import com.example.ecopontos.adapter.RankingAdapter;
import com.example.ecopontos.database.FirestoreManager;
import com.example.ecopontos.model.Usuario;
import com.example.ecopontos.profile.PerfilActivity;
import com.example.ecopontos.shop.LojaActivity;
import com.example.ecopontos.tasks.TarefasActivity;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ClassificacaoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RankingAdapter adapter;
    private List<Usuario> usuarios;
    private FirestoreManager firestoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_classificacao);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firestoreManager = new FirestoreManager();
        usuarios = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_ranking);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new RankingAdapter(usuarios, usuario -> {
            Intent intent = new Intent(this, PerfilActivity.class);
            intent.putExtra("usuario", usuario);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        carregarRanking();
        setupBottomNavigation();
    }

    private void carregarRanking() {
        firestoreManager.buscarRankingPorMunicipio("Botucatu-SP").get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                Log.d("Ranking", "Query concluída. Documentos encontrados: " + queryDocumentSnapshots.size());
                usuarios.clear();
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    Long p = doc.getLong("pontos");
                    Long pt = doc.getLong("pontosTotais");
                    Usuario u = new Usuario(
                            doc.getId(),
                            doc.getString("nome"),
                            doc.getString("municipio"),
                            p != null ? p.intValue() : 0,
                            pt != null ? pt.intValue() : 0
                    );
                    usuarios.add(u);
                }
                adapter.notifyDataSetChanged();
            })
            .addOnFailureListener(e -> {
                Log.e("Ranking", "Erro ao carregar ranking: " + e.getMessage());
                e.printStackTrace();
            });
    }

    private void setupBottomNavigation() {
        LinearLayout btnAcoes = findViewById(R.id.btn_nav_acoes);
        LinearLayout btnPromocoes = findViewById(R.id.btn_nav_promocoes);

        View indicatorAcoes = findViewById(R.id.indicator_acoes);
        View indicatorRanking = findViewById(R.id.indicator_ranking);
        View indicatorPromocoes = findViewById(R.id.indicator_promocoes);

        indicatorAcoes.setBackgroundColor(Color.TRANSPARENT);
        indicatorRanking.setBackgroundColor(Color.parseColor("#5CD67A"));
        indicatorPromocoes.setBackgroundColor(Color.TRANSPARENT);

        btnAcoes.setOnClickListener(v -> {
            startActivity(new Intent(this, TarefasActivity.class));
            finish();
        });

        btnPromocoes.setOnClickListener(v -> {
            startActivity(new Intent(this, LojaActivity.class));
            finish();
        });
    }
}