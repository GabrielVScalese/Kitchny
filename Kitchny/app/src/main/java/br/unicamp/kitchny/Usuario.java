package br.unicamp.kitchny;

import java.io.Serializable;

public class Usuario implements Serializable {
    protected String email;
    protected String nome;
    protected String senha;

    public Usuario (String email, String nome, String senha)
    {
        setEmail(email);
        setNome(nome);
        setSenha(senha);
    }

    public Usuario (String email, String senha)
    {
        setEmail(email);
        setSenha(senha);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
