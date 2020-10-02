package br.unicamp.kitchny;

public class Usuario
{
    String email;
    String nome;
    String senha;
    int aprovacao;
    int reprovacao;
    int receitas;
    int media;
    Integer qtdReceitasAprovadas;
    int id;

    public Usuario (String email, String nome, String senha, int aprovacao, int reprovacao, int receitas, int media)
    {
        setEmail(email);
        setNome(nome);
        setSenha(senha);
        setAprovacao(aprovacao);
        setReprovacao(reprovacao);
        setReceitas(receitas);
        setMedia(media);
    }

    public Usuario (String email, String senha)
    {
        setEmail(email);
        setSenha(senha);
    }

    public Usuario (String email, String nome, String senha)
    {
        setEmail(email);
        setNome(nome);
        setSenha(senha);
    }

    public Usuario (int id, String email, String nome, String senha, Integer qtdReceitasAprovadas)
    {
        setId(id);
        setEmail(email);
        setNome(nome);
        setSenha(senha);
        setQtdReceitasAprovadas(qtdReceitasAprovadas);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setAprovacao(int aprovacao) {
        this.aprovacao = aprovacao;
    }

    public void setReprovacao(int reprovacao) {
        this.reprovacao = reprovacao;
    }

    public void setMedia(int media) {
        this.media = media;
    }

    public void setReceitas(int receitas) {
        this.receitas = receitas;
    }

    public void setQtdReceitasAprovadas(Integer qtdReceitasAprovadas) {
        this.qtdReceitasAprovadas = qtdReceitasAprovadas;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

    public int getAprovacao() {
        return aprovacao;
    }

    public int getMedia() {
        return media;
    }

    public Integer getReceitas() {
        return receitas;
    }

    public int getReprovacao() {
        return reprovacao;
    }
}
