package br.unicamp.kitchny;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONObject;

import br.unicamp.kitchny.kotlin.*;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    Button btnCriarConta;
    Button btnEntrar;
    EditText etEmail;
    EditText etSenha;
    Usuario usuario;
    boolean response = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        etEmail = findViewById(R.id.etEmailLogin);
        etSenha = findViewById(R.id.etSenhaLogin);
        btnCriarConta = findViewById(R.id.btnCriarContaLogin);
        btnEntrar = findViewById(R.id.btnEntrarLogin);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario = new Usuario(etEmail.getText().toString(), etSenha.getText().toString());
                AutenticarUsuario(usuario);
            }
        });
        
        btnCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,TelaDeCriarConta.class);
                startActivity(intent);
            }
        });
    }

    private void AutenticarUsuario (Usuario usuario)
    {
        Call<Status> call = new RetrofitConfig().getService().autenticarUsuario(usuario);
        call.enqueue(new Callback<Status>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Response<Status> response, Retrofit retrofit) {
                if (response.isSuccess())
                {
                    Intent intent = new Intent (MainActivity.this, TelaInicial.class);
                    startActivity(intent);
                }
                else
                {
                    try
                    {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(MainActivity.this, jObjError.getString("status"), Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e)
                    { }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(MainActivity.this, "Falha na autenticação de usuário", Toast.LENGTH_SHORT).show();
            }
        });
    }
}