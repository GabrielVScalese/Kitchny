package br.unicamp.kitchny;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

public class JacksonImmutable {
    public static class Usuario {

        String email;
        String nome;
        String senha;
        int receitas;
        Integer qtdReceitasAprovadas;
        Integer qtdReceitasReprovadas;
        Integer qtdReceitasPublicadas;
        Integer notaMediaReceitas;
        int id;

        public void setId(int id) {
            this.id = id;
        }

        public void setQtdReceitasAprovadas(Integer qtdReceitasAprovadas) {
            this.qtdReceitasAprovadas = qtdReceitasAprovadas;
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

        public void setReceitas(int receitas) {
            this.receitas = receitas;
        }

        public void setQtdReceitasReprovadas(Integer qtdReceitasReprovadas) {
            this.qtdReceitasReprovadas = qtdReceitasReprovadas;
        }

        public void setNotaMediaReceitas(Integer notaMediaReceitas) {
            this.notaMediaReceitas = notaMediaReceitas;
        }

        public void setQtdReceitasPublicadas(Integer qtdReceitasPublicadas) {
            this.qtdReceitasPublicadas = qtdReceitasPublicadas;
        }

        @JsonCreator
        public Usuario (@JsonProperty("id") int id, @JsonProperty("email") String email, @JsonProperty("nome") String nome, @JsonProperty("senha") String senha,
                        @JsonProperty("qtdReceitasAprovadas") Integer qtdReceitasAprovadas, @JsonProperty("qtdReceitasReprovadas") Integer qtdReceitasReprovadas,
                        @JsonProperty("qtdReceitasPublicadas") Integer qtdReceitasPublicadas, @JsonProperty("notaMediaReceitas") int notaMediaReceitas)
        {
            setId(id);
            setEmail(email);
            setNome(nome);
            setSenha(senha);
            setQtdReceitasAprovadas(qtdReceitasAprovadas);
            setQtdReceitasReprovadas(qtdReceitasReprovadas);
            setQtdReceitasPublicadas(qtdReceitasPublicadas);
            setNotaMediaReceitas(notaMediaReceitas);
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

        public int getId ()
        {
            return this.id;
        }

        public Integer getReceitas() {
            return receitas;
        }

        public Integer getNotaMediaReceitas() {
            return notaMediaReceitas;
        }

        public Integer getQtdReceitasAprovadas() {
            return qtdReceitasAprovadas;
        }

        public Integer getQtdReceitasReprovadas() {
            return qtdReceitasReprovadas;
        }

        public Integer getQtdReceitasPublicadas() {
            return qtdReceitasPublicadas;
        }
    }

}
