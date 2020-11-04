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

import br.unicamp.kitchny.kotlin.Compra;

public class CompraAdapter extends ArrayAdapter<Compra> {

    private Context context;
    private int layoutResourceId;
    private List<Compra> dados;

    public CompraAdapter(@NonNull Context context, int resource, @NonNull List<Compra> dados) {
        super(context, resource, dados);

        this.context = context;
        this.layoutResourceId = resource;
        this.dados = dados;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;

        if(view == null){
            LayoutInflater layoutinflater = LayoutInflater.from(context);
            view = layoutinflater.inflate(layoutResourceId, parent, false);
        }

        TextView ingrediente = view.findViewById(R.id.txtIngrediente);
        TextView qtd = view.findViewById(R.id.txtQtd);

        Compra compra = dados.get(position);
        ingrediente.setText("" + compra.getNomeIngrediente());
        qtd.setText("x " + compra.getQuantidade());


        return view;
    }
}
