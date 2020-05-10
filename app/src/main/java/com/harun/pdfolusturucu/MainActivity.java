package com.harun.pdfolusturucu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

// https://harun.xyz/
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button pdfOlustur = findViewById(R.id.pdfOlustur);

        pdfOlustur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // iText PDF toplu resim yollama
                ArrayList<Integer> resimId = new ArrayList<>();
                resimId.add(R.drawable.harun_xyz);
                resimId.add(R.drawable.harun_xyz_opacity_25);

                // iText PDF oluşturma
                new PDFOlusturucu().pdfOlustur(MainActivity.this,resimId);

            }
        });

    }
}
