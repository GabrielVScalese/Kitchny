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

    JacksonImmutable.Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial_usuario);

        getSupportActionBar().hide();
        /*tvNomeUsuario = findViewById(R.id);
        tvEmailUsuario = findViewById(R.id.tvEmailUsuario);
        tvAprovacao = findViewById(R.id.tvAprovacao);
        tvReprovacao = findViewById(R.id.tvReprovacao);
        tvMedia = findViewById(R.id.tvMedia);
        tvReceitas = findViewById(R.id.tvReceitas);*/
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
                    Intent intent = new Intent (TelaInicialUsuario.this, TelaInicial.class);
                    startActivity(intent);
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