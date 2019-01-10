package br.com.poo.vinicius.scholist.model;

public class Aluno {

    private static Long id;
    private String nome;
    private String endereco;
    private String telefone;
    private String site;
    private Double nota;


    public static Long getId() {
        return id;
    }

    public static void setId(Long id) {
        Aluno.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }


}
