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

import java.io.InputStream;
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
        DownloadImageTask dt = new DownloadImageTask();
        dt.execute("https://i.pinimg.com/474x/95/ca/e9/95cae9537671e3f62cbe6e6d8ab8b962.jpg");

        // Obter dados da receita
        getIngredientesFromNomeReceita("Paella");
        getReceita("Paella");

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
                    List<Ingrediente> listaIngredientes = null;
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
                    Receita receita = null;
                    receita = response.body();
                    tvTituloReceita.setText(receita.getNome());
                    tvModoDePreparo.setText(receita.getModoDePreparo());
                    setNotaReceita(receita.getAvaliacao());
                }
                else
                {
                    Toast.makeText(TelaReceita.this, "Falha na busca de receita", Toast.LENGTH_SHORT).show();
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
            vetorDeEstrelas[i].setImageResource(R.drawable.ic_baseline_star_24);
    }

    private void setAvalicaoReceita (int qtdAval)
    {
        for (int i = 0; i < vetorDeAval.length; i++)
             vetorDeAval[i].setImageResource(R.drawable.ic_baseline_star_border_24);

        for (int i = 0; i < qtdAval; i++)
            vetorDeAval[i].setImageResource(R.drawable.ic_baseline_star_24);
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
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            imagemReceita.setImageBitmap(Bitmap.createScaledBitmap(result, 300, 150, false));
        }
    }
}