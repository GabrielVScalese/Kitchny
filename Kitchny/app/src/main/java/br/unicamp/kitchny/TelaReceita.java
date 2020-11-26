package br.unicamp.kitchny;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.List;

import br.unicamp.kitchny.kotlin.Ingrediente;
import br.unicamp.kitchny.kotlin.Receita;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

// Janela de uma determinada receita
public class TelaReceita extends AppCompatActivity {

    private TextView tvTituloReceita;
    private TextView tvModoDePreparo;
    private ListView listViewIngredientes;
    private ImageView imagemReceita;

    // Estrelas que demonstram a avaliação da receita
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ImageView star4;
    private ImageView star5;

    // Vetor de estrelas
    private ImageView[] vetorDeEstrelas;

    // Estrelas que servem para avaliar a receita
    private ImageView aval1;
    private ImageView aval2;
    private ImageView aval3;
    private ImageView aval4;
    private ImageView aval5;

    // Vetor de estrelas para avaliacao
    private ImageView[] vetorDeAval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_receita);

        tvTituloReceita = findViewById(R.id.tvTituloReceita);
        tvModoDePreparo = findViewById(R.id.tvModoDePreparo);
        listViewIngredientes = findViewById(R.id.listaIngredientes);
        imagemReceita = findViewById(R.id.imgReceita);

        // Estrelas para nota de receita
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);
        vetorDeEstrelas = new ImageView[5];
        vetorDeEstrelas[0] = star1;
        vetorDeEstrelas[1] = star2;
        vetorDeEstrelas[2] = star3;
        vetorDeEstrelas[3] = star4;
        vetorDeEstrelas[4] = star5;

        // Estrelas para avaliar receita
        aval1 = findViewById(R.id.aval1);
        aval2 = findViewById(R.id.aval2);
        aval3 = findViewById(R.id.aval3);
        aval4 = findViewById(R.id.aval4);
        aval5 = findViewById(R.id.aval5);
        vetorDeAval = new ImageView[5];
        vetorDeAval[0] = aval1;
        vetorDeAval[1] = aval2;
        vetorDeAval[2] = aval3;
        vetorDeAval[3] = aval4;
        vetorDeAval[4] = aval5;

        // Carregar imagem da receita


        Bundle bundle = getIntent().getExtras();

        String nomeReceita = bundle.getString("nomeReceita");

        getIngredientesFromNomeReceita(nomeReceita);
        getReceita(nomeReceita);

        aval1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAvalicaoReceita(1);
            }
        });

        aval2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAvalicaoReceita(2);
            }
        });

        aval3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAvalicaoReceita(3);
            }
        });

        aval4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAvalicaoReceita(4);
            }
        });

        aval5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAvalicaoReceita(5);
            }
        });
    }

    private void getIngredientesFromNomeReceita (String nomeReceita)
    {
        Call<List<Ingrediente>> call = new RetrofitConfig().getService().getIngredientesFromNomeReceita(nomeReceita);
        call.enqueue(new Callback<List<Ingrediente>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Response<List<Ingrediente>> response, Retrofit retrofit) {
                if(response.isSuccess()) {
                    List<Ingrediente> listaIngredientes;
                    listaIngredientes = response.body();
                    IngredienteAdapter adapter = new IngredienteAdapter(TelaReceita.this, R.layout.ingrediente_item, listaIngredientes);
                    listViewIngredientes.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(TelaReceita.this, "Falha na busca de ingredientes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(TelaReceita.this, "Falha na busca de ingredientes", Toast.LENGTH_SHORT).show();
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
                    Receita receita;
                    receita = response.body();
                    tvTituloReceita.setText(receita.getNome());
                    tvModoDePreparo.setText(receita.getModoDePreparo());
                    setNotaReceita(receita.getAvaliacao());
                    DownloadImageTask dt = new DownloadImageTask();
                    dt.execute(receita.getImagem());
                }
                else
                    Toast.makeText(TelaReceita.this, "Falha na busca de receita", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(TelaReceita.this, "Falha na busca de receita", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateAvaliacao(float nota)
    {
        Receita receita = new Receita("Paella", nota);
        Call<Status> call = new RetrofitConfig().getService().updateAvaliacao(receita);
        call.enqueue(new Callback<Status>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Response<Status> response, Retrofit retrofit) {
                if(response.isSuccess()) {
                    Toast.makeText(TelaReceita.this, "Avaliação enviada com sucesso", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try
                    {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(TelaReceita.this, jObjError.getString("status"), Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e)
                    { }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(TelaReceita.this, "Falha na busca de receita", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setNotaReceita (float nota)
    {
        int conversao = (int) nota / 2;

        for (int i = 0; i < conversao; i++)
            vetorDeEstrelas[i].setImageResource(R.drawable.baseline_star_24);
    }

    private void setAvalicaoReceita (int qtdAval)
    {
        for (int i = 0; i < vetorDeAval.length; i++)
             vetorDeAval[i].setImageResource(R.drawable.baseline_star_border);

        for (int i = 0; i < qtdAval; i++)
            vetorDeAval[i].setImageResource(R.drawable.baseline_star_24);

        updateAvaliacao(qtdAval * 2);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... urls) {
            if (urls[0].contains("content")) {
                try {
                    InputStream in = new java.net.URL("https://www.safraes.com.br/arquivos/noticias/2961_cafeicultores_pomeranos._na_casa_do_elmario_seidler_na_zona_rural_de_itarana_nos_deparamos_com_essa_placa_cuja_frase_em_portugues_quer_dizer_sempre_feliz_nunca_triste._foto_de_leandro_fidelis.jpg").openStream();
                    Bitmap mIcon11 = BitmapFactory.decodeStream(in);
                    return mIcon11;
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
            }
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            imagemReceita.setImageBitmap(Bitmap.createScaledBitmap(result, 300, 150, false));
        }
    }
}