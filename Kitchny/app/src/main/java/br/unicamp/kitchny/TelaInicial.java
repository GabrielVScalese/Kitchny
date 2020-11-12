package br.unicamp.kitchny;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.unicamp.kitchny.kotlin.Compra;
import br.unicamp.kitchny.kotlin.Receita;
import kotlin.collections.builders.ListBuilder;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TelaInicial extends AppCompatActivity {

    ListView listView;
    EditText edtNomeReceita;

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
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                @SuppressLint("ClickableViewAccessibility") final int DRAWABLE_BOTTOM = 3;

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
    }

    private void getReceita (String nomeReceita)
    {
        Call<Receita> call = new RetrofitConfig().getService().getReceita(nomeReceita);
        call.enqueue(new Callback<Receita>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Response<Receita> response, Retrofit retrofit) {
                if(response.isSuccess()) {
                    List<Receita> listaReceita = new ArrayList<>();
                    listaReceita.add(response.body());
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