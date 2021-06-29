package it.matteodigiorgio.loyaltycards.model.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import it.matteodigiorgio.loyaltycards.model.entity.LoyaltyCard;
import it.matteodigiorgio.loyaltycards.model.entity.composite.LoyaltyCardPoi;

@Dao
public interface LoyaltyCardDAO {

    @Query("SELECT * FROM LoyaltyCard")
    public LoyaltyCard[] getCards();

    @Query("SELECT * FROM LoyaltyCard WHERE cardID = :cardID")
    public LoyaltyCard getAtIndex(int cardID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertCard(LoyaltyCard card);

    @Update
    public void updateCard(LoyaltyCard card);

    @Delete
    public void deleteCard(LoyaltyCard card);

    @Transaction
    @Query("SELECT * FROM LoyaltyCard WHERE cardID = :cardID")
    public LoyaltyCardPoi getCard(int cardID);
}
