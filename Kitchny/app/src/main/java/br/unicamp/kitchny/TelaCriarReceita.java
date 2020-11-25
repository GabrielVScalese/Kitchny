package br.unicamp.kitchny;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.unicamp.kitchny.kotlin.Ingrediente;
import br.unicamp.kitchny.kotlin.Receita;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TelaCriarReceita extends AppCompatActivity {

    private ListView listViewIngrediente;
    private ListView listViewModoDePreparo;
    private List<Ingrediente> listaIngredientes;
    private List<String> listaModoDePreparo;
    private EditText edtNomeReceita;
    private Button btnSendReceita;

    private ImageView imgUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_criar_receita);

        listViewIngrediente = findViewById(R.id.listaIngredientesEscolhidos);
        listViewModoDePreparo = findViewById(R.id.listaModoDePreparo);
        edtNomeReceita = findViewById(R.id.tvTituloReceitaEscolhido);
        btnSendReceita = findViewById(R.id.btnSendReceita);
        //imgUpload = findViewById()

        listaIngredientes = new ArrayList<>();
        listaIngredientes.add(new Ingrediente("", ""));
        IngredienteEscolhidoAdapter adapter = new IngredienteEscolhidoAdapter(TelaCriarReceita.this, R.layout.ingrediente_usuario_item, listaIngredientes);

        listaModoDePreparo = new ArrayList<>();
        listaModoDePreparo.add("");
        ModoDePreparoAdapter adapter2 = new ModoDePreparoAdapter(TelaCriarReceita.this, R.layout.modo_de_preparo_item, listaModoDePreparo);

        listViewModoDePreparo.setAdapter(adapter2);
        listViewIngrediente.setAdapter(adapter);

        btnSendReceita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listaIngredientes.size() == 1 || listaModoDePreparo.size() == 1 || edtNomeReceita.getText().toString().equals("")) {
                    Toast.makeText(TelaCriarReceita.this, "Valores fornecidos são inválidos", Toast.LENGTH_SHORT).show();
                    return;
                }

                inserirReceita();
                inserirIngredientes();
                limparListas();
            }
        });
    }

    private void inserirReceita() {
        Receita receita = new Receita(edtNomeReceita.getText().toString(), "", getModoDePreparo(), "", 0.0F);
        Call<Status> call = new RetrofitConfig().getService().inserirReceita(receita);
        call.enqueue(new Callback<Status>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Response<Status> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Toast.makeText(TelaCriarReceita.this, "Receita inserida com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TelaCriarReceita.this, "Falha na inserção de receita", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(TelaCriarReceita.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void inserirIngredientes() {
        Call<Status> call = new RetrofitConfig().getService().inserirIngredientes(edtNomeReceita.getText().toString(), getIngredientes());
        call.enqueue(new Callback<Status>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Response<Status> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    //Toast.makeText(TelaCriarReceita.this, "Receita inserida com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TelaCriarReceita.this, "Falha na inserção de ingredientes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(TelaCriarReceita.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getModoDePreparo() {
        String ret = "";

        for (int i = 1; i < listaModoDePreparo.size(); i++)
            ret += listaModoDePreparo.get(i) + "\n";

        return ret;
    }

    private Ingrediente[] getIngredientes() {
        Ingrediente[] ret = new Ingrediente[listaIngredientes.size() - 1];

        for (int i = 1; i < listaIngredientes.size(); i++)
            ret[i - 1] = listaIngredientes.get(i);

        return ret;
    }

    private void limparListas() {
        for (int i = 1; i < listaIngredientes.size(); i++)
            listaIngredientes.remove(i);

        for (int i = 1; i < listaModoDePreparo.size(); i++)
            listaModoDePreparo.remove(i);
    }
}