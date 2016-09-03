package android.com.wowsfeat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 595056078 on 2016/8/29.
 */

public class NameAdapter extends ArrayAdapter<Player> {

    int resourceId;
    Context mContext;
    List<Player> playerList;


    public NameAdapter(Context context, int resource, List<Player> objects) {
        super(context, resource, objects);
        mContext = context;
        resourceId = resource;
        playerList = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.container = (RelativeLayout) view.findViewById(R.id.container);
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.btn_delete = (ImageView) view.findViewById(R.id.btn_delete);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerList.remove(position);
                WritePlayerListToCache(playerList);
                notifyDataSetChanged();
            }
        });

        viewHolder.name.setText(getItem(position).getName()+"");

        return view;
    }

    class ViewHolder {
        TextView name;
        ImageView btn_delete;
        RelativeLayout container;
    }

    private List<Player> getPlayerListFromCache() {
        List<Player> tempList = new ArrayList<>();

        int count = RsSharedUtil.getInt(mContext, "size");

        if(count < 0){
            count = 0;
        }

        for (int i = 0; i < count; i++) {
            Player player = new Player();
            player.setName(RsSharedUtil.getString(mContext, "player" + i));
            player.setCount(RsSharedUtil.getInt(mContext, "count" + i));
            tempList.add(player);
        }

        return tempList;
    }

    private void WritePlayerListToCache(List<Player> playerList) {
        RsSharedUtil.clear(mContext);

        RsSharedUtil.putInt(mContext, "size", playerList.size());

        for (int i = 0; i < playerList.size(); i++) {
            RsSharedUtil.putString(mContext, "player" + i, playerList.get(i).getName());
            RsSharedUtil.putInt(mContext, "count" + i, playerList.get(i).getCount());
        }
    }
}
