package com.sender.fakedouyu.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sender.fakedouyu.R;
import com.sender.fakedouyu.activity.PlayActivity;
import com.sender.fakedouyu.bean.RoomInfo;
import com.sender.fakedouyu.fragment.dummy.DummyContent.DummyItem;
import com.sender.fakedouyu.listener.RequestStreamUrlListener;
import com.sender.fakedouyu.listener.RequestSubChannelListener;
import com.sender.fakedouyu.model.NetworkRequestImpl;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * TODO: Replace the implementation with code for your data type.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private final static String TAG = "MyRecyclerViewAdapter";
    private final List<RoomInfo> mRoomInfos;
    private String mUrl;
    private NetworkRequestImpl request;
    private int offset = 1;
    private Context mContext;

    public MyRecyclerViewAdapter(Context context, List<RoomInfo> items, String url) {
        mRoomInfos = items;
        mUrl = url;
        mContext = context;
        request = new NetworkRequestImpl(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_liveroom, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final RoomInfo roomInfo = mRoomInfos.get(position);
        Glide.with(holder.mView.getContext()).load(roomInfo.getRoomSrc()).into(holder.roomImg);
        holder.mRoomName.setText(roomInfo.getRoomName());
        holder.nickName.setText(roomInfo.getNickname());
        holder.online.setText("观众人数：" + roomInfo.getOnline());

        if (position == mRoomInfos.size() - 10){
                String urlOffset = mUrl + "&offset=" + offset * 20;//offset是开始位置
//            Log.d(TAG, "onBindViewHolder: " + urlOffset);
                offset++;
                request.getSubChannel(urlOffset, new RequestSubChannelListener() {
                    @Override
                    public void onSuccess(List<RoomInfo> roomInfos) {
                        mRoomInfos.addAll(roomInfos);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(mContext,"无法获取数据", Toast.LENGTH_SHORT).show();
                    }
                });
        }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // TODO: 2017/4/7 项目点击事件
           request.getStreamUrl(roomInfo.getRoomId(), new RequestStreamUrlListener(){
                @Override
                public void onSuccess(int roomId, String url) {
                    Context context = holder.mView.getContext();
                    Intent intent = new Intent(context, PlayActivity.class);
                    intent.putExtra("URL", url).putExtra("ROOM_ID", roomId);
                    context.startActivity(intent);


                }

                @Override
                public void onError() {
                    Toast.makeText(mContext,"无法获取数据", Toast.LENGTH_SHORT).show();

                }
            });
//                Toast.makeText(holder.mView.getContext(), "点击了项目", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRoomInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView roomImg;
        public final TextView mRoomName;
        public final TextView online;
        public final TextView nickName;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            roomImg = (ImageView) view.findViewById(R.id.room_img);
            mRoomName = (TextView) view.findViewById(R.id.room_name);
            nickName = (TextView) view.findViewById(R.id.nick_name);
            online = (TextView) view.findViewById(R.id.online);
        }

    }

    public void resetOffset(){
        offset = 1;
    }
}
