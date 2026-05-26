package com.example.ecopontos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecopontos.R;
import com.example.ecopontos.model.Usuario;

import java.util.List;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ViewHolder> {
    private List<Usuario> lista;
    private OnUserClickListener listener;

    public interface OnUserClickListener {
        void onUserClick(Usuario usuario);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPosicao, tvNome, tvPontos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPosicao = itemView.findViewById(R.id.txt_posicao);
            tvNome = itemView.findViewById(R.id.txt_nome_usuario);
            tvPontos = itemView.findViewById(R.id.txt_pontos_ranking);
        }
    }

    public RankingAdapter(List<Usuario> lista, OnUserClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ranking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Usuario user = lista.get(position);
        holder.tvPosicao.setText(String.valueOf(position + 1));
        holder.tvNome.setText(user.getNome());
        holder.tvPontos.setText(String.valueOf(user.getPontos()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUserClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}
