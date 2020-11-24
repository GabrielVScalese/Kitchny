package br.unicamp.kitchny;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
public class IngredienteAdapter extends ArrayAdapter<Ingrediente> {

    private Context context;
    private int layoutResourceId;
    private List<Ingrediente> dados;
    private ImageView imagem;

    public IngredienteAdapter (@NonNull Context context, int resource, @NonNull List<Ingrediente> dados) {
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

        TextView tvIngrediente = view.findViewById(R.id.tvIngrediente);
        imagem = view.findViewById(R.id.imgReceita);

        Ingrediente ingrediente = dados.get(position);

        Pattern p = Pattern.compile( "[0-9]" );
        Matcher m = p.matcher(ingrediente.getQuantidade());

        if (m.find())
            tvIngrediente.setText(ingrediente.getQuantidade() + " de " + ingrediente.getNomeIngrediente());
        else
            tvIngrediente.setText(ingrediente.getNomeIngrediente() + " " + ingrediente.getQuantidade());

        return view;
    }
}
