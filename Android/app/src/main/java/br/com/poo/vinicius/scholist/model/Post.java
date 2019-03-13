package br.com.poo.vinicius.scholist.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.security.Timestamp;


public class Post implements Parcelable {

    String descricao;
    String timestamp;
    Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post() {}

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(descricao);
        dest.writeString(timestamp);
    }

    public Post(String descricao, String timestamp) {
            this.timestamp = timestamp;
            this.descricao = descricao;
    }

    protected Post(Parcel in) {
        descricao = in.readString();
        timestamp = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }


    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };


}
