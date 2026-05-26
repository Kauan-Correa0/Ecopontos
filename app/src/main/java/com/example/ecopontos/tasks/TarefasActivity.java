package com.example.ecopontos.tasks;

import android.content.Intent;
import android.graphics.Color;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecopontos.R;
import com.example.ecopontos.adapter.AcaoAdapter;
import com.example.ecopontos.database.FirestoreManager;
import com.example.ecopontos.leaderboard.ClassificacaoActivity;
import com.example.ecopontos.model.Acao;
import com.example.ecopontos.model.TipoMissao;
import com.example.ecopontos.model.Usuario;
import com.example.ecopontos.profile.PerfilActivity;
import com.example.ecopontos.shop.LojaActivity;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TarefasActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Acao> acoes;
    private AcaoAdapter adapter;
    private FirestoreManager firestoreManager;
    private Button btnDiarias, btnMensais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tarefas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setupBottomNavigation();
        
        firestoreManager = new FirestoreManager();
        
        acoes = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_missoes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AcaoAdapter(acoes, firestoreManager);
        recyclerView.setAdapter(adapter);

        btnDiarias = findViewById(R.id.btn_diarias);
        btnMensais = findViewById(R.id.btn_mensais);

        btnDiarias.setOnClickListener(v -> carregarMissoes(TipoMissao.DIARIA));
        btnMensais.setOnClickListener(v -> carregarMissoes(TipoMissao.MENSAL));

        carregarMissoes(TipoMissao.DIARIA);
    }

    public void carregarMissoes(TipoMissao tipo) {
        if (tipo == TipoMissao.DIARIA) {
            btnDiarias.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BCD1D3")));
            btnMensais.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E8F1F2")));
        } else {
            btnDiarias.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E8F1F2")));
            btnMensais.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BCD1D3")));
        }

        firestoreManager.buscarTodasMissoes(tipo).addOnSuccessListener(queryDocumentSnapshots -> {
            acoes.clear();
            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                String id = doc.getId();
                String descricao = doc.getString("descricao");
                String tipoStr = doc.getString("tipo");
                Long pontosLong = doc.getLong("pontos");

                int pontos = (pontosLong != null) ? pontosLong.intValue() : 0;
                TipoMissao t = TipoMissao.DIARIA;
                try {
                    if (tipoStr != null) t = TipoMissao.valueOf(tipoStr);
                } catch (IllegalArgumentException e) {
                    // Ignora
                }

                Acao acao = new Acao(id, descricao, t, pontos);
                acoes.add(acao);
            }
            adapter.notifyDataSetChanged();
        });
    }

    private void setupBottomNavigation() {
        LinearLayout btnRanking = findViewById(R.id.btn_nav_ranking);
        LinearLayout btnPromocoes = findViewById(R.id.btn_nav_promocoes);

        View indicatorAcoes = findViewById(R.id.indicator_acoes);
        View indicatorRanking = findViewById(R.id.indicator_ranking);
        View indicatorPromocoes = findViewById(R.id.indicator_promocoes);

        indicatorAcoes.setBackgroundColor(Color.parseColor("#5CD67A"));
        indicatorRanking.setBackgroundColor(Color.TRANSPARENT);
        indicatorPromocoes.setBackgroundColor(Color.TRANSPARENT);

        btnRanking.setOnClickListener(v -> {
            startActivity(new Intent(this, ClassificacaoActivity.class));
            finish();
        });

        btnPromocoes.setOnClickListener(v -> {
            startActivity(new Intent(this, LojaActivity.class));
            finish();
        });
    }

    public void abrirPerfil(View view) {
        Intent intent = new Intent(this, PerfilActivity.class);
        startActivity(intent);
    }
}
