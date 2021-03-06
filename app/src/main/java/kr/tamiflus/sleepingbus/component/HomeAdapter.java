package kr.tamiflus.sleepingbus.component;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.tamiflus.sleepingbus.BusStationInfoActivity;
import kr.tamiflus.sleepingbus.FinalActivity;
import kr.tamiflus.sleepingbus.HomeActivity;
import kr.tamiflus.sleepingbus.R;
import kr.tamiflus.sleepingbus.SearchBusStationByLocationActivity;
import kr.tamiflus.sleepingbus.holders.HomeBookMarkViewHolder;
import kr.tamiflus.sleepingbus.holders.HomeNearStationListViewHolder;
import kr.tamiflus.sleepingbus.holders.HomeNearStationViewHolder;
import kr.tamiflus.sleepingbus.structs.ArrivingBus;
import kr.tamiflus.sleepingbus.structs.BookMark;
import kr.tamiflus.sleepingbus.structs.HomeObject;
import kr.tamiflus.sleepingbus.structs.NearStation;
import kr.tamiflus.sleepingbus.structs.NearTwoStation;
import kr.tamiflus.sleepingbus.utils.BusStationToStrArray;
import kr.tamiflus.sleepingbus.utils.ColorMap;

/**
 * Created by tamiflus on 16. 8. 17..
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    LayoutInflater inflater;
    ArrayList<HomeObject> list = new ArrayList<>();
    private static final int NEAR_STATION_TYPE = 1;
    private static final int NEAR_STATION_DOUBLED_TYPE = 2;
    private static final int BOOKMARK_TYPE = 3;

    public HomeObject getItemByPosition(int position) { return list.get(position); }
    public void setItemByPosition(int position, HomeObject object) { list.set(position, object); }
    public ArrayList<HomeObject> getList() {return list; }

    public HomeAdapter(Context context) {
        //super(context, R.layout.busstation_route_listview);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == NEAR_STATION_TYPE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_home_nearstation, parent, false);
            return new HomeNearStationViewHolder(v);
        }
        else if(viewType == NEAR_STATION_DOUBLED_TYPE){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_home_nearstation_two, parent, false);
            return new HomeNearStationListViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_home_bookmark, parent, false);
            return new HomeBookMarkViewHolder(v);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        //TODO: 여기에서 OnClickListener 등을 구현할 것
        if(vh instanceof HomeNearStationViewHolder) {
            HomeNearStationViewHolder holder = (HomeNearStationViewHolder) vh;
            final NearStation st = (NearStation) list.get(position);
            holder.nearStationNameView.setText(st.getStation().getName());
            holder.nearStationDistanceView.setText(Integer.toString(st.distance) + "m");
            holder.nearStationNameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("HomeAdapter", "onClick()");
                    Log.d("HomeAdapter", "toString() : " + st.getStation().toString());
                    String name = st.getStation().getName();
                    if((HomeActivity.STRING_FAILED_NETWORK.equals(name) || HomeActivity.STRING_LOADING_LOCATION.equals(name)) || HomeActivity.STRING_FAILED_LOCATION.equals(name)) {
                        return;
                    } else {
                        Intent intent = new Intent(context, BusStationInfoActivity.class);
                        intent.putExtra("departStation", BusStationToStrArray.listToArr(st.getStation()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        } else if(vh instanceof HomeNearStationListViewHolder) {
            final HomeNearStationListViewHolder holder = (HomeNearStationListViewHolder) vh;
            final NearTwoStation st = (NearTwoStation) list.get(position);
            //TODO: holder.cardView에 OnClickLister 달 것

            holder.name.setText(st.name);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SearchBusStationByLocationActivity.class);

                    String[] arr1 = BusStationToStrArray.listToArr(st.getS1());
                    String[] arr2 = BusStationToStrArray.listToArr(st.getS2());

                    intent.putExtra("st1", arr1);
                    intent.putExtra("st2", arr2);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

        } else {
            HomeBookMarkViewHolder holder = (HomeBookMarkViewHolder) vh;
            BookMark mark = (BookMark) list.get(position);

            holder.name.setText(mark.name);
            holder.routeName.setTextColor(context.getResources().getColor(ColorMap.byID.get(mark.arrivingBus.getRouteTypeCd())));
            holder.routeName.setText(mark.arrivingBus.getRouteName());
            holder.course.setText(mark.startSt.getName() + " > " + mark.endSt.getName());
            holder.leftSeat.setText(String.format("(남은 정류장 : %s)", mark.arrivingBus.getNumOfStationsToWait()));
            holder.leftTime.setText(Integer.toString(mark.arrivingBus.getTimeToWait()) + "분 전");
            holder.cardView.setOnClickListener((v) -> {
                Log.d("clicked", "clicked");
                Intent intent = new Intent(context, FinalActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("departStation", BusStationToStrArray.listToArr(mark.startSt));
                intent.putExtra("destStation", BusStationToStrArray.listToArr(mark.endSt));
                intent.putExtra("arrivingBus", ArrivingBus.ArrivingBusToArray(mark.arrivingBus));
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return this.list.get(position).type;
    }


    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public void addAll(List<HomeObject> list) {
        this.list.addAll(list);
    }

    public void add(HomeObject obj) {
        this.list.add(obj);
    }

    public void clear() {
        this.list.clear();
    }

    public void removeByPosition(int position) { this.list.remove(position); }

}
