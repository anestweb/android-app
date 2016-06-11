package com.jralison.anestweb.model;

import java.io.Serializable;

/**
 * Representação do objeto Profissional na tabela profissionais.
 * <p/>
 * Created by Jonathan Souza on 08/06/2016.
 */
public class Profissional implements Serializable {

    private Long id;
    private String nome;
    private String crm;
    private String senha;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Profissional profissional = (Profissional) o;

        if (!id.equals(profissional.id)) return false;
        if (!nome.equals(profissional.nome)) return false;
        if (!crm.equals(profissional.crm)) return false;
        return senha.equals(profissional.senha);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + nome.hashCode();
        result = 31 * result + crm.hashCode();
        result = 31 * result + senha.hashCode();
        return result;
    }
}
