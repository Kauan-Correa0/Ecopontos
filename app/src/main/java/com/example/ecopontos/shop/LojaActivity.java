package com.example.ecopontos.shop;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecopontos.R;
import com.example.ecopontos.adapter.PromocaoAdapter;
import com.example.ecopontos.database.FirestoreManager;
import com.example.ecopontos.leaderboard.ClassificacaoActivity;
import com.example.ecopontos.model.Promocao;
import com.example.ecopontos.tasks.TarefasActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

public class LojaActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PromocaoAdapter adapter;
    private List<Promocao> listaPromocoes;
    private FirestoreManager firestoreManager;
    private TextView tvPontosLoja;
    private ListenerRegistration listenerPontos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loja);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firestoreManager = new FirestoreManager();
        listaPromocoes = new ArrayList<>();
        
        tvPontosLoja = findViewById(R.id.txt_pontos_loja);

        recyclerView = findViewById(R.id.recycler_promocoes);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        
        adapter = new PromocaoAdapter(listaPromocoes, firestoreManager);
        recyclerView.setAdapter(adapter);

        carregarPromocoes();
        escutarPontos();
        setupBottomNavigation();
    }

    private void escutarPontos() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String idUsuario = FirebaseAuth.getInstance().getCurrentUser().getUid();
            listenerPontos = firestoreManager.obterDocumentoUsuario(idUsuario)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        Long pontos = documentSnapshot.getLong("pontos");
                        tvPontosLoja.setText(String.valueOf(pontos != null ? pontos : 0));
                    }
                });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listenerPontos != null) {
            listenerPontos.remove();
        }
    }

    private void carregarPromocoes() {
        firestoreManager.buscarTodasPromocoes().addOnSuccessListener(queryDocumentSnapshots -> {
            listaPromocoes.clear();
            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                Long vLong = doc.getLong("valor");
                Promocao p = new Promocao(
                        doc.getId(),
                        doc.getString("idParceiro"),
                        doc.getString("descricao"),
                        vLong != null ? vLong.intValue() : 0
                );
                listaPromocoes.add(p);
            }
            adapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Erro ao carregar promoções", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupBottomNavigation() {
        LinearLayout btnAcoes = findViewById(R.id.btn_nav_acoes);
        LinearLayout btnRanking = findViewById(R.id.btn_nav_ranking);

        View indicatorAcoes = findViewById(R.id.indicator_acoes);
        View indicatorRanking = findViewById(R.id.indicator_ranking);
        View indicatorPromocoes = findViewById(R.id.indicator_promocoes);

        indicatorAcoes.setBackgroundColor(Color.TRANSPARENT);
        indicatorRanking.setBackgroundColor(Color.TRANSPARENT);
        indicatorPromocoes.setBackgroundColor(Color.parseColor("#5CD67A"));

        btnAcoes.setOnClickListener(v -> {
            startActivity(new Intent(this, TarefasActivity.class));
            finish();
        });

        btnRanking.setOnClickListener(v -> {
            startActivity(new Intent(this, ClassificacaoActivity.class));
            finish();
        });
    }
}
