package br.com.poo.vinicius.scholist.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.telephony.SmsMessage;
import android.widget.Toast;

import br.com.poo.vinicius.scholist.R;
import br.com.poo.vinicius.scholist.dao.AlunoDAO;

public class SMSReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {

        Object []pdus = (Object[]) intent.getSerializableExtra("pdus");
        byte[] pdu = (byte[])pdus[0];
        String formato = (String) intent.getSerializableExtra("format");
        AlunoDAO dao = AlunoDAO.getInstance(context);

        SmsMessage sms = SmsMessage.createFromPdu(pdu, formato);
        String telefone = sms.getDisplayOriginatingAddress();

        if(dao.verifyStudent(telefone)) {
            Toast.makeText(context, "SMS De um aluno recebido!", Toast.LENGTH_SHORT).show();
            MediaPlayer mp = MediaPlayer.create(context, R.raw.msg);
            mp.start();
        }


    }


}
