package com.sender.fakedouyu.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.sender.fakedouyu.db.Room;
import com.sender.fakedouyu.fragment.dummy.DummyContent.DummyItem;
import com.sender.fakedouyu.listener.RequestStreamUrlListener;
import com.sender.fakedouyu.model.NetworkRequestImpl;

import java.util.List;


/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * TODO: Replace the implementation with code for your data type.
 */
public class MyFavoriteRecyclerViewAdapter extends RecyclerView.Adapter<MyFavoriteRecyclerViewAdapter.ViewHolder> {

    private final static String TAG = "roomId";
    private final List<RoomInfo> mRoomInfos;

    public MyFavoriteRecyclerViewAdapter(List<RoomInfo> roomInfos) {
        mRoomInfos = roomInfos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_liveroom, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final RoomInfo roomInfo = mRoomInfos.get(position);
        int roomIdLog = roomInfo.getRoomId();
        Log.d(TAG, "roomId:" + roomIdLog );
        Glide.with(holder.mView.getContext()).load(roomInfo.getRoomSrc()).into(holder.roomImg);
        holder.mRoomName.setText(roomInfo.getRoomName());
        holder.nickName.setText(roomInfo.getNickname());
        holder.online.setText("观众人数：" + roomInfo.getOnline());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/4/7 项目点击事件
                final Context context = holder.mView.getContext();
                new NetworkRequestImpl(context).getStreamUrl(roomInfo.getRoomId(), new RequestStreamUrlListener() {
                    @Override
                    public void onSuccess(int roomId, String url) {
                        Intent intent = new Intent(context, PlayActivity.class);
                        intent.putExtra("URL", url).putExtra("ROOM_ID", roomId);
                        context.startActivity(intent);


                    }

                    @Override
                    public void onError() {
                        Toast.makeText(context, "无法获取数据", Toast.LENGTH_SHORT).show();

                    }
                });
//                Toast.makeText(holder.mView.getContext(), "点击了项目", Toast.LENGTH_SHORT).show();
            }
        });
//        holder.mView.setLongClickable(true);
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(holder.mView.getContext()).setTitle("删除").setMessage("删除收藏？").setNegativeButton("否", null).setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItem(position, roomInfo.getRoomId());
                    }
                }).show();

                return true;
            }
        });
    }

    private void deleteItem(int position, int roomId) {
        mRoomInfos.remove(position);
        notifyDataSetChanged();
        Room room = new Room();
        room.roomId = roomId;
        room.delete();
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

}
