package br.unicamp.kitchny;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;

import br.unicamp.kitchny.kotlin.Usuario;

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
        tvNomeUsuario = findViewById(R.id.tvNomeUsuario);
        tvEmailUsuario = findViewById(R.id.tvEmailUsuario);
        tvAprovacao = findViewById(R.id.tvAprovacao);
        tvReprovacao = findViewById(R.id.tvReprovacao);
        tvMedia = findViewById(R.id.tvMedia);
        tvReceitas = findViewById(R.id.tvReceitas);

        MyTask task = new MyTask();
        task.execute("http://192.168.0.28:3000/api/usuario");
    }

    private class MyTask extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                usuario = (JacksonImmutable.Usuario) ClienteWS.getObjeto(JacksonImmutable.Usuario.class, strings[0], "gabriel.scalese@hotmail.com");
            }
            catch (Exception error)
            {
                error.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String s) {

            tvNomeUsuario.setText(usuario.getNome());
            tvEmailUsuario.setText(usuario.getEmail());
            tvAprovacao.setText("Você aprovou " + usuario.getQtdReceitasAprovadas() + " receitas");
            tvReprovacao.setText("Você reprovou " + usuario.getQtdReceitasReprovadas() + " receitas");
            tvMedia.setText("A nota média de suas receitas publicadas é " + usuario.getNotaMediaReceitas());
            tvReceitas.setText("Você publicou " + usuario.getQtdReceitasPublicadas() + " receitas");

            super.onPostExecute(s);
        }
    }
}