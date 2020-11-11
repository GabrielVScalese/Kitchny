package br.unicamp.kitchny;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.unicamp.kitchny.kotlin.Compra;
import br.unicamp.kitchny.kotlin.Receita;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TelaInicial extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);
        listView = findViewById(R.id.listaReceitas);
        getListaReceitas();
    }

    private void getListaReceitas()
    {
        Call<List<Receita>> call = new RetrofitConfig().getService().getReceitas();
        call.enqueue(new Callback<List<Receita>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Response<List<Receita>> response, Retrofit retrofit) {
                if(response.isSuccess()) {
                    List<Receita> listaReceita = null;
                    listaReceita = response.body();
                    ReceitaAdapter adapter = new ReceitaAdapter(TelaInicial.this, R.layout.receita_item, listaReceita);
                    listView.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(TelaInicial.this, "Falha no carregamento das receitas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(TelaInicial.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}