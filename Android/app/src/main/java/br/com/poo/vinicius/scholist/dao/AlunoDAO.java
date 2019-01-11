package br.com.poo.vinicius.scholist.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlunoDAO extends SQLiteOpenHelper {

    private static AlunoDAO uniqueInstance;

    private AlunoDAO(Context context) {
        super(context, "Agenda", null, 1);
    }

    public static AlunoDAO getInstance(Context context) {
        if(uniqueInstance == null) {
            uniqueInstance = new AlunoDAO(context);
        }
        return uniqueInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            String sql = "CREATE TABLE Alunos (id INTEGER PRIMARY KEY, nome TEXT NOT NULL, endereco TEXT, telefone TEXT, site TEXT, nota REAL);";
            db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS Alunos";
        db.execSQL(sql);
        onCreate(db);
    }
}
