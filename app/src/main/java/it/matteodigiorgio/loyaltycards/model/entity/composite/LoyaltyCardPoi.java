package it.matteodigiorgio.loyaltycards.model.entity.composite;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;
import it.matteodigiorgio.loyaltycards.model.entity.LoyaltyCard;

public class LoyaltyCardPoi {
    @Embedded
    public LoyaltyCard card;

    @Relation(
            parentColumn = "cardID",
            entityColumn = "cardID"
    )
    public List<LoyaltyCard> listCard;
}
