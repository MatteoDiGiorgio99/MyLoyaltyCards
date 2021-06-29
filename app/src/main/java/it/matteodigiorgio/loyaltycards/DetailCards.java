package it.matteodigiorgio.loyaltycards;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;

import it.matteodigiorgio.loyaltycards.model.LoyaltyCardsDatabase;
import it.matteodigiorgio.loyaltycards.model.entity.LoyaltyCard;

public class DetailCards extends AppCompatActivity {

    int kIndexCard;
    LoyaltyCard Card;
    int val;
    LoyaltyCardsDatabase cardDataSource;
    MultiFormatWriter format = new MultiFormatWriter();
    EditText ClientCodeDetail;
    EditText NameCardDetail;
    EditText AddressCardDetail;
    RadioButton qr;
    RadioButton barcode;
    ImageView ImageQrBarCode;
    byte[] mURLImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_cards);

      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        this.kIndexCard = (Integer) intent.getSerializableExtra("CardIndex");
        this.cardDataSource = Room.databaseBuilder(getApplicationContext(), LoyaltyCardsDatabase.class, "loyalty1-cards-database").allowMainThreadQueries().build();

        ClientCodeDetail = (EditText) findViewById(R.id.txtClientCodeDetail);
        NameCardDetail = (EditText) findViewById(R.id.txtNameDetail);
        AddressCardDetail=(EditText)findViewById(R.id.txtAddress);
        ImageQrBarCode=(ImageView) findViewById(R.id.imgQrBar);
        qr= (RadioButton)findViewById(R.id.rdbQrDetail);
        barcode=(RadioButton)findViewById(R.id.rdbBarCodeDetail);


        if(kIndexCard != -1) {
            this.Card = this.cardDataSource.cardDAO().getAtIndex(this.kIndexCard);

            this.NameCardDetail.setText(this.Card.Cname);
            this.ClientCodeDetail.setText(this.Card.Ccode);
            this.AddressCardDetail.setText(this.Card.Caddress);
            this.mURLImage = this.Card.Iqrbarcode;

            Bitmap bmp = BitmapFactory.decodeByteArray(this.Card.Iqrbarcode, 0, this.Card.Iqrbarcode.length);
            this.ImageQrBarCode.setImageBitmap(bmp);
        }

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
        inflater.inflate(R.menu.menu_detailcards, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_done:

                if(this.kIndexCard != -1) {

                    this.Card.Cname = this.NameCardDetail.getText().toString();
                    this.Card.Ccode = this.ClientCodeDetail.getText().toString();
                    this.Card.Caddress=this.AddressCardDetail.getText().toString();

                    if(qr.isChecked() == true && barcode.isChecked() == false)
                    {
                        this.Card.Iqrbarcode=this.mURLImage;
                    }
                    else
                    {
                        this.Card.Iqrbarcode=this.mURLImage;
                    }

                    this.cardDataSource.cardDAO().updateCard(this.Card);
                    Toast.makeText(getBaseContext(), "Card has been update.", Toast.LENGTH_LONG).show();
                }

                Intent result = new Intent();
                setResult(1, result);
                Intent goActivity = new Intent(DetailCards.this, MainActivity.class);
                startActivity(goActivity);
                finish();
                return true;

            case R.id.action_delete:

                this.cardDataSource.cardDAO().deleteCard(this.Card);

                Toast.makeText(getBaseContext(), "Card has been discarded.", Toast.LENGTH_LONG).show();

                Intent deleteResult = new Intent();
                setResult(1, deleteResult);
                Intent goActivity2 = new Intent(DetailCards.this, MainActivity.class);
                startActivity(goActivity2);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getBarCode() throws WriterException {
        BitMatrix matrix = format.encode(ClientCodeDetail.getText().toString(), BarcodeFormat.CODE_128,400,170);
        BarcodeEncoder encoder = new BarcodeEncoder();
        Bitmap bitmap = encoder.createBitmap(matrix);
        ImageQrBarCode.setImageBitmap(bitmap);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        mURLImage= baos.toByteArray();
    }

    private void getQr() throws WriterException{
        BitMatrix matrix = format.encode(ClientCodeDetail.getText().toString(), BarcodeFormat.QR_CODE,350,300);
        BarcodeEncoder encoder = new BarcodeEncoder();
        Bitmap bitmap = encoder.createBitmap(matrix);
        ImageQrBarCode.setImageBitmap(bitmap);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        mURLImage = baos.toByteArray();
    }

}
