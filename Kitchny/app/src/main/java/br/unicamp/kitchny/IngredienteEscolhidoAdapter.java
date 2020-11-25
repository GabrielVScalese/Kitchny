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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.unicamp.kitchny.kotlin.Compra;
import br.unicamp.kitchny.kotlin.Ingrediente;

// Adapter utilizado para a lista de ingredientes de uma receita
public class IngredienteEscolhidoAdapter extends ArrayAdapter<Ingrediente> {

    private Context context;
    private int layoutResourceId;
    private List<Ingrediente> dados;
    private boolean isFirst;


    public IngredienteEscolhidoAdapter (@NonNull Context context, int resource, @NonNull List<Ingrediente> dados) {
        super(context, resource, dados);

        this.context = context;
        this.layoutResourceId = resource;
        this.dados = dados;
        this.isFirst = false;
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

        final EditText edtIngrediente = view.findViewById(R.id.edtIngredienteEscolhido);
        final EditText edtQuantidade = view.findViewById(R.id.edtQuantidadeIngrediente);

        edtQuantidade.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    Ingrediente ingrediente = new Ingrediente(edtIngrediente.getText().toString(),
                            edtQuantidade.getText().toString());
                    dados.add(ingrediente);

                    return true;
                }
                return false;
            }
        });

        Ingrediente ingrediente = dados.get(position);

        edtIngrediente.setText(ingrediente.getNomeIngrediente());
        edtQuantidade.setText(ingrediente.getQuantidade());

        return view;
    }
}
