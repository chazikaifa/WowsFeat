package android.com.wowsfeat;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.URLEncoder;

/**
 * Created by 595056078 on 2016/10/2.
 */

public class WebViewActivity extends Activity {

    private WebView mWebView;
    private ProgressBar progressBar;
    private String name;
    private String zone = "south";
    private final String baseUrl = "http://rank.kongzhong.com/wows/index.html?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle bundle = getIntent().getExtras();

        if(bundle==null){
            Toast.makeText(getApplicationContext(),"玩家数据错误！",Toast.LENGTH_SHORT).show();
            finish();
        }else {
            name = bundle.getString("name");
            int zone_number = bundle.getInt("zone");

            //0代表南区，1代表北区，zone默认为south，当zone不为0是，设置zone为north
            if(zone_number != 0){
                zone = "north";
            }
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview);
        mWebView = (WebView) findViewById(R.id.mWebView);
        progressBar  = (ProgressBar) findViewById(R.id.progress_bar);

        name = URLEncoder.encode(name);
        String url = baseUrl + "name=" + name + "&zone=" + zone;

        mWebView.clearHistory();
        mWebView.clearView();

        WebSettings ws = mWebView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setUseWideViewPort(true);
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);

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
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
