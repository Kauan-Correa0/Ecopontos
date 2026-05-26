package com.example.ecopontos.model;

import java.io.Serializable;

public class Promocao implements Serializable {
    private String id;
    private String idParceiro;
    private String descricao;
    private int valor;

    public Promocao() {
    }

    public Promocao(String id, String idParceiro, String descricao, int valor) {
        this.id = id;
        this.idParceiro = idParceiro;
        this.descricao = descricao;
        this.valor = valor;
    }

    public Promocao(String idParceiro, String descricao, int valor) {
        this.idParceiro = idParceiro;
        this.descricao = descricao;
        this.valor = valor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdParceiro() {
        return idParceiro;
    }

    public void setIdParceiro(String idParceiro) {
        this.idParceiro = idParceiro;
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
