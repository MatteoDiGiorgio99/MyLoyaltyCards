package it.matteodigiorgio.loyaltycards;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.Intent;
import android.net.Uri;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.room.Room;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;

import it.matteodigiorgio.loyaltycards.model.LoyaltyCardsDatabase;
import it.matteodigiorgio.loyaltycards.model.entity.LoyaltyCard;
import yuku.ambilwarna.AmbilWarnaDialog;

public class AddCards extends AppCompatActivity {

    int SELECT_PICTURE;
    LoyaltyCard card;
    int val;
    byte[] CardImage = null;
    byte[] QrImage = null;
    byte[] BarCodeImage = null;

    MultiFormatWriter format = new MultiFormatWriter();
    LoyaltyCardsDatabase cardDataSource;

    Button btnImage;
    ImageView ImageCard;
    ImageView ImageQrBarCode;
    Button btnColors;
    RadioButton qr;
    RadioButton barcode;
    EditText ClientCode;
    EditText NameCard;
    EditText PostalAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cards);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.cardDataSource = Room.databaseBuilder(getApplicationContext(), LoyaltyCardsDatabase.class, "loyalty1-cards-database").allowMainThreadQueries().build();

        ClientCode = (EditText) findViewById(R.id.txtClientCode);
        NameCard = (EditText) findViewById(R.id.txtNameCard);
        btnImage=(Button) findViewById(R.id.btnImageCard);
        ImageCard =(ImageView) findViewById(R.id.imageCard);
        btnColors = (Button) findViewById((R.id.btnColor));
        qr = (RadioButton) findViewById(R.id.rdbQr);
        barcode = (RadioButton) findViewById(R.id.rdbBarCode);
        ImageQrBarCode = (ImageView)findViewById(R.id.QrBarAddCard);
        PostalAddress = (EditText) findViewById(R.id.txtPostalAddress);

        btnColors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               setColorDialog();
            }
        });

        qr.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                qr.setChecked(true);
                SetRadioButtonQR();
            }
        });
        barcode.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                barcode.setChecked(true);
                SetRadioButtonBAR();
            }
        });

    }

    private void setColorDialog (){
        AmbilWarnaDialog colorpicker = new AmbilWarnaDialog(this, Color.RED, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                 val=color;
            }
        });
        colorpicker.show();
    }

    private void SetRadioButtonQR() {
        if(qr.isChecked()==true)
        {
            barcode.setChecked(false);
            try {
                getQr();
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }
    private void SetRadioButtonBAR() {
        if(barcode.isChecked()==true)
        {
            qr.setChecked(false);
            try {
                getBarCode();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_addcards, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                return true;

            case R.id.action_done:

                if(this.NameCard.getText().toString().isEmpty()==false && this.ClientCode.getText().toString().isEmpty()==false) {
                    LoyaltyCard newCard = new LoyaltyCard();

                    String upperName = this.NameCard.getText().toString().toUpperCase();
                    newCard.Cname = upperName;
                    newCard.Ccode = this.ClientCode.getText().toString();
                    newCard.Ccolor = this.val;
                    newCard.Caddress = this.PostalAddress.getText().toString();

                    newCard.Icard = this.CardImage;

                    if (qr.isChecked() == true && barcode.isChecked() == false) {
                        newCard.Iqrbarcode = this.QrImage;
                    } else {
                        newCard.Iqrbarcode = this.BarCodeImage;
                    }

                    this.cardDataSource.cardDAO().insertCard(newCard);

                    Toast.makeText(getBaseContext(), "Card has been registered.", Toast.LENGTH_LONG).show();
                    Intent goActivity = new Intent(AddCards.this, MainActivity.class);
                    startActivity(goActivity);
                    finish();
                    return true;
                }
                else {
                    AlertDialog.Builder dialog=new AlertDialog.Builder(this);
                    dialog.setMessage("Incomplete Data Entered");
                    dialog.setTitle("ATTENTION");
                    dialog.setNegativeButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                }
                            });
                    AlertDialog alertDialog=dialog.create();
                    alertDialog.show();
                }


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void getBarCode() throws WriterException{
        BitMatrix matrix = format.encode(ClientCode.getText().toString(), BarcodeFormat.CODE_128,400,170);
        BarcodeEncoder encoder = new BarcodeEncoder();
        Bitmap bitmap = encoder.createBitmap(matrix);
        ImageQrBarCode.setImageBitmap(bitmap);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        BarCodeImage = baos.toByteArray();
    }

    private void getQr() throws WriterException{
        BitMatrix matrix = format.encode(ClientCode.getText().toString(), BarcodeFormat.QR_CODE,350,300);
        BarcodeEncoder encoder = new BarcodeEncoder();
        Bitmap bitmap = encoder.createBitmap(matrix);
        ImageQrBarCode.setImageBitmap(bitmap);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        QrImage = baos.toByteArray();
    }

    public void btnImageCard(View view) {
        imageChooser();

    }

    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    ImageCard.setImageURI(selectedImageUri);
                    Bitmap bitmap = ((BitmapDrawable) ImageCard.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    CardImage= baos.toByteArray();
                }
            }
        }
    }
}