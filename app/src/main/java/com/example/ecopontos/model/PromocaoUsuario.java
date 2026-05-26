package com.example.ecopontos.model;

import java.io.Serializable;
import java.util.Date;

public class PromocaoUsuario implements Serializable {
    private String id;
    private String idPromocao;
    private String idUsuario;
    private String idParceiro;
    private String descricao;
    private int valorPago;
    private Date dataCompra;

    public PromocaoUsuario() {
    }

    public PromocaoUsuario(String idPromocao, String idUsuario, String idParceiro, String descricao, int valorPago) {
        this.idPromocao = idPromocao;
        this.idUsuario = idUsuario;
        this.idParceiro = idParceiro;
        this.descricao = descricao;
        this.valorPago = valorPago;
    }

    public PromocaoUsuario(String id, String idPromocao, String idUsuario, String idParceiro, String descricao, int valorPago, Date dataCompra) {
        this.id = id;
        this.idPromocao = idPromocao;
        this.idUsuario = idUsuario;
        this.idParceiro = idParceiro;
        this.descricao = descricao;
        this.valorPago = valorPago;
        this.dataCompra = dataCompra;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdPromocao() {
        return idPromocao;
    }

    public void setIdPromocao(String idPromocao) {
        this.idPromocao = idPromocao;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
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

    public int getValorPago() {
        return valorPago;
    }

    public void setValorPago(int valorPago) {
        this.valorPago = valorPago;
    }

    public Date getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(Date dataCompra) {
        this.dataCompra = dataCompra;
    }
}
