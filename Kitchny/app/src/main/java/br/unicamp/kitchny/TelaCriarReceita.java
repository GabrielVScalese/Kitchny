package br.unicamp.kitchny;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.unicamp.kitchny.kotlin.Ingrediente;

public class TelaCriarReceita extends AppCompatActivity {

    private ListView listViewIngrediente;
    private ListView listViewModoDePreparo;
    private List<Ingrediente> listaIngredientes;
    private List<String> listaModoDePreparo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_criar_receita);

        listViewIngrediente = findViewById(R.id.listaIngredientesEscolhidos);
        listViewModoDePreparo = findViewById(R.id.listaModoDePreparo);

        listaIngredientes = new ArrayList<>();
        listaIngredientes.add(new Ingrediente("Digite um ingrediente", "Digite a quantidade"));
        IngredienteEscolhidoAdapter adapter = new IngredienteEscolhidoAdapter(TelaCriarReceita.this, R.layout.ingrediente_usuario_item, listaIngredientes);

        listaModoDePreparo = new ArrayList<>();
        listaModoDePreparo.add("Digite o 1º passo");
        ModoDePreparoAdapter adapter2 = new ModoDePreparoAdapter(TelaCriarReceita.this, R.layout.modo_de_preparo_item, listaModoDePreparo);

        listViewModoDePreparo.setAdapter(adapter2);
        listViewIngrediente.setAdapter(adapter);
    }
}