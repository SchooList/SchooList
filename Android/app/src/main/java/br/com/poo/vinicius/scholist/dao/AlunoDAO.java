package br.com.poo.vinicius.scholist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.poo.vinicius.scholist.model.Aluno;

public class AlunoDAO extends SQLiteOpenHelper implements GlobalDAO<Aluno>{

    private static AlunoDAO uniqueInstance;

    private AlunoDAO(Context context) {
        super(context, "Agenda", null, 2);
    }
    public static AlunoDAO getInstance(Context context) {
        if(uniqueInstance == null) {
            uniqueInstance = new AlunoDAO(context);
        }
        return uniqueInstance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Alunos (id INTEGER PRIMARY KEY, " +
                "nome TEXT NOT NULL, " +
                "endereco TEXT, " +
                "telefone TEXT, " +
                "site TEXT, " +
                "nota REAL, " +
                "caminhoFoto TEXT);";
            db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "";
        switch (oldVersion) {
            case 1:
                sql = "ALTER TABLE Alunos ADD COLUMN caminhoFoto TEXT";
                db.execSQL(sql); // indo para versao 2
        }
    }

    public void insere(Aluno object) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues dados = getContentValuesAluno(object);
            db.insert("Alunos",null, dados);


    }

    @NonNull
    private ContentValues getContentValuesAluno(Aluno aluno) {
        ContentValues dados = new ContentValues();
        dados.put("nome", aluno.getNome());
        dados.put("endereco", aluno.getEndereco());
        dados.put("telefone", aluno.getTelefone());
        dados.put("site", aluno.getSite());
        dados.put("nota", aluno.getNota());
        dados.put("caminhoFoto", aluno.getCaminhoFoto());
        return dados;
    }

    public List<Aluno> buscaAlunos() {
        String sql = "SELECT * FROM Alunos;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);

        List<Aluno> alunos = new ArrayList<Aluno>();
        while (c.moveToNext()) {
            Aluno aluno = new Aluno();
            aluno.setId(c.getLong(c.getColumnIndex("id")));
            aluno.setNome(c.getString(c.getColumnIndex("nome")));
            aluno.setEndereco(c.getString(c.getColumnIndex("endereco")));
            aluno.setTelefone(c.getString(c.getColumnIndex("telefone")));
            aluno.setSite(c.getString(c.getColumnIndex("site")));
            aluno.setNota(c.getDouble(c.getColumnIndex("nota")));
            aluno.setCaminhoFoto(c.getString(c.getColumnIndex("caminhoFoto")));

            alunos.add(aluno);
        }
        c.close();
        return alunos;
    }
    public void delete(Aluno object) {
            SQLiteDatabase db = getWritableDatabase();
            String[] params = {object.getId().toString()};
            db.delete("Alunos", "id = ?", params);

    }
    public void update(Aluno object) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues dados = getContentValuesAluno(object);
            String[] params = {object.getId().toString()};
            db.update("Alunos", dados, "id = ?", params);
    }

    public boolean verifyStudent(String telefone) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Alunos WHERE telefone = ?", new String[]{telefone});
        int result = c.getCount();
        c.close();
        return result > 0;
    }


}
