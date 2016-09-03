package android.com.wowsfeat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends Activity {

    private WebView mWebView;
    private RelativeLayout search_ll;
    private EditText name_et;
    private String name;
    private Spinner zone_selector;
    private ProgressBar progressBar;
    private ImageView btn_select_name;
    private String zone = "south";
    private Button confirm;
    private List<Player> playerList = new ArrayList<>();
    private SpinnerAdapter spinnerAdapter;
    private final String baseUrl = "http://rank.kongzhong.com/wows/index.html?";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1&&resultCode==1){
            Bundle bundle = data.getExtras();
            String name  ="";
            if(bundle!=null)
                name = bundle.getString("name");
            if(!name.equals("")){
                name_et.setText(name);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView) findViewById(R.id.mWebView);
        search_ll = (RelativeLayout) findViewById(R.id.search_ll);
        btn_select_name = (ImageView) findViewById(R.id.btn_select_name);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        name_et = (EditText) findViewById(R.id.name);
        zone_selector = (Spinner) findViewById(R.id.zone);
        confirm = (Button) findViewById(R.id.btn_confirm);

        init();
    }

    private void init() {
        playerList.clear();
        playerList.addAll(getPlayerListFromCache());
        BubbleSortByCount();

        List<String> zoneList = new ArrayList<>();
        zoneList.add("南区");
        zoneList.add("北区");
        spinnerAdapter = new SpinnerAdapter(zoneList, this);
        zone_selector.setAdapter(spinnerAdapter);
        zone_selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setZone(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_select_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,SelectNameActivity.class);
                startActivityForResult(intent,1);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                name = name_et.getText().toString();

                if(name.equals("")) {
                    Toast.makeText(getApplicationContext(),"请输入昵称!",Toast.LENGTH_SHORT).show();
                    return;
                }


                boolean flag = true;
                for (Player player : playerList) {
                    if (Objects.equals(player.getName(), name)) {
                        player.setCount(player.getCount() + 1);
                        flag = false;
                    }
                }

                if (flag) {
                    Player player = new Player();
                    player.setName(name);
                    player.setCount(1);
                    playerList.add(player);
                }

                BubbleSortByCount();
                WritePlayerListToCache(playerList);

                name = URLEncoder.encode(name);
                String url = baseUrl + "name=" + name + "&zone=" + zone;
                showWebView(url);
            }
        });


    }

    private void showWebView(String url) {
        mWebView.loadUrl("about:blank");//实现clearView的功能
        mWebView.clearHistory();

        search_ll.setVisibility(View.GONE);
        mWebView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        //设置加载监听器,用进度条显示加载进度
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //当加载到100%的时候 进度条自动消失
                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    //否则显示加载进度
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });

        WebSettings ws = mWebView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setUseWideViewPort(true);
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        int Scale = 1;
        mWebView.setInitialScale(Scale);
        mWebView.loadUrl(url);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//            if (mWebView.canGoBack()) {
//                mWebView.goBack();
//                return true;
//            } else
            if (search_ll.getVisibility() == View.GONE) {
                mWebView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                search_ll.setVisibility(View.VISIBLE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setZone(int position) {
        switch (position) {
            case 0:
                zone = "south";
                break;
            case 1:
                zone = "north";
                break;
        }

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
}
