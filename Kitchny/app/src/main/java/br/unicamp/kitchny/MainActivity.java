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
import br.unicamp.kitchny.kotlin.*;

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
                MyTask task = new MyTask();
                task.execute("http://192.168.0.28:3000/api/autenticateUsuario");
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

    private class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            response = (boolean) ClienteWS.postObjeto(usuario, boolean.class, params[0]);
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }

        @SuppressLint("ShowToast")
        @Override
        protected void onPostExecute(String s) {
            if (response)
            {
                Intent intent = new Intent (MainActivity.this, TelaInicialUsuario.class);
                startActivity(intent);
            }
            else
                Toast.makeText(MainActivity.this, "Email ou senha incorretos!", Toast.LENGTH_LONG).show();
        }
    }
}