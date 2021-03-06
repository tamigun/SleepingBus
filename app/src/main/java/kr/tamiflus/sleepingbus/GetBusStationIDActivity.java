package kr.tamiflus.sleepingbus;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import kr.tamiflus.sleepingbus.component.BusStationAdapter;
import kr.tamiflus.sleepingbus.views.ClearEditText;
import kr.tamiflus.sleepingbus.utils.BusStationDBHelper;

public class GetBusStationIDActivity extends AppCompatActivity {

    ClearEditText stationSearchBar;
    RecyclerView listView;
    BusStationAdapter adapter;
    BusStationDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_bus_station_id);

        stationSearchBar = (ClearEditText) findViewById(R.id.busStationSearchEditText);
        listView = (RecyclerView) findViewById(R.id.busStationListView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        llm.scrollToPosition(0);
        listView.setLayoutManager(llm);


        stationSearchBar.setHintTextColor(Color.parseColor("#aaaaaa"));
        adapter = new BusStationAdapter(getApplicationContext());
        listView.setAdapter(adapter);

        db = new BusStationDBHelper(getApplicationContext());

        stationSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String value = charSequence.toString();
                Log.i("Change", value);
                if(value.length() > 0 ) {
                    adapter.clear();
                    adapter.addAll(db.getStation(value));
                    adapter.notifyDataSetChanged();
                }
                //TODO : length가 0일 때 즐겨찾기 보여주기.
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        /*((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchBusStationByLocationActivity.class);
                startActivity(intent);
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_get_bus_station_id, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
