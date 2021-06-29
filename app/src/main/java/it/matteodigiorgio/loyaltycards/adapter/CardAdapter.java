package it.matteodigiorgio.loyaltycards.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import it.matteodigiorgio.loyaltycards.R;
import it.matteodigiorgio.loyaltycards.model.LoyaltyCardsDatabase;
import it.matteodigiorgio.loyaltycards.model.entity.LoyaltyCard;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.Viewholder> {
    private final Context context;
    private LoyaltyCardsDatabase cardDataSource;
    private LoyaltyCard[] cards;
    private ClickListener<Integer, String> cardClickListener;

    public CardAdapter(Context context, LoyaltyCardsDatabase cardArrayList) {
        this.context = context;
        this.cardDataSource = cardArrayList;
        this.cards = this.cardDataSource.cardDAO().getCards();
    }

    public void updateDataSource(LoyaltyCardsDatabase dataSource) {
        this.cardDataSource = dataSource;
        this.cards = this.cardDataSource.cardDAO().getCards();
        super.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        LoyaltyCard model = this.cards[position];

        holder.cardTitle.setText(model.Cname);

        if(model.Icard != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(model.Icard, 0, model.Icard.length);
            holder.cardImage.setImageBitmap(bmp);
        }
        holder.Card.setCardBackgroundColor(model.Ccolor);
        holder.Card.setOnClickListener(v -> cardClickListener.onItemClick(model.cardID, null));
    }

    public void setOnItemClickListener(ClickListener<Integer, String> cardClickListener) {
        this.cardClickListener = cardClickListener;
    }

    @Override
    public int getItemCount() {
        return this.cards.length;
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        private final CardView Card;
        private final ImageView cardImage;
        private final TextView cardTitle;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            Card = itemView.findViewById(R.id.CardList);
            cardImage = itemView.findViewById(R.id.imageCard);
            cardTitle = itemView.findViewById(R.id.txtNameCard);

        }
    }
}