package com.example.ecopontos.model;

import java.io.Serializable;

public class Promocoes implements Serializable {
    String id_promocao;
    String id_parceiro;
    String descricao;
    int valor;

    public Promocoes(String id_promocao, String id_parceiro, String descricao, int valor) {
        this.id_promocao = id_promocao;
        this.id_parceiro = id_parceiro;
        this.descricao = descricao;
        this.valor = valor;
    }

    public Promocoes(String id_parceiro, String descricao, int valor) {
        this.id_parceiro = id_parceiro;
        this.descricao = descricao;
        this.valor = valor;
    }

    public String getId_promocao() {
        return id_promocao;
    }

    public void setId_promocao(String id_promocao) {
        this.id_promocao = id_promocao;
    }

    public String getId_parceiro() {
        return id_parceiro;
    }

    public void setId_parceiro(String id_parceiro) {
        this.id_parceiro = id_parceiro;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }
}
