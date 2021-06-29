package it.matteodigiorgio.loyaltycards;

import it.matteodigiorgio.loyaltycards.adapter.CardAdapter;
import it.matteodigiorgio.loyaltycards.adapter.ClickListener;
import it.matteodigiorgio.loyaltycards.model.LoyaltyCardsDatabase;
import it.matteodigiorgio.loyaltycards.model.entity.LoyaltyCard;


import android.content.Intent;
import android.widget.SearchView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;


public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private LoyaltyCardsDatabase dataSource;
    CardAdapter cardAdapter;
    private RecyclerView listCards;
    int val;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listCards = (RecyclerView) findViewById(R.id.recyclerCards);


        //Collego con DATABASE
        dataSource = Room.databaseBuilder(getApplicationContext(), LoyaltyCardsDatabase.class, "loyalty1-cards-database").allowMainThreadQueries().build();
        cardAdapter = new CardAdapter(this, dataSource);

        cardAdapter.setOnItemClickListener(new ClickListener<Integer, String>() {
            @Override
            public void onItemClick(Integer data, String dataType) {
                Intent intent = new Intent(getApplicationContext(), DetailCards.class);
                intent.putExtra("CardIndex", data);

                startActivityForResult(intent, 1);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listCards = findViewById(R.id.recyclerCards);
        listCards.setLayoutManager(linearLayoutManager);
        listCards.setAdapter(cardAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activitymain, menu);

        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_search:
                return true;

            case R.id.action_map:
                Intent goActivity3 = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(goActivity3);
                return true;

            case R.id.action_add:

                Intent goActivity = new Intent(MainActivity.this, AddCards.class);
                startActivity(goActivity);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            this.dataSource = Room.databaseBuilder(getApplicationContext(), LoyaltyCardsDatabase.class, "loyalty-cards-database").allowMainThreadQueries().build();
            this.cardAdapter.updateDataSource(this.dataSource);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        String upperText =newText.toUpperCase();
        LoyaltyCardsDatabase newdataSource;
        newdataSource = Room.databaseBuilder(getApplicationContext(), LoyaltyCardsDatabase.class, "Filtered-cards-database").allowMainThreadQueries().build();

        for(LoyaltyCard name : this.dataSource.cardDAO().getCards())
        {
            if(name.Cname.contains(upperText))
            {
                newdataSource.cardDAO().insertCard(name);
            }
        }
        this.cardAdapter.updateDataSource(newdataSource);
        newdataSource.clearAllTables();
        return true;
    }
}