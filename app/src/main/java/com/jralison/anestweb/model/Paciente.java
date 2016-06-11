package com.jralison.anestweb.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Representação do objeto Paciente na tabela pacientes.
 * <p/>
 * Created by Jonathan Souza on 09/06/2016.
 */
public class Paciente implements Serializable {

    private Long id;
    private String nome;
    private String cpf;
    private Genero genero;
    private Date nascimento;

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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public void setGenero(String genero) {
        setGenero(Genero.valueOf(genero));
    }

    public Date getNascimento() {
        return nascimento;
    }

    public void setNascimento(Date nascimento) {
        this.nascimento = nascimento;
    }

    public String getNascimentoFormatado() {
        if (null != nascimento) {
            return DateFormat.getDateInstance().format(nascimento);
        }
        return null;
    }

    public Integer getIdade() {
        if (null != nascimento) {
            final Calendar hoje = Calendar.getInstance();
            final Calendar nasc = Calendar.getInstance();
            nasc.setTime(nascimento);

            int idade = hoje.get(Calendar.YEAR) - nasc.get(Calendar.YEAR);

            int diff_mes = hoje.get(Calendar.MONTH) - nasc.get(Calendar.MONTH);
            int diff_dia = hoje.get(Calendar.DAY_OF_MONTH) - nasc.get(Calendar.DAY_OF_MONTH);
            if (diff_mes < 0 || (diff_mes == 0 && diff_dia < 0)) {
                idade--;
            }

            return idade;
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Paciente paciente = (Paciente) o;

        if (genero != paciente.genero) return false;
        if (!id.equals(paciente.id)) return false;
        if (!nome.equals(paciente.nome)) return false;
        if (cpf != null ? !cpf.equals(paciente.cpf) : paciente.cpf != null) return false;
        return nascimento != null ? nascimento.equals(paciente.nascimento) : paciente.nascimento == null;

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + nome.hashCode();
        result = 31 * result + (cpf != null ? cpf.hashCode() : 0);
        result = 31 * result + (genero != null ? genero.hashCode() : 0);
        result = 31 * result + (nascimento != null ? nascimento.hashCode() : 0);
        return result;
    }

    public enum Genero {
        MASCULINO,
        FEMININO
    }
}
