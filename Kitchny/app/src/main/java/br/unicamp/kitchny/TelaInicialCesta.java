package br.unicamp.kitchny;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.unicamp.kitchny.kotlin.Compra;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class TelaInicialCesta extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial_cesta);

        listView = findViewById(R.id.listaCompras);

        getListaDeCompras("gabriel.scalese@hotmail.com");
    }

    private void getListaDeCompras(String email)
    {
        Call<List<Compra>> call = new RetrofitConfig().getService().getListaDeCompras(email);
        call.enqueue(new Callback<List<Compra>>() {
            @Override
            public void onResponse(Response<List<Compra>> response, Retrofit retrofit) {
                if(response.isSuccess()) {
                    List<Compra> listaCompra = null;
                    listaCompra = response.body();
                    CompraAdapter adapter = new CompraAdapter(TelaInicialCesta.this, R.layout.compra_item, listaCompra);
                    listView.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(TelaInicialCesta.this, "Falha na busca de lista de compras", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}