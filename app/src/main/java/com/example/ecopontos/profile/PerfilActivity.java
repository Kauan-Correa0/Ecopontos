package com.example.ecopontos.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecopontos.R;
import com.example.ecopontos.database.FirestoreManager;
import com.example.ecopontos.model.TipoMissao;
import com.example.ecopontos.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;

public class PerfilActivity extends AppCompatActivity {
	private TextView tvNome, tvPontos, tvPontosTotais, tvRanking,
			tvAcoesFeitas, tvAcoesMensais, tvAcoesDiarias, tvPontosGastos;
	private Usuario user;
	private FirestoreManager fManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EdgeToEdge.enable(this);
		setContentView(R.layout.activity_perfil);
		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
			Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
			return insets;
		});

		tvNome = findViewById(R.id.tvNome);
		tvPontos = findViewById(R.id.tvPontos);
		tvPontosTotais = findViewById(R.id.tvPontosTotais);
		tvRanking = findViewById(R.id.tvRanking);
		tvAcoesFeitas = findViewById(R.id.tvAcoesFeitas);
		tvAcoesMensais = findViewById(R.id.tvAcoesMensais);
		tvAcoesDiarias = findViewById(R.id.tvAcoesDiarias);
		tvPontosGastos = findViewById(R.id.tvPontosGastos);

		fManager = new FirestoreManager();
		
		user = (Usuario) getIntent().getSerializableExtra("usuario");

		if (user != null) {
			atualizarInterface();
		} else if (FirebaseAuth.getInstance().getCurrentUser() != null) {
			String idUsuario = FirebaseAuth.getInstance().getCurrentUser().getUid();
			fManager.buscarUsuario(idUsuario).addOnSuccessListener(documentSnapshot -> {
				if (documentSnapshot.exists()) {
					Long pontos = documentSnapshot.getLong("pontos");
					Long pontosTotais = documentSnapshot.getLong("pontosTotais");
					
					user = new Usuario(
							idUsuario,
							documentSnapshot.getString("nome"),
							documentSnapshot.getString("municipio"),
							pontos != null ? pontos.intValue() : 0,
							pontosTotais != null ? pontosTotais.intValue() : 0
					);
					atualizarInterface();
				}
			});
		}
	}

	private void atualizarInterface() {
		if (user != null) {
			tvNome.setText(user.getNome());
			tvPontos.setText(String.valueOf(user.getPontos()));
			tvPontosTotais.setText(String.valueOf(user.getPontosTotais()));

			fManager.buscarPosicaoRanking(user.getId())
					.addOnSuccessListener(rank -> tvRanking.setText(rank + "º"))
					.addOnFailureListener(e -> tvRanking.setText("--"));
			fManager.contarAcoes(user.getId(), null)
					.addOnSuccessListener(task ->
							tvAcoesFeitas.setText(String.valueOf(task)));
			fManager.contarAcoes(user.getId(), TipoMissao.MENSAL)
					.addOnSuccessListener(task ->
							tvAcoesMensais.setText(String.valueOf(task)));
			fManager.contarAcoes(user.getId(), TipoMissao.DIARIA)
					.addOnSuccessListener(task ->
							tvAcoesDiarias.setText(String.valueOf(task)));
			tvPontosGastos.setText(String.valueOf(
					user.getPontosTotais() - user.getPontos()));
		}
	}

	public void voltar(View view) {
		onBackPressed();
	}
}
