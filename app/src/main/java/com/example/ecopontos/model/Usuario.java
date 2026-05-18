package com.example.ecopontos.model;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String id;
    private String nome;
    private String municipio;
    private int pontos, pontosTotais;

    public Usuario(String id, String nome, String municipio, int pontos, int pontosTotais) {
        this.id = id;
        this.nome = nome;
        this.municipio = municipio;
        this.pontos = pontos;
        this.pontosTotais = pontosTotais;
    }

    public Usuario(String nome, String municipio, int pontos, int pontosTotais) {
        this.nome = nome;
        this.municipio = municipio;
        this.pontos = pontos;
        this.pontosTotais = pontosTotais;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
