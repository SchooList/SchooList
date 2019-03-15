package br.com.poo.vinicius.scholist;

public class Material {
    String materialUrl;
    String timestamp;

    public Material(){}

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    String titulo;


    public String getMaterialUrl() {
        return materialUrl;
    }

    public void setMaterialUrl(String materialUrl) {
        this.materialUrl = materialUrl;
    }



    public Material(String materialUrl, String timestamp, String titulo) {
        this.materialUrl = materialUrl;
        this.timestamp = timestamp;
        this.titulo = titulo;
    }




}
