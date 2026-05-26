package com.example.ecopontos.model;

import java.io.Serializable;

public class AcaoUsuario implements Serializable {
    private String id;
    private String idAcao;
    private String idUsuario;
    private String descricao;
    private TipoMissao tipo;
    private int pontos;

    public AcaoUsuario() {
    }

    public AcaoUsuario(String idAcao, String idUsuario, String descricao, TipoMissao tipo, int pontos) {
        this.idAcao = idAcao;
        this.idUsuario = idUsuario;
        this.descricao = descricao;
        this.tipo = tipo;
        this.pontos = pontos;
    }

    public AcaoUsuario(String id, String idAcao, String idUsuario, String descricao, TipoMissao tipo, int pontos) {
        this.id = id;
        this.idAcao = idAcao;
        this.idUsuario = idUsuario;
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

    public String getidAcao() {
        return idAcao;
    }

    public void setidAcao(String idAcao) {
        this.idAcao = idAcao;
    }

    public String getIdAcao() {
        return idAcao;
    }

    public void setIdAcao(String idAcao) {
        this.idAcao = idAcao;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
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
