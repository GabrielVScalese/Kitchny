package br.unicamp.kitchny;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.w3c.dom.Text;

import java.util.Objects;

// Dialog utilizado para pop-up na lista de compras
public class Dialog extends DialogFragment {

    private EditText editText;
    private TextView textView;

    public Dialog()
    { }

    public static Dialog newInstance(String title) {
        Dialog frag = new Dialog();
        Bundle args = new Bundle();
        args.putString(title, title.trim());
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_dialog, container);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            editText = (EditText) view.findViewById(R.id.txtQuantidade);
            textView = view.findViewById(R.id.txtTitulo);

            assert getArguments() != null;
            String title = getArguments().getString("titulo");
            textView.setText(title.substring(0,1).toUpperCase() + title.substring(1, title.length()));
            Objects.requireNonNull(getDialog()).setTitle(title);


            editText.requestFocus();
            Objects.requireNonNull(getDialog().getWindow()).setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}
