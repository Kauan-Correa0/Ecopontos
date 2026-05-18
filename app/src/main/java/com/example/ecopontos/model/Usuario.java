package com.example.ecopontos.model;

import java.io.Serializable;

public class Usuario implements Serializable {
    private int id;
    private String nome;
    private String municipio;
    private int pontos, pontosTotais;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public int getPontosTotais() {
        return pontosTotais;
    }

    public void setPontosTotais(int pontosTotais) {
        this.pontosTotais = pontosTotais;
    }
}
