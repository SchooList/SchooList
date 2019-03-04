package br.com.poo.vinicius.scholist.dao;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.poo.vinicius.scholist.model.Aluno;

public interface GlobalDAO {
    public void onCreate(SQLiteDatabase db);
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
    public void insere(Object object);
    public void delete(Object object);
    public void update(Object object);
}
