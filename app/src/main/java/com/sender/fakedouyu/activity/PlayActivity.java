package com.sender.fakedouyu.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kyleduo.switchbutton.SwitchButton;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.sender.fakedouyu.R;
import com.sender.fakedouyu.db.Room;
import com.sender.fakedouyu.db.Room_Table;
import com.sender.fakedouyu.utils.BarrageProcess;

import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;
import master.flame.danmaku.ui.widget.DanmakuView;

public class PlayActivity extends BaseActivity {
    private static final String TAG = "PLAY_ACTIVITY";
    private static final int CONTROL_STAY_TIME = 4000;

//    @BindView(R.id.back_button) ImageView backButton;
//    @BindView(R.id.nick_name) TextView nickName;
//    @BindView(R.id.barrage_switch) Switch barrageSwitch;
//    @BindView(R.id.favorite_flag) ImageView favotiteflag;
//    @BindView(R.id.control_view) RelativeLayout controlView;
//    @BindView(R.id.vitamio_video) VideoView videoView;

//    private TextView nickName;
    private SwitchButton barrageSwitch;
    private RelativeLayout controlView;
    private VideoView videoView;
    private ImageView favoriteFlag;
    private DanmakuView danmakuView;
    private Integer mRoomId;
    private String mUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vitamio.isInitialized(this);
        setContentView(R.layout.activity_play);
//        ButterKnife.bind(this);
        hideSystemUI();
        mUrl = getIntent().getStringExtra("URL");
        mRoomId = getIntent().getIntExtra("ROOM_ID", -1);
        initControlView();
//        nickName.setText(getIntent().getStringExtra("NICK_NAME"));
        playVideo(mUrl);
        playBarrage();
        setFavoriteFlagStatus();

    }


    private void playBarrage() {
        new BarrageProcess(this, danmakuView, mRoomId).start();
    }

    private void playVideo(String url) {
        videoView.setVideoPath(url);
        videoView.start();

    }

    private void initControlView() {
        videoView = (VideoView) findViewById(R.id.vitamio_video);
        danmakuView = (DanmakuView) findViewById(R.id.danmaku_view);
        controlView = (RelativeLayout) findViewById(R.id.control_view);
        favoriteFlag = (ImageView) findViewById(R.id.favorite_flag);

        //弹幕开关
        barrageSwitch = (SwitchButton) findViewById(R.id.barrage_switch);
        barrageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    danmakuView.setVisibility(View.VISIBLE);
                }else {
                    danmakuView.setVisibility(View.INVISIBLE);
                }
            }
        });
//        nickName = (TextView) findViewById(R.id.nick_name);



    }

    private boolean isFavorite;
    private void setFavoriteFlagStatus() {
        Room judgeRoom = new Select().from(Room.class).where(Room_Table.roomId.eq(mRoomId)).querySingle();
        if (judgeRoom != null){
            Log.d(TAG, "setFavoriteFlagStatus: " + judgeRoom.roomId + "and:" + mRoomId);
            isFavorite = true;
            favoriteFlag.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
        favoriteFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite){
                    isFavorite = false;
                    favoriteFlag.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    Room room = new Room();
                    room.roomId = mRoomId;
                    room.delete();
                }else {
                    isFavorite = true;
                    favoriteFlag.setImageResource(R.drawable.ic_favorite_black_24dp);
                    Room room = new Room();
                    room.roomId = mRoomId;
                    room.roomUrl = mUrl;
                    room.save();
                }
            }
        });
    }


    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public void onBack(View view) {
        finish();
    }

    /**
     * 屏幕点击事件
     */
    Handler mHandler = new Handler();

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            controlView.setVisibility(View.GONE);
        }
    };
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                if (controlView.getVisibility() == View.VISIBLE) {
                    controlView.setVisibility(View.GONE);
                    mHandler.removeCallbacks(mRunnable);
                } else {
                    controlView.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(mRunnable, CONTROL_STAY_TIME);
                }
        }
        return super.onTouchEvent(event);
    }
}
