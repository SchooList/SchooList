package br.com.poo.vinicius.scholist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import br.com.poo.vinicius.scholist.model.Prova;
import br.com.poo.vinicius.scholist.view.ProvasActivity;

public class ListaProvasFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_provas, container,false);
        List<String> topicosMat = Arrays.asList("Função", "Probabilidade");
        Prova provaMatematica = new Prova("Matemática", "26/05/2018", topicosMat);
        List<String> topicosPort = Arrays.asList("Sujeito", "Predicado", "Objeto");
        Prova provaPortugues = new Prova("Português", "25/05/2018", topicosPort);


        List<Prova> provas = Arrays.asList(provaMatematica, provaPortugues);

        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_expandable_list_item_1, provas);

        ListView listaProvas  = (ListView) view.findViewById(R.id.provas_lista);
        listaProvas.setAdapter(adapter);

        listaProvas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Prova prova = (Prova) parent.getItemAtPosition(position);

                ProvasActivity provasActivity = (ProvasActivity) getActivity();
                provasActivity.selecionaProva(prova);


            }
        });


        return view;
    }
}
