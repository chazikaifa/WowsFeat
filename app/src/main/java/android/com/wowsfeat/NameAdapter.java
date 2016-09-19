package android.com.wowsfeat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 595056078 on 2016/8/29.
 */

public class NameAdapter extends ArrayAdapter<String> {

    private int resourceId;
    private MainActivity mainActivity;
    private List<String> playerList;
    private String word = "";
//    private EditText name_et;


    public NameAdapter(MainActivity mainActivity, int resource, List<String> objects) {
        super(mainActivity, resource, objects);
        this.mainActivity = mainActivity;
        resourceId = resource;
        playerList = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(mainActivity).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.container = (RelativeLayout) view.findViewById(R.id.container);
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.btn_delete = (ImageView) view.findViewById(R.id.btn_delete);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.setName(getItem(position)+"");
            }
        });

        //删除特定名字后，写入缓存，并更新allNameList
        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.deleteName(getItem(position));
                playerList.remove(position);
                notifyDataSetChanged();
            }
        });

        if(word != "") {

            //利用在关键词两端加HTML标签实现TextView中关键词颜色不同
            Pattern pattern = Pattern.compile(word);
            Matcher matcher = pattern.matcher(getItem(position) + "");
            String coloredName = matcher.replaceAll("<font color='#3191E8'>" + word + "</font>");
            //目前使用的颜色#3191E8对应color文件里面的main_blue

            //将加上HTML标签的字符串经过HTML解析之后设置到title上
            viewHolder.name.setText(Html.fromHtml(coloredName));
        }else {
            viewHolder.name.setText(getItem(position) + "");
        }


        return view;
    }

    public void setWord(String word) {
        this.word = word;
    }

//    public EditText getName_et() {
//        return name_et;
//    }
//
//    public void setName_et(EditText name_et) {
//        this.name_et = name_et;
//    }

    class ViewHolder {
        TextView name;
        ImageView btn_delete;
        RelativeLayout container;
    }

    private List<String> getPlayerListFromCache() {
        List<String> tempList = new ArrayList<>();

        int count = RsSharedUtil.getInt(mainActivity.getApplicationContext(), "size");

        if (count < 0) {
            count = 0;
        }

        for (int i = 0; i < count; i++) {
            String player = RsSharedUtil.getString(mainActivity.getApplicationContext(), "player" + i);
            tempList.add(player);
        }

        return tempList;
    }

    private void WritePlayerListToCache(List<String> playerList) {
        RsSharedUtil.clear(mainActivity.getApplicationContext());

        RsSharedUtil.putInt(mainActivity.getApplicationContext(), "size", playerList.size());

        for (int i = 0; i < playerList.size(); i++) {
            RsSharedUtil.putString(mainActivity.getApplicationContext(), "player" + i, playerList.get(i));
        }
    }

}
