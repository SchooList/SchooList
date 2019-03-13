package br.com.poo.vinicius.scholist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.poo.vinicius.scholist.model.Prova;

public class ProvaDAO extends SQLiteOpenHelper implements GlobalDAO<Prova>{
    private static ProvaDAO uniqueInstance;

    private ProvaDAO(Context context) {
        super(context, "Agenda", null, 2);
    }
    public static ProvaDAO getInstance(Context context) {
        if(uniqueInstance == null) {
            uniqueInstance = new ProvaDAO(context);
        }
        return uniqueInstance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Prova (id INTEGER PRIMARY KEY, " +
                "materia TEXT NOT NULL, " +
                "data TEXT NOT NULL, " +
                "topicos TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "";
        switch (oldVersion) {
            case 1:
                sql = "ALTER TABLE Prova ADD COLUMN nota TEXT";
                db.execSQL(sql); // indo para versao 2
        }
    }

    @Override
    public void insere(Prova object) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = getContentValuesProva(object);
        db.insert("Prova",null, dados);
    }

    @NonNull
    private ContentValues getContentValuesProva(Prova prova) {
        ContentValues dados = new ContentValues();
        dados.put("materia", prova.getMateria());
        dados.put("data", prova.getData());
        dados.put("topicos", String.valueOf(prova.getTopicos()));
        return dados;
    }

    public List<Prova> buscaProvas() {
        String sql = "SELECT * FROM Prova;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);

        List<Prova> provas = new ArrayList<Prova>();
        while (c.moveToNext()) {
            Prova prova = new Prova();
            prova.setId(c.getLong(c.getColumnIndex("id")));
            prova.setMateria(c.getString(c.getColumnIndex("nome")));
            prova.setData(c.getString(c.getColumnIndex("endereco")));
            prova.setTopicos(Collections.singletonList(c.getString(c.getColumnIndex("telefone"))));

            provas.add(prova);
        }
        c.close();
        return provas;
    }
    public void delete(Prova object) {
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {object.getId().toString()};
        db.delete("Prova", "id = ?", params);

    }
    public void update(Prova object) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = getContentValuesProva(object);
        String[] params = {object.getId().toString()};
        db.update("Prova", dados, "id = ?", params);
    }
}