package br.com.poo.vinicius.scholist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.poo.vinicius.scholist.model.Post;

public class PostDAO extends SQLiteOpenHelper implements GlobalDAO<Post>{

    private static PostDAO uniqueInstance;

    private PostDAO(Context context) {
        super(context, "Agenda", null, 2);
    }

    public static PostDAO getInstance(Context context) {
        if(uniqueInstance == null) {
            uniqueInstance = new PostDAO(context);
        }
        return uniqueInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Message (id INTEGER PRIMARY KEY, " +
                "descricao TEXT NOT NULL, " +
                "timeStamp LONG)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "";
        switch (oldVersion) {
            case 1:
                sql = "ALTER TABLE Message ADD COLUMN caminhoFoto TEXT";
                db.execSQL(sql); // indo para versao 2
        }
    }

    public void insere(Post object) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = getContentValuesPost(object);
        db.insert("Post",null, dados);
    }

    @NonNull
    private ContentValues getContentValuesPost(Post post) {
        ContentValues dados = new ContentValues();
        dados.put("id", post.getId());
        dados.put("descricao", post.getDescricao());
        dados.put("timeStamp", post.getTimestamp());
        return dados;
    }

    public List<Post> buscaPosts() {
        String sql = "SELECT * FROM Post;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);

        List<Post> posts = new ArrayList<Post>();
        while (c.moveToNext()) {
            Post post = new Post();
            post.setId(c.getLong(c.getColumnIndex("id")));
            post.setDescricao(c.getString(c.getColumnIndex("descricao")));
            post.setTimestamp(c.getString(c.getColumnIndex("timeStamp")));
            posts.add(post);
        }
        c.close();
        return posts;
    }

    public void delete(Post object) {
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {object.getId().toString()};
        db.delete("Post", "id = ?", params);
    }

    public void update(Post object) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = getContentValuesPost(object);
        String[] params = {object.getId().toString()};
        db.update("Post", dados, "id = ?", params);
    }
}