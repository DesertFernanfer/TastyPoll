package com.fernando.tastypoll.dialog;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.fernando.tastypoll.R;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class DialogCompartirEncuesta extends DialogFragment {
    private String url;

    public DialogCompartirEncuesta(String url) {
        this.url = url;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_compartir_encuesta, container, false);

        ImageView qrImageView = view.findViewById(R.id.qrImage);
        Button copyButton = view.findViewById(R.id.copyButton);

        try {
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.encodeBitmap(url, BarcodeFormat.QR_CODE, 500, 500);
            qrImageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        copyButton.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("URL", url);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(requireContext(), getString(R.string.url_copied), Toast.LENGTH_SHORT).show();
        });
        view.findViewById(R.id.botonAtras).setOnClickListener(v -> dismiss());


        return view;
    }
}
