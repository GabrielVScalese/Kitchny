package br.unicamp.kitchny;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import br.unicamp.kitchny.kotlin.Ingrediente;
import br.unicamp.kitchny.kotlin.Receita;
import br.unicamp.kitchny.kotlin.Status;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TelaCriarReceita extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private ListView listViewIngrediente;
    private ListView listViewModoDePreparo;
    private List<Ingrediente> listaIngredientes;
    private List<String> listaModoDePreparo;
    private EditText edtNomeReceita;
    private EditText edtEnderecoImagem;
    private Button btnSendReceita;
    private ImageView imgReceita;
    private BottomNavigationView menu;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_criar_receita);

        menu = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        listViewIngrediente = findViewById(R.id.listaIngredientesEscolhidos);
        listViewModoDePreparo = findViewById(R.id.listaModoDePreparo);
        edtNomeReceita = findViewById(R.id.tvTituloReceitaEscolhido);
        edtEnderecoImagem = findViewById(R.id.edtEnderecoImagem);
        btnSendReceita = findViewById(R.id.btnSendReceita);
        imgReceita = findViewById(R.id.addImage);
        session = new Session(TelaCriarReceita.this);

        listaIngredientes = new ArrayList<>();
        listaIngredientes.add(new Ingrediente("", ""));
        IngredienteEscolhidoAdapter adapter = new IngredienteEscolhidoAdapter(TelaCriarReceita.this, R.layout.ingrediente_usuario_item, listaIngredientes);

        listaModoDePreparo = new ArrayList<>();
        listaModoDePreparo.add("");
        ModoDePreparoAdapter adapter2 = new ModoDePreparoAdapter(TelaCriarReceita.this, R.layout.modo_de_preparo_item, listaModoDePreparo);

        listViewModoDePreparo.setAdapter(adapter2);
        listViewIngrediente.setAdapter(adapter);

        menu.setOnNavigationItemSelectedListener(this);

        btnSendReceita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listaIngredientes.size() == 1 || listaModoDePreparo.size() == 1 || edtNomeReceita.getText().toString().equals("") || edtEnderecoImagem.getText().toString().equals("")) {
                    Toast.makeText(TelaCriarReceita.this, "Valores fornecidos são inválidos", Toast.LENGTH_SHORT).show();
                    return;
                }

                inserirReceita();
                inserirIngredientes();
                limparListas();
            }
        });

        // Evento enter
        edtEnderecoImagem.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (event == null || !event.isShiftPressed()) {
                                DownloadImageTask dt = new DownloadImageTask();
                                dt.execute(edtEnderecoImagem.getText().toString());
                                return true; // consume.
                            }
                        }
                        return false; // pass on to other listeners.
                    }
                }
        );

        if(session.getTela() == 1)
        {
            MenuItem menuItem = menu.getMenu().getItem(0);
            menuItem.setChecked(true);
        }
        else
        {
            MenuItem menuItem = menu.getMenu().getItem(1);
            menuItem.setChecked(true);
        }
    }

    private void inserirReceita() {
        Receita receita = new Receita(edtNomeReceita.getText().toString(), "", getModoDePreparo(), edtEnderecoImagem.getText().toString(), 0.0F);

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
            ret += i + " - " + listaModoDePreparo.get(i) + "\n";

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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
                Toast.makeText(TelaCriarReceita.this, "Endereço de imagem inválido!", Toast.LENGTH_SHORT).show();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            imgReceita.setImageBitmap(Bitmap.createScaledBitmap(result, 300, 150, false));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.page_1: {
                session.setTela(1);
                Intent intent = new Intent(TelaCriarReceita.this, TelaInicial.class);
                startActivity(intent);
                break;
            }
            case R.id.page_2: {
                session.setTela(2);
                Intent intent = new Intent(TelaCriarReceita.this, TelaInicialUsuario.class);
                startActivity(intent);
                break;
            }
        }

        return true;
    }
}