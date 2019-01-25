package br.com.poo.vinicius.scholist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.List;

import br.com.poo.vinicius.scholist.model.Prova;

public class DetalhesProvaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_prova);

        List<String> topicosPort = Arrays.asList("Sujeito", "Predicado", "Objeto");
        Prova provaPortugues = new Prova("Português", "25/05/2018", topicosPort);


        Intent intentDetails = getIntent();
        Prova prova = (Prova) intentDetails.getSerializableExtra("prova");

        TextView materiaTxt = (TextView) findViewById(R.id.detalhes_prova_materia);
        TextView dataTxt = (TextView) findViewById(R.id.detalhes_prova_data);
        ListView listaTopicos = (ListView) findViewById(R.id.detalhes_prova_topicos);


        materiaTxt.setText(prova.getMateria());
        dataTxt.setText(prova.getData());


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, prova.getTopicos());
        listaTopicos.setAdapter(adapter);

    }
}
