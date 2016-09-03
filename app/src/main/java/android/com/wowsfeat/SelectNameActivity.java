package android.com.wowsfeat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 595056078 on 2016/9/3.
 */

public class SelectNameActivity extends Activity implements AdapterView.OnItemClickListener{
    private ListView name_list;
    private List<Player> playerList = new ArrayList<>();
    private NameAdapter nameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_name);

        name_list = (ListView) findViewById(R.id.name_list);

        playerList.addAll(getPlayerListFromCache());

        nameAdapter = new NameAdapter(getApplicationContext(),R.layout.item_name,playerList);
        name_list.setAdapter(nameAdapter);
        name_list.setOnItemClickListener(this);
    }

    private void BubbleSortByCount() {
        List<Player> tempList = new ArrayList<>();
        tempList.addAll(playerList);

        boolean flag = true;
        while (flag) {
            flag = false;
            for (int i = 0; i < playerList.size() - 1; i++) {
                if (tempList.get(i).getCount() < tempList.get(i + 1).getCount()) {
                    Player player = tempList.remove(i);
                    tempList.add(i + 1, player);
                    flag = true;
                }
            }
        }

        playerList.clear();
        playerList.addAll(tempList);
    }

    private List<Player> getPlayerListFromCache() {
        List<Player> tempList = new ArrayList<>();

        int count = RsSharedUtil.getInt(getApplicationContext(), "size");

        if(count < 0){
            count = 0;
        }

        for (int i = 0; i < count; i++) {
            Player player = new Player();
            player.setName(RsSharedUtil.getString(getApplicationContext(), "player" + i));
            player.setCount(RsSharedUtil.getInt(getApplicationContext(), "count" + i));
            tempList.add(player);
        }

        return tempList;
    }

    private void WritePlayerListToCache(List<Player> playerList) {
        RsSharedUtil.clear(getApplicationContext());

        RsSharedUtil.putInt(getApplicationContext(), "size", playerList.size());

        for (int i = 0; i < playerList.size(); i++) {
            RsSharedUtil.putString(getApplicationContext(), "player" + i, playerList.get(i).getName());
            RsSharedUtil.putInt(getApplicationContext(), "count" + i, playerList.get(i).getCount());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("name",playerList.get(position).getName());
        setResult(1,intent);
        finish();
    }
}
