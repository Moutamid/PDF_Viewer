package com.moutamid.pdfviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.moutamid.pdfviewer.databinding.ActivityPdfBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class PdfActivity extends AppCompatActivity {
    ActivityPdfBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        String path = Stash.getString(Constants.PATH, "");

        if (!path.isEmpty()){
            loadPdf(path);
        }

    }

    private static final String TAG = "PdfActivity";
    private void loadPdf(String path) {
        Log.d(TAG, "loadPdf: " + path);
//        Uri uri = Uri.parse(path);
        try {
//            ContentResolver contentResolver = getContentResolver();
//            InputStream inputStream = contentResolver.openInputStream(uri);
            binding.pdf.fromFile(new File(path))
                    .defaultPage(0)
                    .onPageChange((page, pageCount) -> {
                    })
                    .enableAnnotationRendering(true)
                    .onError(t -> {
                        Stash.clear(Constants.PATH);
                        t.printStackTrace();
                        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    })
                    .scrollHandle(new DefaultScrollHandle(this))
                    .spacing(10)
                    .load();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}