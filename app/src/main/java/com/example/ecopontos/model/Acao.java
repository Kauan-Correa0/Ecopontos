package com.example.ecopontos.model;

import java.io.Serializable;

public class Acao implements Serializable {
    private String id;
    private String descricao;
    private TipoMissao tipo;
    int pontos;

    public Acao(String id, String descricao, TipoMissao tipo, int pontos) {
        this.id = id;
        this.descricao = descricao;
        this.tipo = tipo;
        this.pontos = pontos;
    }

    public Acao(String descricao, TipoMissao tipo, int pontos) {
        this.descricao = descricao;
        this.tipo = tipo;
        this.pontos = pontos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoMissao getTipo() {
        return tipo;
    }

    public void setTipo(TipoMissao tipo) {
        this.tipo = tipo;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }
}
