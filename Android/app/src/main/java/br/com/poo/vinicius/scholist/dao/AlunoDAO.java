package br.com.poo.vinicius.scholist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.poo.vinicius.scholist.model.Aluno;

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

    public void insere(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = new ContentValues();
        dados.put("nome", aluno.getNome());
        dados.put("endereco", aluno.getEndereco());
        dados.put("telefone", aluno.getTelefone());
        dados.put("site", aluno.getSite());
        dados.put("nota",aluno.getNota());


        db.insert("Alunos",null, dados);
    }
}
