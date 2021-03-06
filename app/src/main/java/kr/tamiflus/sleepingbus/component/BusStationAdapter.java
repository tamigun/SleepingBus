package kr.tamiflus.sleepingbus.component;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kr.tamiflus.sleepingbus.BusStationInfoActivity;
import kr.tamiflus.sleepingbus.R;
import kr.tamiflus.sleepingbus.SearchBusStationByLocationActivity;
import kr.tamiflus.sleepingbus.holders.BusStationViewHolder;
import kr.tamiflus.sleepingbus.holders.SectionViewHolder;
import kr.tamiflus.sleepingbus.structs.BusStation;
import kr.tamiflus.sleepingbus.structs.BusStationTag;
import kr.tamiflus.sleepingbus.utils.BusStationDBHelper;
import kr.tamiflus.sleepingbus.utils.BusStationToStrArray;

/**
 * Created by tamiflus on 2016. 7. 31..
 */
public class BusStationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context context;
    LayoutInflater inflater;
    ArrayList<BusStation> list = new ArrayList<>();
    public static int TYPE_SECTION = 1;
    public static int TYPE_ITEM = 2;

    public BusStationAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_SECTION)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_busstation_component, parent, false);
            return new SectionViewHolder(v);
        }
        else{
            View view = inflater.inflate(R.layout.busstation_listview_component, parent, false);
            return new BusStationViewHolder(view);
        }

    }

    public void addAll(List<BusStation> list) {
        this.list.addAll(list);
    }

    public void add(BusStation obj) {
        this.list.add(obj);
    }

    public void clear() {
        this.list.clear();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(vh instanceof SectionViewHolder) {
            BusStationTag tag = (BusStationTag) list.get(position);
            SectionViewHolder holder = (SectionViewHolder) vh;

            holder.sectionName.setText(tag.locate);
        }
        else{
            final BusStation st = list.get(position);
            BusStationViewHolder holder = (BusStationViewHolder) vh;

            //TODO: holder.layout에 onClickListener 달 것

            holder.nameView.setText(st.getName());
            holder.idView.setText(st.getId());
            holder.locationView.setText(st.getRegion());
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<BusStation> list = (new BusStationDBHelper(context)).getStationByStationName(st.getName());
//                    Intent intent = new Intent(context, SearchBusStationByLocationActivity.class);
                    //TODO: DB에서 전체 리스트 받아와서 이름 같은거 있으면 두개 넣고 맵으로 넘어가기
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    List<BusStation> stations = new ArrayList<>();
                    for(int i = 0; i<list.size(); i++) {
                        if(list.get(i).getName().equals(st.getName())) {
                            stations.add(list.get(i));
                        }
                    }
                    if(stations.size() == 1) {
                        // 지도 안거치고 넘어가기
                        Log.d("BusStationAdapter", "지도 안거치고 넘어가기");
                        Log.d("BusStationAdapter", "st : " + stations.get(0).toString());
                        Intent intent = new Intent(context, BusStationInfoActivity.class);
                        intent.putExtra("departStation", BusStationToStrArray.listToArr(stations.get(0)));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    } else if(stations.size() == 2) {
                        // 지도 거쳐서 넘어가기
                        Log.d("BusStationAdapter", "지도 거쳐서 넘어가기");
                        Log.d("BusStationAdapter", "st1 : " + stations.get(0).toString());
                        Log.d("BusStationAdapter", "st2 : " + stations.get(1).toString());
                        Intent intent = new Intent(context, SearchBusStationByLocationActivity.class);
                        String[] arr1 = BusStationToStrArray.listToArr(stations.get(0));
                        String[] arr2 = BusStationToStrArray.listToArr(stations.get(1));
                        intent.putExtra("st1", arr1);
                        intent.putExtra("st2", arr2);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    } else {
                        Log.d("BusStationAdapter", "unexpected sameCnt : " + stations.size());
                        Toast.makeText(context, "unexpected sameCnt", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position) instanceof BusStationTag)
            return TYPE_SECTION;
        return TYPE_ITEM;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
