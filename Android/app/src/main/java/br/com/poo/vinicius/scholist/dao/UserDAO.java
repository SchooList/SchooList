package br.com.poo.vinicius.scholist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.poo.vinicius.scholist.model.User;

public class UserDAO extends SQLiteOpenHelper implements GlobalDAO<User>{
    private static UserDAO uniqueInstance;

    private UserDAO(Context context) {
        super(context, "Agenda", null, 2);
    }
    public static UserDAO getInstance(Context context) {
        if(uniqueInstance == null) {
            uniqueInstance = new UserDAO(context);
        }
        return uniqueInstance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Prova (id INTEGER PRIMARY KEY, " +
                "username TEXT NOT NULL, " +
                "uuid TEXT NOT NULL, " +
                "profileUrl TEXT NOT NULL, " +
                "tipo TEXT NOT NULL, " +
                "idTurmas LONG NOT NULL, " +
                "turmas TURMA NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "";
        switch (oldVersion) {
            case 1:
                sql = "ALTER TABLE UserDAO ADD COLUMN endereco TEXT";
                db.execSQL(sql); // indo para versao 2
        }
    }

    @Override
    public void insere(User object) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = getContentValuesUser(object);
        db.insert("User",null, dados);
    }

    @NonNull
    private ContentValues getContentValuesUser(User user) {
        ContentValues dados = new ContentValues();
        dados.put("username", user.getUsername());
        dados.put("uuid", user.getUuid());
        dados.put("profileUrl", user.getProfileUrl());
        dados.put("tipo", user.getTipo());
        dados.put("idTurmas", String.valueOf(user.getIdTurmas()));
        dados.put("turmas", String.valueOf(user.getTurmas()));
        dados.put("id", user.getId());
        return dados;
    }

    public List<User> buscaUsers() {
        String sql = "SELECT * FROM User;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);

        List<User> users = new ArrayList<User>();
        while (c.moveToNext()) {
            User user = new User();
            user.setId(c.getLong(c.getColumnIndex("id")));
            user.setUsername(c.getString(c.getColumnIndex("username")));
            user.setUuid(c.getString(c.getColumnIndex("uuid")));
            user.setProfileUrl(c.getString(c.getColumnIndex("profileUrl")));
            user.setTipo(c.getString(c.getColumnIndex("tipo")));
            users.add(user);
        }
        c.close();
        return users;
    }
    public void delete(User object) {
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {String.valueOf(object.getId())};
        db.delete("User", "id = ?", params);

    }
    public void update(User object) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = getContentValuesUser(object);
        String[] params = {String.valueOf(object.getId())};
        db.update("User", dados, "id = ?", params);
    }
}