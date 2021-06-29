package it.matteodigiorgio.loyaltycards.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import java.io.Serializable;
import it.matteodigiorgio.loyaltycards.model.dao.LoyaltyCardDAO;

import it.matteodigiorgio.loyaltycards.model.entity.LoyaltyCard;

@Database(entities = { LoyaltyCard.class}, version = 1, exportSchema = false)
public abstract class LoyaltyCardsDatabase extends RoomDatabase implements Serializable {
    public abstract LoyaltyCardDAO cardDAO();

}
