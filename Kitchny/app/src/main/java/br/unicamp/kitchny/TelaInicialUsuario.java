package br.unicamp.kitchny;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import br.unicamp.kitchny.kotlin.Usuario;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

// Janela de dados do usuário
public class TelaInicialUsuario extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView tvNomeUsuario;
    private  TextView tvEmailUsuario;
    private TextView tvAprovacao;
    private TextView tvReprovacao;
    private TextView tvReceitas;
    private TextView tvMedia;
    private BottomNavigationView menu;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial_usuario);

        menu = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        tvNomeUsuario = findViewById(R.id.tvNomeUsuario);
        tvEmailUsuario = findViewById(R.id.tvEmailUsuario);
        tvAprovacao = findViewById(R.id.tvAprovacao);
        tvReprovacao = findViewById(R.id.tvReprovacao);
        tvMedia = findViewById(R.id.tvMedia);
        tvReceitas = findViewById(R.id.tvReceitas);
        menu.setOnNavigationItemSelectedListener(this);
        session = new Session(TelaInicialUsuario.this);

        GetUsuario(session.getEmail());

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

    private void GetUsuario (String email)
    {
        Call<Usuario> call = new RetrofitConfig().getService().getUsuario(email);
        call.enqueue(new Callback<Usuario>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Response<Usuario> response, Retrofit retrofit) {
                if (response.isSuccess())
                {
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
                Toast.makeText(TelaInicialUsuario.this, "Falha na obtenção de dados do usuário", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.page_1: {
                session.setTela(1);
                Intent intent = new Intent(TelaInicialUsuario.this, TelaInicial.class);
                startActivity(intent);
                break;
            }
            case R.id.page_2: {
                session.setTela(2);
                Intent intent = new Intent(TelaInicialUsuario.this, TelaInicialUsuario.class);
                startActivity(intent);
                break;
            }
        }

        return true;
    }
}