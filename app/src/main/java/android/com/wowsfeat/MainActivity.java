package android.com.wowsfeat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {

    private EditText name_et;
    private ImageView btn_select_name;
    private Spinner zone_selector;
    private Button btn_confirm;
    private ListView name_list_view;
    private ImageView logo;
    private ImageView btn_empty;
    private LinearLayout search_ll;
    private SpinnerAdapter spinnerAdapter;
    private NameAdapter nameAdapter;


    private List<String> allNameList = new ArrayList<>();   //从缓存读取的所有昵称
    private List<String> nameList = new ArrayList<>();      //呈现的昵称
    private int zone = 0;//0代表南区，1代表北区，默认为南区
    private String name;

    //当再次回到Activity时更新allNameList
    @Override
    protected void onResume() {
        super.onResume();
        refreshAllNameList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init_data();
        init_view();
    }

    private void init_data(){
        allNameList.addAll(getPlayerListFromCache());

        //当没有关键词时，显示所有缓存玩家
        nameList.addAll(allNameList);
    }

    private void init_view() {
        name_et = (EditText) findViewById(R.id.name);
        btn_select_name = (ImageView) findViewById(R.id.btn_select_name);
        zone_selector = (Spinner) findViewById(R.id.zone);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        logo = (ImageView) findViewById(R.id.logo);
        btn_empty = (ImageView) findViewById(R.id.btn_empty);
        name_list_view = (ListView) findViewById(R.id.name_list);
        search_ll = (LinearLayout) findViewById(R.id.search_ll);

        nameAdapter = new NameAdapter(this,R.layout.item_name,nameList);
        name_list_view.setAdapter(nameAdapter);

        btn_empty.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if(action == MotionEvent.ACTION_DOWN){
                    btn_empty.setImageResource(R.drawable.ic_empty_press);
                }else if(action == MotionEvent.ACTION_UP||action == MotionEvent.ACTION_CANCEL){
                    btn_empty.setImageResource(R.drawable.ic_empty);
                }

                return false;
            }
        });

        btn_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_et.setText("");
            }
        });

        name_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.i("onFocusChange","View is "+v+" and hasFocus is "+hasFocus);
            }
        });

        name_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                name = s+"";

                //与缓存昵称判断匹配的代码，若有匹配，显示nameList，否则不显示
                //若关键词为空，填入所有缓存的玩家（这样点击下拉按钮显示为所有缓存的玩家）

                //为适配器设置关键词
                nameAdapter.setWord(name);
                nameList.clear();

                if(!name.equals("")){
                    btn_empty.setVisibility(View.VISIBLE);

                    Pattern pattern = Pattern.compile(".*" + name + ".*");
                    Matcher matcher;
                    for (String nickName : allNameList) {
                        matcher = pattern.matcher(nickName);
                        if (matcher.matches()) {
                            nameList.add(nickName);
                        }
                    }

                    nameAdapter.notifyDataSetChanged();

                    //若有匹配，显示name_list,否则隐藏
                    if(nameList.size()>0){
                        name_list_view.setVisibility(View.VISIBLE);
                        btn_select_name.setImageResource(R.drawable.ic_arrow_up);
                        logo.setVisibility(View.GONE);
                    }else {
                        name_list_view.setVisibility(View.GONE);
                        btn_select_name.setImageResource(R.drawable.ic_arrow_down);
                        logo.setVisibility(View.VISIBLE);
                    }
                }else {
                    btn_empty.setVisibility(View.GONE);
                    nameList.addAll(allNameList);
                    nameAdapter.notifyDataSetChanged();
                }

            }
        });

        //若点击下拉按钮，则显示/隐藏所有缓存名称
        //且当name_list显示时，logo隐藏（空出位置）
        //后期可以考虑加入动画效果（往上移动之类的）
        btn_select_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name_list_view.getVisibility() == View.GONE){
                    name_list_view.setVisibility(View.VISIBLE);
                    btn_select_name.setImageResource(R.drawable.ic_arrow_up);
                    logo.setVisibility(View.GONE);
                }else {
                    name_list_view.setVisibility(View.GONE);
                    btn_select_name.setImageResource(R.drawable.ic_arrow_down);
                    logo.setVisibility(View.VISIBLE);
                }
            }
        });

        //当点击其他位置时，若name_list可见，则隐藏（实现点击外部隐藏）
        search_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name_list_view.getVisibility() == View.VISIBLE){
                    name_list_view.setVisibility(View.GONE);
                    btn_select_name.setImageResource(R.drawable.ic_arrow_down);
                    logo.setVisibility(View.VISIBLE);
                }
            }
        });

        List<String> zoneList = new ArrayList<>();
        zoneList.add("南区");
        zoneList.add("北区");
        spinnerAdapter = new SpinnerAdapter(zoneList,this);
        zone_selector.setAdapter(spinnerAdapter);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                for(String eachName:allNameList){
                    if(eachName.equals(name)){
                        flag = false;
                    }
                }

                if(flag){
                    allNameList.add(0,name);
                    WritePlayerListToCache(allNameList);
                }

                Toast.makeText(getApplicationContext(),"跳转Activity",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //设置昵称后，隐藏name_list,显示logo
    public void setName(String nickName){
        name_et.setText(nickName);
        name_list_view.setVisibility(View.GONE);
        btn_select_name.setImageResource(R.drawable.ic_arrow_down);
        logo.setVisibility(View.VISIBLE);
    }

    //设置zone
    public void setZone(int zone){
        this.zone = zone;
    }

    public void deleteName(String nickName){
        for(int i=0;i<allNameList.size();i++){
            if(nickName.equals(allNameList.get(i))){
                allNameList.remove(i);
                Log.i("deleteName","match!");
            }
        }
        WritePlayerListToCache(allNameList);
    }

    public void refreshAllNameList(){
        allNameList.clear();
        allNameList.addAll(getPlayerListFromCache());
    }

    private List<String> getPlayerListFromCache() {
        List<String> tempList = new ArrayList<>();

        int count = RsSharedUtil.getInt(getApplicationContext(), "size");

        if (count < 0) {
            count = 0;
        }

        for (int i = 0; i < count; i++) {
            String player = RsSharedUtil.getString(getApplicationContext(), "player" + i);
            tempList.add(player);
        }

        return tempList;
    }

    private void WritePlayerListToCache(List<String> playerList) {
        RsSharedUtil.clear(getApplicationContext());

        RsSharedUtil.putInt(getApplicationContext(), "size", playerList.size());

        for (int i = 0; i < playerList.size(); i++) {
            RsSharedUtil.putString(getApplicationContext(), "player" + i, playerList.get(i));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //当按下返回键时，如果下拉列表处于可见状态，使其隐藏，而不是退出应用
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (name_list_view.getVisibility()==View.VISIBLE) {
                name_list_view.setVisibility(View.GONE);
                btn_select_name.setImageResource(R.drawable.ic_arrow_down);
                logo.setVisibility(View.VISIBLE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
