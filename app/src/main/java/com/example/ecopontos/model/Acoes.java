package com.example.ecopontos.model;

import java.io.Serializable;

public class Acoes implements Serializable {
    String id;
    String id_usuario;
    String descricao;

    public Acoes(String id, String id_usuario, String descricao) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.descricao = descricao;
    }

    public Acoes(String id_usuario, String descricao) {
        this.id_usuario = id_usuario;
        this.descricao = descricao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
