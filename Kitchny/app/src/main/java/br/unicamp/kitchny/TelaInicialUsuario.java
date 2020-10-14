package br.unicamp.kitchny;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import br.unicamp.kitchny.kotlin.Usuario;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TelaInicialUsuario extends AppCompatActivity {

    TextView tvNomeUsuario;
    TextView tvEmailUsuario;
    TextView tvAprovacao;
    TextView tvReprovacao;
    TextView tvReceitas;
    TextView tvMedia;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial_usuario);



        tvNomeUsuario = findViewById(R.id.tvNomeUsuario);
        tvEmailUsuario = findViewById(R.id.tvEmailUsuario);
        tvAprovacao = findViewById(R.id.tvAprovacao);
        tvReprovacao = findViewById(R.id.tvReprovacao);
        tvMedia = findViewById(R.id.tvMedia);
        tvReceitas = findViewById(R.id.tvReceitas);
        GetUsuario("gabriel.scalese@hotmail.com");
    }

    private void GetUsuario (String email)
    {
        Call<Usuario> call = new RetrofitConfig().getService().getUsuario(email);
        call.enqueue(new Callback<Usuario>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Response<Usuario> response, Retrofit retrofit) {
                if (response.isSuccess())
                {
                    System.out.println("a");
                    Usuario usuario = response.body();
                    tvNomeUsuario.setText(usuario.getNome());
                    tvEmailUsuario.setText(usuario.getEmail());
                    tvAprovacao.setText("Você aprovou " + usuario.getQtdReceitasAprovadas() + " receitas!");
                    tvReprovacao.setText("Você reprovou " + usuario.getQtdReceitasReprovadas() + " receitas!");
                    tvMedia.setText("Você publicou " + usuario.getNotaMediaReceitas() + " receitas!");
                    tvReceitas.setText("A nota média de suas receitas é " + usuario.getQtdReceitasPublicadas());
                }
                else
                {
                    try
                    {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(TelaInicialUsuario.this, jObjError.getString("status"), Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e)
                    { }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(TelaInicialUsuario.this, "Falha obtenção de dados do usuários", Toast.LENGTH_SHORT).show();
            }
        });
    }
}