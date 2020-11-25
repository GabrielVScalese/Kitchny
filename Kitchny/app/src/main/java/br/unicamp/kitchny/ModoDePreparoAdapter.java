package br.unicamp.kitchny;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.unicamp.kitchny.kotlin.Compra;
import br.unicamp.kitchny.kotlin.Ingrediente;

// Adapter utilizado para a lista de ingredientes de uma receita
public class ModoDePreparoAdapter extends ArrayAdapter<String> {

    private Context context;
    private int layoutResourceId;
    private List<String> dados;


    public ModoDePreparoAdapter (@NonNull Context context, int resource, @NonNull List<String> dados) {
        super(context, resource, dados);

        this.context = context;
        this.layoutResourceId = resource;
        this.dados = dados;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;

        if(view == null){
            LayoutInflater layoutinflater = LayoutInflater.from(context);
            view = layoutinflater.inflate(layoutResourceId, parent, false);
        }

        TextView tvNumItem = view.findViewById(R.id.tvNumItem);
        final EditText edtNumPasso = view.findViewById(R.id.edtNumPasso);

        if (dados.size() == 1) {
            tvNumItem.setText(dados.size() + "º");
            edtNumPasso.setHint("Digite o " + dados.size() + "º passo");
        }
        else
        {
            tvNumItem.setText((position) + "º");
            edtNumPasso.setHint("Digite o " + dados.size() + "º passo");
        }

        if (position == 0)
        {
            if (dados.size() == 1)
                tvNumItem.setText(dados.size() + "º");
            else
                tvNumItem.setText((dados.size()) + "º");
        }

        edtNumPasso.setText(dados.get(position));

        edtNumPasso.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    String modoDePreparo = edtNumPasso.getText().toString();
                    modoDePreparo = modoDePreparo.replace("\n", "");

                    dados.add(modoDePreparo);

                    return true;
                }
                return false;
            }
        });

        return view;
    }
}
