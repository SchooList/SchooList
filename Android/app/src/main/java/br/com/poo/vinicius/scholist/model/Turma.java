package br.com.poo.vinicius.scholist.model;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Turma implements Parcelable {

    String nome;
    String descricao;
    String profileUrl;
    String uuid;
    User users[];

    public Turma() {

    }

    public Turma(String nome, String descricao, String profileUrl){
        this.nome = nome;
        this.descricao = descricao;
        this.profileUrl = profileUrl;
    }

    protected Turma(Parcel in) {
        uuid = in.readString();
        nome = in.readString();
        descricao = in.readString();
        profileUrl = in.readString();
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void profileUrl(String getProfileUrl) {
        this.profileUrl = getProfileUrl;
    }

    public String getUuid() {
        return uuid;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Turma> CREATOR = new Creator<Turma>() {
        @Override
        public Turma createFromParcel(Parcel in) {
            return new Turma(in);
        }

        @Override
        public Turma[] newArray(int size) {
            return new Turma[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeString(nome);
        dest.writeString(descricao);
        dest.writeString(profileUrl);
    }
}
