package com.example.ecopontos.adapter;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecopontos.R;
import com.example.ecopontos.database.FirestoreManager;
import com.example.ecopontos.model.Acao;
import com.example.ecopontos.model.AcaoUsuario;
import com.google.firebase.auth.FirebaseAuth;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AcaoAdapter extends RecyclerView.Adapter<AcaoAdapter.ViewHolder> {
    private List<Acao> lista;
    private final FirestoreManager firestoreManager;
    private final Set<String> acoesConcluidasLocal = new HashSet<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView descricao;
        private TextView pontos;
        private CheckBox check;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            descricao = itemView.findViewById(R.id.txt_descricao_missao);
            pontos = itemView.findViewById(R.id.txt_pontos_missao);
            check = itemView.findViewById(R.id.chk_missao);
        }

        public TextView getDescricao() {
            return descricao;
        }

        public TextView getPontos() {
            return pontos;
        }

        public CheckBox getCheck() {
            return check;
        }
    }
    public AcaoAdapter(List lista, FirestoreManager firestoreManager) {
        this.lista = lista;
        this.firestoreManager = firestoreManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_acao, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Acao acao = lista.get(position);
        String descricao = acao.getDescricao();
        int pontos = acao.getPontos();
        CheckBox checkButton = holder.getCheck();

        holder.getDescricao().setText(descricao);
        holder.getPontos().setText(String.valueOf(pontos));

        // Reset estado para evitar bugs de reciclagem do RecyclerView
        checkButton.setEnabled(true);
        checkButton.setChecked(false);
        holder.getDescricao().setPaintFlags(0);
        holder.getDescricao().setTextColor(Color.BLACK);

        // Verifica cache local primeiro para resposta instantânea
        if (acoesConcluidasLocal.contains(acao.getId())) {
            marcarComoConcluido(holder, checkButton);
        } else {
            // Verifica se a missão já foi concluída anteriormente no período atual (dia ou mês)
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                String idUsuario = FirebaseAuth.getInstance().getCurrentUser().getUid();
                firestoreManager.verificarAcaoConcluida(idUsuario, acao.getId(), acao.getTipo())
                        .addOnSuccessListener(jaConcluida -> {
                            if (jaConcluida) {
                                acoesConcluidasLocal.add(acao.getId());
                                marcarComoConcluido(holder, checkButton);
                            }
                        });
            }
        }

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkButton.isChecked()) {
                    if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                        Toast.makeText(view.getContext(), "Usuário não autenticado", Toast.LENGTH_SHORT).show();
                        checkButton.setChecked(false);
                        return;
                    }

                    String idUsuario = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    firestoreManager.verificarAcaoConcluida(idUsuario, acao.getId(), acao.getTipo())
                            .addOnSuccessListener(jaConcluida -> {
                                if (jaConcluida) {
                                    Toast.makeText(view.getContext(), "Missão já concluída!", Toast.LENGTH_SHORT).show();
                                    checkButton.setChecked(true);
                                    checkButton.setEnabled(false);
                                    return;
                                }

                                checkButton.setEnabled(false);
                                holder.getDescricao().setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                holder.getDescricao().setTextColor(Color.argb(120, 0, 0, 0));

                                AcaoUsuario acaoUsuario = new AcaoUsuario(
                                        acao.getId(),
                                        idUsuario,
                                        acao.getDescricao(),
                                        acao.getTipo(),
                                        acao.getPontos()
                                );

                                firestoreManager.registrarAcao(acaoUsuario)
                                        .addOnSuccessListener(documentReference -> {
                                            acoesConcluidasLocal.add(acao.getId());
                                            firestoreManager.atualizarPontos(idUsuario, acao.getPontos());
                                            Toast.makeText(view.getContext(), "Missão concluída!", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(view.getContext(), "Erro ao registrar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            checkButton.setEnabled(true);
                                            checkButton.setChecked(false);
                                            holder.getDescricao().setPaintFlags(0);
                                            holder.getDescricao().setTextColor(Color.BLACK);
                                        });
                            });
                }
            }
        });
    }

    private void marcarComoConcluido(ViewHolder holder, CheckBox checkButton) {
        checkButton.setChecked(true);
        checkButton.setEnabled(false);
        holder.getDescricao().setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.getDescricao().setTextColor(Color.argb(120, 0, 0, 0));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}
