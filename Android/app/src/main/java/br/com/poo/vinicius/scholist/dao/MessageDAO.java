package br.com.poo.vinicius.scholist.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.poo.vinicius.scholist.model.Message;

public class MessageDAO extends SQLiteOpenHelper implements GlobalDAO<Message>{

    private static MessageDAO uniqueInstance;

    private MessageDAO(Context context) {
        super(context, "Agenda", null, 2);
    }

    public static MessageDAO getInstance(Context context) {
        if(uniqueInstance == null) {
            uniqueInstance = new MessageDAO(context);
        }
        return uniqueInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Message (id INTEGER PRIMARY KEY, " +
                "text TEXT NOT NULL, " +
                "timeStamp LONG, " +
                "fromId TEXT);";
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

    public void insere(Message object) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = getContentValuesMessage(object);
        db.insert("Message",null, dados);
    }

    @NonNull
    private ContentValues getContentValuesMessage(Message message) {
        ContentValues dados = new ContentValues();
        dados.put("id", message.getId());
        dados.put("text", message.getText());
        dados.put("timeStamp", message.getTimeStamp());
        dados.put("fromId", message.getFromId());
        return dados;
    }

    public List<Message> buscaMessages() {
        String sql = "SELECT * FROM Message;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);

        List<Message> messages = new ArrayList<Message>();
        while (c.moveToNext()) {
            Message message = new Message();
            message.setId(c.getString(c.getColumnIndex("id")));
            message.setText(c.getString(c.getColumnIndex("text")));
            message.setTimeStamp(c.getLong(c.getColumnIndex("timeStamp")));
            message.setFromId(c.getString(c.getColumnIndex("fromId")));
            messages.add(message);
        }
        c.close();
        return messages;
    }

    public void delete(Message object) {
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {object.getId().toString()};
        db.delete("Message", "id = ?", params);

    }

    public void update(Message object) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = getContentValuesMessage(object);
        String[] params = {object.getId().toString()};
        db.update("Message", dados, "id = ?", params);
    }
}