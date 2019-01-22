package br.com.poo.vinicius.scholist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import br.com.poo.vinicius.scholist.model.Prova;

public class ProvasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provas);
        List<String> topicosPort = Arrays.asList("Sujeito", "Predicado", "Objeto");
        Prova provaPortugues = new Prova("Português", "25/05/2018", topicosPort);

        List<String> topicosMat = Arrays.asList("Função", "Probabilidade");
        Prova provaMatematica = new Prova("Matemática", "26/05/2018", topicosMat);


        List<Prova> provas = Arrays.asList(provaMatematica, provaPortugues);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, provas);

        ListView listaProvas  = (ListView) findViewById(R.id.provas_lista);
        listaProvas.setAdapter(adapter);

        listaProvas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Prova prova = (Prova) parent.getItemAtPosition(position);
                Intent goToDetails = new Intent(ProvasActivity.this, DetalhesProvaActivity.class);
                //goToDetails.putExtra("prova", prova);
                startActivity(goToDetails);
            }
        });
    }
}
