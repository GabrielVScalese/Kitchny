package br.unicamp.kitchny;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.strictmode.CleartextNetworkViolation;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import br.unicamp.kitchny.kotlin.*;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import java.util.UUID;

public class TelaDeCriarConta extends AppCompatActivity {

    EditText etNome;
    EditText etEmail;
    EditText etSenha;
    EditText etConfirmarSenha;
    Button btnCriar;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_de_criar_conta);
        getSupportActionBar().hide();

        etNome = findViewById(R.id.etNome);
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        etConfirmarSenha = findViewById(R.id.etConfirmarSenha);
        btnCriar = findViewById(R.id.btnCriarConta);

        btnCriar.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View view) {
                if (etSenha.getText().toString().equals(etConfirmarSenha.getText().toString()))
                {
                    usuario = new Usuario(etEmail.getText().toString(), etNome.getText().toString(), etSenha.getText().toString());
                    PostUsuario(usuario);
                }
                else
                    Toast.makeText(TelaDeCriarConta.this, "Senhas não são iguais", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void PostUsuario (Usuario usuario)
    {
        Call<Status> call = new RetrofitConfig().getService().insertUsuario(usuario);
        call.enqueue(new Callback<Status>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Response<Status> response, Retrofit retrofit) {
                if (response.isSuccess())
                {
                    Intent intent = new Intent (TelaDeCriarConta.this, TelaInicial.class);
                    startActivity(intent);
                }
                else
                {
                    try
                    {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(TelaDeCriarConta.this, jObjError.getString("status"), Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e)
                    { }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(TelaDeCriarConta.this, "Falha obtenção de dados do usuários", Toast.LENGTH_SHORT).show();
            }
        });
    }
}