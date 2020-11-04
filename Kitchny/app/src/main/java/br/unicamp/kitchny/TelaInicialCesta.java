package br.unicamp.kitchny;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    TextView tvTitulo;
    ImageView btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial_cesta);

        listView = findViewById(R.id.listaCompras);
        tvTitulo = findViewById(R.id.tvTitulo);
        btnClear = findViewById(R.id.btnClear);

        getListaDeCompras("gabriel.scalese@hotmail.com");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Compra compra = (Compra)listView.getItemAtPosition(position);
                AlertDialog.Builder alert = new AlertDialog.Builder(TelaInicialCesta.this);
                alert.setTitle("" + compra.getNomeIngrediente());
                alert.show();
            }
        });
    }

    private void getListaDeCompras(String email)
    {
        Call<List<Compra>> call = new RetrofitConfig().getService().getListaDeCompras(email);
        call.enqueue(new Callback<List<Compra>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Response<List<Compra>> response, Retrofit retrofit) {
                if(response.isSuccess()) {
                    List<Compra> listaCompra = null;
                    listaCompra = response.body();
                    CompraAdapter adapter = new CompraAdapter(TelaInicialCesta.this, R.layout.compra_item, listaCompra);
                    listView.setAdapter(adapter);
                    tvTitulo.setText("Você possui " + listaCompra.size() + " ingrediente(s)");
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