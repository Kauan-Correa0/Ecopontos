package com.example.ecopontos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecopontos.R;
import com.example.ecopontos.database.FirestoreManager;
import com.example.ecopontos.model.Promocao;
import com.example.ecopontos.model.PromocaoUsuario;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class PromocaoAdapter extends RecyclerView.Adapter<PromocaoAdapter.ViewHolder> {
    private List<Promocao> lista;
    private final FirestoreManager firestoreManager;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvEmpresa, tvDescricao, tvCusto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmpresa = itemView.findViewById(R.id.txt_nome_empresa);
            tvDescricao = itemView.findViewById(R.id.txt_descricao_desconto);
            tvCusto = itemView.findViewById(R.id.txt_custo_pontos);
        }
    }

    public PromocaoAdapter(List<Promocao> lista, FirestoreManager firestoreManager) {
        this.lista = lista;
        this.firestoreManager = firestoreManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promocao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Promocao promo = lista.get(position);
        
        holder.tvEmpresa.setText(promo.getIdParceiro()); 
        holder.tvDescricao.setText(promo.getDescricao());
        holder.tvCusto.setText(String.valueOf(promo.getValor()));

        holder.itemView.setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                Toast.makeText(v.getContext(), "Usuário não autenticado", Toast.LENGTH_SHORT).show();
                return;
            }

            String idUsuario = FirebaseAuth.getInstance().getCurrentUser().getUid();

            PromocaoUsuario promoUsuario = new PromocaoUsuario(
                    promo.getId(),
                    idUsuario,
                    promo.getIdParceiro(),
                    promo.getDescricao(),
                    promo.getValor()
            );

            firestoreManager.processarCompra(promoUsuario)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(v.getContext(), "Compra realizada com sucesso!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        String msg = "Erro ao realizar compra";
                        if (e.getMessage() != null && e.getMessage().contains("Saldo insuficiente")) {
                            msg = "Saldo insuficiente para esta compra!";
                        } else {
                            msg += ": " + e.getMessage();
                        }
                        Toast.makeText(v.getContext(), msg, Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}
