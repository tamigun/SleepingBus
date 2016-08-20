package kr.tamiflus.sleepingbus.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.tamiflus.sleepingbus.R;

/**
 * Created by tamiflus on 16. 8. 18..
 */
public class HomeNearStationListViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.nearStationName) public TextView name;
    @BindView(R.id.nearStationUpName) public TextView upName;
    @BindView(R.id.nearStationUpDistance) public TextView upDistance;
    @BindView(R.id.nearStationDownName) public TextView downName;
    @BindView(R.id.nearStationDownDistance) public TextView downDistance;
    @BindView(R.id.expandedList) public ExpandableRelativeLayout layout;
    @BindView(R.id.expand) public ImageView btn;
    @BindView(R.id.nearStationUpView) public LinearLayout upView;
    @BindView(R.id.nearStationDownView) public LinearLayout downView;

    public HomeNearStationListViewHolder (View v) {
        super(v);
        ButterKnife.bind(this, v);
    }
}
