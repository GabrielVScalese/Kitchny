package br.unicamp.kitchny;
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

import java.util.Objects;

public class Dialog extends DialogFragment {
    private EditText mEditText;

    public Dialog() {
    }

    public static Dialog newInstance(String title) {
        Dialog frag = new Dialog();
        Bundle args = new Bundle();
        args.putString("titulo", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            // Get field from view
            mEditText = (EditText) view.findViewById(R.id.txtQuantidade);
            // Fetch arguments from bundle and set title
            assert getArguments() != null;
            String title = getArguments().getString("titulo", "Enter Name");
            Objects.requireNonNull(getDialog()).setTitle(title);
            // Show soft keyboard automatically and request focus to field
            mEditText.requestFocus();
            Objects.requireNonNull(getDialog().getWindow()).setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

}
