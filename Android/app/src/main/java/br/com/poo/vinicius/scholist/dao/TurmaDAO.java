package br.com.poo.vinicius.scholist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.poo.vinicius.scholist.model.Turma;

public class TurmaDAO extends SQLiteOpenHelper implements GlobalDAO<Turma>{
    private static TurmaDAO uniqueInstance;

    private TurmaDAO(Context context) {
        super(context, "Agenda", null, 2);
    }
    public static TurmaDAO getInstance(Context context) {
        if(uniqueInstance == null) {
            uniqueInstance = new TurmaDAO(context);
        }
        return uniqueInstance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Prova (id INTEGER PRIMARY KEY, " +
                "user USER NOT NULL, " +
                "nome TEXT NOT NULL, " +
                "descricao TEXT NOT NULL, " +
                "profileUrl TEXT NOT NULL, " +
                "uuid TEXT NOT NULL, " +
                "post POST, " +
                "users USER, " +
                "uuidAdmin TEXT NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "";
        switch (oldVersion) {
            case 1:
                sql = "ALTER TABLE TurmaDAO ADD COLUMN horario TEXT";
                db.execSQL(sql); // indo para versao 2
        }
    }

    @Override
    public void insere(Turma object) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = getContentValuesTurma(object);
        db.insert("Turma",null, dados);
    }

    @NonNull
    private ContentValues getContentValuesTurma(Turma turma) {
        ContentValues dados = new ContentValues();
        dados.put("nome", turma.getNome());
        dados.put("descricao", turma.getDescricao());
        dados.put("profileUrl", turma.getProfileUrl());
        dados.put("uuid", turma.getUuid());
        dados.put("uuidAdmin", turma.getUuidAdmin());
        dados.put("id", turma.getId());

        return dados;
    }

    public List<Turma> buscaTurmas() {
        String sql = "SELECT * FROM Turma;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);

        List<Turma> turmas = new ArrayList<Turma>();
        while (c.moveToNext()) {
            Turma turma = new Turma();
            turma.setId(c.getLong(c.getColumnIndex("id")));
            turma.setNome(String.valueOf(c.getLong((c.getColumnIndex("nome")))));
            turma.setDescricao(String.valueOf(c.getLong((c.getColumnIndex("descricao")))));
            turma.setProfileUrl(String.valueOf(c.getLong((c.getColumnIndex("profileUrl")))));
            turma.setUuid(String.valueOf(c.getLong((c.getColumnIndex("uuid")))));
            turma.setUuidAdmin(String.valueOf(c.getLong((c.getColumnIndex("uuidAdmin")))));

            turmas.add(turma);
        }
        c.close();
        return turmas;
    }
    public void delete(Turma object) {
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {String.valueOf(object.getId())};
        db.delete("Turma", "id = ?", params);

    }
    public void update(Turma object) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = getContentValuesTurma(object);
        String[] params = {String.valueOf(object.getId())};
        db.update("Turma", dados, "id = ?", params);
    }
}