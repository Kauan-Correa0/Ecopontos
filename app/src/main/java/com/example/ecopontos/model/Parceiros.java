package com.example.ecopontos.model;

import java.io.Serializable;

public class Parceiros implements Serializable {
    String id_parceiro;
    String nome;
    String categoria;

    public Parceiros(String id_parceiro, String nome, String categoria) {
        this.id_parceiro = id_parceiro;
        this.nome = nome;
        this.categoria = categoria;
    }

    public Parceiros(String nome, String categoria) {
        this.nome = nome;
        this.categoria = categoria;
    }

    public String getId_parceiro() {
        return id_parceiro;
    }

    public void setId_parceiro(String id_parceiro) {
        this.id_parceiro = id_parceiro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
