package br.unicamp.kitchny;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.unicamp.kitchny.kotlin.Compra;
import br.unicamp.kitchny.kotlin.Receita;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

// Janela de exibição de lista de receitas
public class TelaInicial extends AppCompatActivity {

    private ListView listView;
    private EditText edtNomeReceita;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        listView = findViewById(R.id.listaReceitas);
        edtNomeReceita = findViewById(R.id.edtNomeReceita);
        getListaReceitas();

        edtNomeReceita.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (edtNomeReceita.getRight() - edtNomeReceita.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        String nomeReceita = edtNomeReceita.getText().toString();
                        if (!nomeReceita.equals(""))
                            getReceita(edtNomeReceita.getText().toString());
                        else
                            getListaReceitas();

                        return true;
                    }
                }

                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Receita receita = (Receita) listView.getItemAtPosition(position);

                Intent intent = new Intent(TelaInicial.this, TelaReceita.class);

                Bundle bundle = new Bundle();

                bundle.putString("nomeReceita", receita.getNome());

                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
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

    private void getReceita (String pesquisa)
    {
        Call<List<Receita>> call = new RetrofitConfig().getService().getReceitaFromPesquisa(pesquisa);
        call.enqueue(new Callback<List<Receita>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Response<List<Receita>> response, Retrofit retrofit) {
                if(response.isSuccess()) {
                    List<Receita> listaReceita = new ArrayList<>();
                    listaReceita = response.body();
                    ReceitaAdapter adapter = new ReceitaAdapter(TelaInicial.this, R.layout.receita_item, listaReceita);
                    listView.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(TelaInicial.this, "Receita não encontrada!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(TelaInicial.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}