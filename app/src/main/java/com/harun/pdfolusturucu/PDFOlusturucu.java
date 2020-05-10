package com.harun.pdfolusturucu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.RomanList;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

// https://harun.xyz/
public class PDFOlusturucu {

    // Android itextpdf ile pdf oluşturma
    public void pdfOlustur(Context context, ArrayList<Integer> resimId){

        Document pdfDosyam = new Document();

        // Her Sayfayı A4 Boyutuna getirdik
        pdfDosyam.setPageSize(PageSize.A4);

        // Oluşturma tarihini girdik
        pdfDosyam.addCreationDate();

        // Yazar ve Kurucu adını ekledik
        pdfDosyam.addAuthor("PDF Yazar Adı");
        pdfDosyam.addCreator("PDF Oluşturucu Adı");

        pdfDosyam.addTitle("PDF Başlığı");
        pdfDosyam.addSubject("PDF Açıklaması");

        try {

            // Telefon belleğinde test.pdf isimli bir pdf dosyası oluşturduk ve açtık
            PdfWriter.getInstance(pdfDosyam, new FileOutputStream(new File(Environment.getExternalStorageDirectory(),"test.pdf")));

            pdfDosyam.open();

            //******************* LOGO EKLEME *******************

            try {

                // Logoyu bitmap'e çevirip stream ile baytlara ayırıp pdfe basıyoruz

                // drawable klasörümüzde home isimli resim olduğu varsayılmıştır
                Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.home);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Image logo = Image.getInstance(byteArray);

                // Logonun konumunu sağ yapıyoruz
                logo.setAlignment(Element.ALIGN_RIGHT);

                pdfDosyam.add(logo);

            } catch (IOException e) {
                Log.e("hata","Dosya Hatası: "+e.toString());
            }


            //******************* LOGO EKLEME *******************


            // Fontu Çekip Türkçe Karakter Sorununu Atlatmak İçin Ayarladığımız Kısım
            BaseFont arial = null;

            try {

                // Burada font olarak arial kullanıyoruz assets klasörümüz altında yer aldığını varsayarak
                arial = BaseFont.createFont("assets/arial.ttf", BaseFont.IDENTITY_H,BaseFont.EMBEDDED);

            } catch (Exception e) {
                Log.e("hata","Font Çekme Hatası "+e.toString());
                e.printStackTrace();
            }

            // Fontlarımızı tanımladığımız ilk yer
            Font fontBaslik,fontIcerik;

            // Eğer kendi assets klasörümüzdeki fontumuzu çekersek bir sorun çıkmadan tamamlanıyor
            if(arial != null){

                fontBaslik = new Font(arial,30.0f);
                fontIcerik = new Font(arial,25.0f);

            }else{ // Eğer font çekilmezse bir hata verirse onun içinde önlem alıyoruz ek olarak türkçe karakter desteklemeyen ama iyi çalışan bir font ekliyoruz

                fontBaslik = new Font(Font.FontFamily.HELVETICA,30.0f, Font.NORMAL, BaseColor.BLACK);
                fontIcerik = new Font(Font.FontFamily.HELVETICA,25.0f, Font.NORMAL, BaseColor.BLACK);

            }


            // Başlığı tanımladığımız yer
            Chunk baslik_yazisi = new Chunk("BAŞLIK", fontBaslik);
            Paragraph baslik = new Paragraph(baslik_yazisi);
            baslik.setAlignment(Element.ALIGN_CENTER);
            pdfDosyam.add(baslik);

            // Separator tanımlama ve renk ekleme
            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));

            // Separator ekleme
            pdfDosyam.add(new Paragraph(""));

            // Bir boşlukluk satır bırakma
            pdfDosyam.add(new Paragraph("\n"));


            // TABLO VE HUCRE EKLEME ++++

            // Sütun sayısını girerek tablo oluşturma
            PdfPTable tablo_1 = new PdfPTable(2);

            // Satırları oluşturup içerik girme // Ayrıca fontlarıda burada belirliyoruz
            PdfPCell hucre_1 = new PdfPCell(new Paragraph(new Chunk("Hücre 1", fontIcerik)));
            PdfPCell hucre_2 = new PdfPCell(new Paragraph(new Chunk("Hücre 2", fontIcerik)));

            // Her tablo için padding ile 4dp iç boşluk veriyoruz
            hucre_1.setPadding(4);
            hucre_2.setPadding(4);

            // Sütunları Tabloya ekliyoruz
            tablo_1.addCell(hucre_1);
            tablo_1.addCell(hucre_2);

            // Dosyaya tabloyu ekleme
            pdfDosyam.add(tablo_1);

            // TABLO VE HUCRE EKLEME ----

            // İçerik eklemek
            Paragraph veriler_icerik = new Paragraph(new Chunk("Merhaba bu düz bir içeriktir.", fontIcerik));
            veriler_icerik.setAlignment(Element.ALIGN_CENTER);
            pdfDosyam.add(veriler_icerik);
            pdfDosyam.add(new Paragraph("\n"));

            // Liste ekleme
            List liste1 = new RomanList();
            liste1.add(new ListItem("Item 1"));
            liste1.add(new ListItem("Item 2"));

            pdfDosyam.add(liste1);

            List liste2 = new List();
            liste2.add(new ListItem("İtem 1"));
            liste2.add(new ListItem("İtem 2"));

            pdfDosyam.add(liste2);

            //******************* iText PDF Birden Fazla Resim Ekleme ve Küçültme *******************

            for(Integer id : resimId){

                try {

                    Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), id);

                    // Resmi bitmap'e çevirip stream ile baytlara ayırıp pdfe basıyoruz

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                    byte[] byteArray = stream.toByteArray();

                    Image image = Image.getInstance(byteArray);
                    image.setAlignment(Element.ALIGN_CENTER);
                    image.scaleToFit(500,500); // 500 X 500 olarak boyutlandırdık
                    pdfDosyam.add(image);

                } catch (IOException e) {
                    Log.e("hata","Resim Hatası: "+e.toString());
                    e.printStackTrace();
                }

            }

            //******************* iText PDF Birden Fazla Resim Ekleme ve Küçültme *******************

            // Sayfaları bölümlere ayırmak
            Chapter bolum1 = new Chapter("Bölüm", 1);
            Paragraph bolum1_icerik = new Paragraph("Bölüm 1 İçerik");
            bolum1.add(bolum1_icerik);
            pdfDosyam.add(bolum1);

            Chapter bolum2 = new Chapter("Bölüm", 2);
            bolum2.add(new Paragraph("Bölüm 2 İçerik"));
            pdfDosyam.add(bolum2);

            // Belgeyle işimiz bittiği için kapatıyoruz
            pdfDosyam.close();

            Toast.makeText(context,"İşlem Başarılı Pdf Oluştu",Toast.LENGTH_LONG).show();

        } catch (DocumentException e) {
            Toast.makeText(context,"İşlem Başarısız, Döküman Hatası!",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            Toast.makeText(context,"İşlem Başarısız, Dosya Bulunamadı Hatası! Uygulamaya gerekli izinleri veriniz!ç",Toast.LENGTH_LONG).show();
            Log.e("hata","hata: "+e.toString());
            e.printStackTrace();
        }

    }

}
