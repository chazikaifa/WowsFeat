package android.com.wowsfeat.infoDetail;

import android.app.Activity;
import android.com.wowsfeat.R;
import android.com.wowsfeat.WebViewActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by 595056078 on 2016/9/26.
 */

public class PlayerDetailActivity extends Activity {

    private String name;
    private int zone;

    private ImageView btn_back;
    private ImageView btn_webview;

    private TextView name_tv;
    private TextView player_rank;
    private TextView person_game;
    private TextView team_game;
    private TextView competition_rank;
    private TextView PvC_game;

    PlayerData playerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_detail);

        //考虑在MainActivity先获取数据，然后再跳转
        //目前先把name和zone作为bundle传输过来
        Bundle bundle = getIntent().getExtras();

        if(bundle==null) {
            Toast.makeText(getApplicationContext(),"bundle为空！",Toast.LENGTH_SHORT).show();
        }else {
            name = bundle.getString("name");
            zone = bundle.getInt("zone");

            init_data();
            init_view();
        }
    }

    private void init_data(){
        playerData = new PlayerData();
        //这里应该从抓回来的数据中获取玩家数据的部分

        //暂时先写点测试数据
        playerData.rank = 9900;
        playerData.activeRank = 9999;

        playerData.personWinRate = 6956;
        playerData.teamWinRate = 7345;
        playerData.PvCWinRate = 10000;

        playerData.personGameCount = 2000;
        playerData.teamGameCount = 3000;
        playerData.PvCGameCount = 200;

        playerData.competitionRankCount = 200;
        playerData.competitionRank = 18;
    }

    private void init_view(){
        player_rank = (TextView) findViewById(R.id.player_rank);
        person_game = (TextView) findViewById(R.id.person_game);
        team_game = (TextView) findViewById(R.id.team_game);
        competition_rank = (TextView) findViewById(R.id.competition_rank);
        PvC_game = (TextView) findViewById(R.id.PvC_game);

        btn_back  = (ImageView) findViewById(R.id.btn_back);
        name_tv = (TextView) findViewById(R.id.name_zone);
        btn_webview = (ImageView) findViewById(R.id.btn_webview);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_webview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(PlayerDetailActivity.this, WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name",name);
                bundle.putInt("zone",zone);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        player_rank.setText(playerData.getRankString());
        person_game.setText(playerData.getPersonString());
        team_game.setText(playerData.getTeamString());
        PvC_game.setText(playerData.getPvCString());
        competition_rank.setText(playerData.getCompetitionString());

        if(zone == 0 ){
            name_tv.setText("南区-"+name);
        }else {
            name_tv.setText("北区-"+name);
        }
    }


    //装载玩家基本数据的类
    class PlayerData{
        //所有百分数应为整型，带百分号小数点两位，使用时先除以100，并再后面加%
        int rank;
        int activeRank;

        int personWinRate;
        int teamWinRate;
        int PvCWinRate;

        int personGameCount;
        int teamGameCount;
        int PvCGameCount;

        int competitionRankCount;
        int competitionRank;
//        int competitionStarCount;

        String getRankString(){
            String result;
            if(rank>10000||rank<0||activeRank>10000||activeRank<0){
                //若数据非法返回“- -”，并增加换行符以保持布局
                result =  "\n- -\n";
//                String.format("%.2f",rank/100.00);
            }else {
                result = rank/100.00+"%\n------------\n"+activeRank/100.00+"%";
            }
            return result;
        }

        String getPersonString(){
            String result;
            if(personGameCount<0||personWinRate>10000||personWinRate<0){
                //若数据非法返回“- -”，并增加换行符以保持布局
                result =  "\n- -\n";
            }else {
                result = personGameCount+"\n------------\n"+personWinRate/100.00+"%";
            }
            return result;
        }

        String getTeamString(){
            String result;
            if(teamGameCount<0||teamWinRate>10000||teamWinRate<0){
                //若数据非法返回“- -”，并增加换行符以保持布局
                result =  "\n- -\n";
            }else {
                result = teamGameCount+"\n------------\n"+teamWinRate/100.00+"%";
            }
            return result;
        }

        String getPvCString(){
            String result;
            if(PvCGameCount<0||PvCWinRate>10000||PvCWinRate<0){
                //若数据非法返回“- -”，并增加换行符以保持布局
                result =  "\n- -\n";
            }else {
                result = PvCGameCount+"\n------------\n"+PvCWinRate/100.00+"%";
            }
            return result;
        }

        String getCompetitionString(){
            String result;
            if(competitionRankCount<0||competitionRank<0){
                //若数据非法返回“- -”，并增加换行符以保持布局
                result =  "\n- -\n";
            }else {
                result = competitionRankCount+"\n------------\n"+competitionRank+"级";
            }
            return result;
        }
    }
}
