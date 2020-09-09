package br.unicamp.kitchny;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.strictmode.CleartextNetworkViolation;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                    MyTask task = new MyTask();
                    task.execute("http://192.168.0.28:3000/api/insertUsuario");
                }
                else
                    Toast.makeText(TelaDeCriarConta.this, "Senhas não são iguais", Toast.LENGTH_LONG).show();
            }
        });
    }

    private class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            ClienteWS.postObjeto(usuario, Integer.class, params[0]);
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }

        @Override
        protected void onPostExecute(String s) {
            finish();
        }
    }
}