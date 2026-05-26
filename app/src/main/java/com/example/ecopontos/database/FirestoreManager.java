package com.example.ecopontos.database; // Ajuste para o seu pacote real

import com.example.ecopontos.model.AcaoUsuario;
import com.example.ecopontos.model.PromocaoUsuario;
import com.example.ecopontos.model.TipoMissao;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FirestoreManager {

	private final FirebaseFirestore db;
	public FirestoreManager() {
		this.db = FirebaseFirestore.getInstance();
	}

	public Task<Void> salvarUsuario(String idUsuario, String nome, String municipio, int pontos, int pontosTotais) {
		Map<String, Object> usuario = new HashMap<>();
		usuario.put("nome", nome);
		usuario.put("municipio", municipio);
		usuario.put("pontos", pontos);
		usuario.put("pontosTotais", pontosTotais);

		return db.collection("usuarios").document(idUsuario).set(usuario);
	}

	public Task<DocumentSnapshot> buscarUsuario(String idUsuario) {
		return db.collection("usuarios").document(idUsuario).get();
	}

	public DocumentReference obterDocumentoUsuario(String idUsuario) {
		return db.collection("usuarios").document(idUsuario);
	}

	public Task<Integer> buscarPosicaoRanking(String idUsuario) {
		return db.collection("usuarios").document(idUsuario).get().continueWithTask(task -> {
			if (!task.isSuccessful() || task.getResult() == null) {
				throw task.getException();
			}

			DocumentSnapshot userDoc = task.getResult();
			if (!userDoc.exists()) {
				throw new Exception("Usuário não encontrado");
			}

			Long pontos = userDoc.getLong("pontos");
			if (pontos == null) pontos = 0L;

			return db.collection("usuarios")
					.whereGreaterThan("pontos", pontos)
					.get()
					.continueWith(countTask -> {
						if (!countTask.isSuccessful()) {
							throw countTask.getException();
						}
						// A posição é o total de pessoas com mais pontos + 1
						return countTask.getResult().size() + 1;
					});
		});
	}

	public Query buscarRankingPorMunicipio(String municipio) {
		return db.collection("usuarios")
				.whereEqualTo("municipio", municipio)
				.orderBy("pontos", Query.Direction.DESCENDING);
	}

	public Task<DocumentReference> registrarAcao(AcaoUsuario acaoUsuario) {
		Map<String, Object> acao = new HashMap<>();
		acao.put("id_acao", acaoUsuario.getIdAcao());
		acao.put("id_usuario", acaoUsuario.getIdUsuario());
		acao.put("descricao", acaoUsuario.getDescricao());
		acao.put("tipo", acaoUsuario.getTipo().name());
		acao.put("pontos", acaoUsuario.getPontos());
		acao.put("data", FieldValue.serverTimestamp());

		return db.collection("usuarios")
				.document(acaoUsuario.getIdUsuario())
				.collection("acoes")
				.add(acao);
	}

	public Task<Void> atualizarPontos(String idUsuario, int pontos) {
		DocumentReference userRef = db.collection("usuarios").document(idUsuario);
		Map<String, Object> updates = new HashMap<>();
		updates.put("pontos", FieldValue.increment(pontos));
		updates.put("pontosTotais", FieldValue.increment(pontos));

		return userRef.update(updates);
	}

	public Task<Boolean> verificarAcaoConcluida(String idUsuario, String idAcao, TipoMissao tipo) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		if (tipo == TipoMissao.MENSAL) {
			cal.set(Calendar.DAY_OF_MONTH, 1);
		}

		Date inicioPeriodo = cal.getTime();

		return db.collection("usuarios")
				.document(idUsuario)
				.collection("acoes")
				.whereEqualTo("id_acao", idAcao)
				.whereGreaterThanOrEqualTo("data", inicioPeriodo)
				.get()
				.continueWith(task -> {
					if (task.isSuccessful() && task.getResult() != null) {
						return !task.getResult().isEmpty();
					}
					return false;
				});
	}

	public Query buscarHistoricoAcoes(String idUsuario) {
		return db.collection("usuarios")
				.document(idUsuario)
				.collection("acoes")
				.orderBy("data", Query.Direction.DESCENDING);
	}

	public CollectionReference obterAcoes(String idUsuario) {
		return db.collection("usuarios").document(idUsuario).collection("acoes");
	}

	public Task<Long> contarAcoes(String idUsuario, TipoMissao tipo) {
		Query query = db.collection("usuarios")
				.document(idUsuario)
				.collection("acoes");

		if (tipo != null) {
			query = query.whereEqualTo("tipo", tipo.name());
		}

		return query.count().get(AggregateSource.SERVER).continueWith(task -> {
			if (task.isSuccessful()) {
				return task.getResult().getCount();
			}
			return 0L;
		});
	}

	public Task<QuerySnapshot> buscarTodasPromocoes() {
		return db.collection("promocoes").get();
	}

	public Task<QuerySnapshot> buscarTodasMissoes(TipoMissao tipo) {
		Query query = db.collection("missoes");

		if (tipo != null) {
			query = query.whereEqualTo("tipo", tipo.name());
		}

		return query.get();
	}

	public Task<Void> processarCompra(PromocaoUsuario promoUsuario) {
		DocumentReference userRef = db.collection("usuarios").document(promoUsuario.getIdUsuario());
		DocumentReference compraRef = userRef.collection("compras").document();

		return db.runTransaction(transaction -> {
			DocumentSnapshot userDoc = transaction.get(userRef);
			Long pontosAtuais = userDoc.getLong("pontos");
			if (pontosAtuais == null) pontosAtuais = 0L;

			int custo = promoUsuario.getValorPago();

			if (pontosAtuais < custo) {
				throw new FirebaseFirestoreException("Saldo insuficiente",
						FirebaseFirestoreException.Code.ABORTED);
			}

			// Deduz os pontos
			transaction.update(userRef, "pontos", FieldValue.increment(-custo));

			// Registra a compra
			Map<String, Object> compra = new HashMap<>();
			compra.put("id_promocao", promoUsuario.getIdPromocao());
			compra.put("id_parceiro", promoUsuario.getIdParceiro());
			compra.put("descricao", promoUsuario.getDescricao());
			compra.put("valor_pago", custo);
			compra.put("data_compra", FieldValue.serverTimestamp());

			transaction.set(compraRef, compra);

			return null;
		});
	}

	public Task<DocumentReference> registrarCompra(PromocaoUsuario promoUsuario) {
		Map<String, Object> compra = new HashMap<>();
		compra.put("id_promocao", promoUsuario.getIdPromocao());
		compra.put("id_parceiro", promoUsuario.getIdParceiro());
		compra.put("descricao", promoUsuario.getDescricao());
		compra.put("valor_pago", promoUsuario.getValorPago());
		compra.put("data_compra", FieldValue.serverTimestamp());

		return db.collection("usuarios")
				.document(promoUsuario.getIdUsuario())
				.collection("compras")
				.add(compra);
	}
}