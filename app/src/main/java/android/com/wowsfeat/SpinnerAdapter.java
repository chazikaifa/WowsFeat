package android.com.wowsfeat;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 595056078 on 2016/8/1.
 */
public class SpinnerAdapter extends BaseAdapter {

    private List<String> zoneList;
    private MainActivity mainActivity;

    SpinnerAdapter(List<String> zoneList,MainActivity activity) {
        this.zoneList = zoneList;
        mainActivity = activity;
    }

    @Override
    public int getCount() {
        return zoneList.size();
    }

    @Override
    public String getItem(int position) {
        return zoneList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mainActivity,R.layout.item_spinner, null);
        }

        TextView zone_name = (TextView) convertView.findViewById(R.id.zone_name);
        zone_name.setText(getItem(position));

        return convertView;
    }
}
