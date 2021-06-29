package it.matteodigiorgio.loyaltycards.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity
public class LoyaltyCard implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int cardID;

    @ColumnInfo(name = "cardName")
    public String Cname;
    @ColumnInfo(name = "imageCard", typeAffinity = ColumnInfo.BLOB)
    public byte[] Icard;

    @ColumnInfo(name = "clientCode")
    public String Ccode;

    @ColumnInfo(name = "cardColor")
    public int Ccolor;

    @ColumnInfo(name = "cardAddress")
    public String Caddress;

    public transient POI location;

    @ColumnInfo(name = "imageQrBarCode", typeAffinity = ColumnInfo.BLOB)
    public byte[] Iqrbarcode;
}
