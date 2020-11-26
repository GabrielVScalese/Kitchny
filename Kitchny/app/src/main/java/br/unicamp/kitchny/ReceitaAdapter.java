package br.unicamp.kitchny;

import android.content.*;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import br.unicamp.kitchny.kotlin.Receita;

// Adapter utilizado para a lista de receitas
public class ReceitaAdapter extends ArrayAdapter<Receita> {

    private ImageView imagemReceita;
    private Context context;
    private int layoutResourceId;
    private List<Receita> dados;
    private String urlDefault;

    public ReceitaAdapter(@NonNull Context context, int resource, @NonNull List<Receita> dados) {
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

        TextView nomeReceita = view.findViewById(R.id.txtNomeReceita);
        imagemReceita = view.findViewById(R.id.ImgImagem);

        Receita receita = dados.get(position);

        if (!receita.getImagem().equals(""))
        {
            urlDefault = receita.getImagem();
            DownloadImageTask dt = new DownloadImageTask();
            dt.execute(receita.getImagem());
            nomeReceita.setText(receita.getNome());
        }
        else
        {
            DownloadImageTask dt = new DownloadImageTask();
            dt.execute(urlDefault);
            nomeReceita.setText(receita.getNome());
        }

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
    {
        protected Bitmap doInBackground(String... urls)
        {
          if (urls[0].contains("content"))
            {
                try
                {
                    InputStream in = new java.net.URL("https://www.safraes.com.br/arquivos/noticias/2961_cafeicultores_pomeranos._na_casa_do_elmario_seidler_na_zona_rural_de_itarana_nos_deparamos_com_essa_placa_cuja_frase_em_portugues_quer_dizer_sempre_feliz_nunca_triste._foto_de_leandro_fidelis.jpg").openStream();
                    Bitmap mIcon11 = BitmapFactory.decodeStream(in);
                    return mIcon11;
                }
                catch (Exception e)
                {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
               /* try {
                    Uri uriImg = Uri.parse(urls[0]);
                    context.getContentResolver().takePersistableUriPermission( uriImg, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    context.getContentResolver().takePersistableUriPermission( uriImg, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap( context.getContentResolver(), Uri.parse(urls[0]));
                    return bitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //imgUpload.setImageBitmap(bitmap);*/
            }

            String urlDisplay = urls[0];
            Bitmap mIcon11 = null;
            try
            {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            }
            catch (Exception e)
            {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            imagemReceita.setImageBitmap(Bitmap.createScaledBitmap(result, 300, 150, false));
        }
    }
}
