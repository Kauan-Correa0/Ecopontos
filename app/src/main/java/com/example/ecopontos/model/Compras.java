package com.example.ecopontos.model;

import java.io.Serializable;

public class Compras implements Serializable {
    String id_compra;
    String id_promocao;
    String id_usuario;
    public Compras(String id_compra, String id_promocao, String id_usuario) {
        this.id_compra = id_compra;
        this.id_promocao = id_promocao;
        this.id_usuario = id_usuario;
    }

    public Compras(String id_usuario, String id_promocao) {
        this.id_usuario = id_usuario;
        this.id_promocao = id_promocao;
    }

    public String getId_compra() {
        return id_compra;
    }

    public void setId_compra(String id_compra) {
        this.id_compra = id_compra;
    }

    public String getId_promocao() {
        return id_promocao;
    }

    public void setId_promocao(String id_promocao) {
        this.id_promocao = id_promocao;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }
}
