package com.example.ecopontos.model;

import java.io.Serializable;

public class Parceiro implements Serializable {
    private String id;
    private String nome;
    private String categoria;

    public Parceiro() {
    }

    public Parceiro(String id, String nome, String categoria) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
    }

    public Parceiro(String nome, String categoria) {
        this.nome = nome;
        this.categoria = categoria;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
