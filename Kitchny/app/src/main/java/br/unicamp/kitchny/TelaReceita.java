package br.unicamp.kitchny;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.unicamp.kitchny.kotlin.Compra;
import br.unicamp.kitchny.kotlin.Ingrediente;
import br.unicamp.kitchny.kotlin.Receita;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TelaReceita extends AppCompatActivity {

    private TextView tvTituloReceita;
    private TextView tvModoDePreparo;
    private ListView listViewIngredientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_receita);

        tvTituloReceita = findViewById(R.id.tvTituloReceita);
        tvModoDePreparo = findViewById(R.id.tvModoDePreparo);
        listViewIngredientes = findViewById(R.id.listaIngredientes);

        getIngredientesFromNomeReceita("Paella");
        getReceita("Paella de Petrúcio");
    }

    private void getIngredientesFromNomeReceita (String nomeReceita)
    {
        Call<List<Ingrediente>> call = new RetrofitConfig().getService().getIngredientesFromNomeReceita(nomeReceita);
        call.enqueue(new Callback<List<Ingrediente>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Response<List<Ingrediente>> response, Retrofit retrofit) {
                if(response.isSuccess()) {
                    List<Ingrediente> listaIngredientes = null;
                    listaIngredientes = response.body();
                    IngredienteAdapter adapter = new IngredienteAdapter(TelaReceita.this, R.layout.ingrediente_item, listaIngredientes);
                    listViewIngredientes.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(TelaReceita.this, "Falha na busca de lista de compras", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(TelaReceita.this, "Falha na busca de lista de compras", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getReceita (String nomeReceita)
    {
        Call<Receita> call = new RetrofitConfig().getService().getReceita(nomeReceita);
        call.enqueue(new Callback<Receita>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Response<Receita> response, Retrofit retrofit) {
                if(response.isSuccess()) {
                    Receita receita = null;
                    receita = response.body();
                    tvTituloReceita.setText(receita.getNome());
                    tvModoDePreparo.setText(receita.getModoDePreparo());
                }
                else
                {
                    Toast.makeText(TelaReceita.this, "Falha na busca de lista de compras", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(TelaReceita.this, "Falha na busca de lista de compras", Toast.LENGTH_SHORT).show();
            }
        });
    }
}