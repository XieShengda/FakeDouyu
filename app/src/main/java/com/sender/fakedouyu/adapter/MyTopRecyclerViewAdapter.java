package com.sender.fakedouyu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sender.fakedouyu.R;
import com.sender.fakedouyu.bean.RoomInfo;
import com.sender.fakedouyu.fragment.ChannelFragment.OnListFragmentInteractionListener;
import com.sender.fakedouyu.fragment.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyTopRecyclerViewAdapter extends RecyclerView.Adapter<MyTopRecyclerViewAdapter.ViewHolder> {

    private final List<RoomInfo> mValues;

    public MyTopRecyclerViewAdapter(List<RoomInfo> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_liveroom, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        RoomInfo roomInfo = mValues.get(position);
        holder.mItem = roomInfo;
        Glide.with(holder.mView.getContext()).load(roomInfo.getRoomSrc()).into(holder.roomImg);
        holder.mRoomName.setText(roomInfo.getRoomName());
        holder.nickName.setText(roomInfo.getNickname());
        if (roomInfo.getOnline() != 0) {
            holder.online.setText("观众人数：" + roomInfo.getOnline());
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/4/7 项目点击事件
                Toast.makeText(holder.mView.getContext(), "点击了项目", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView roomImg;
        public final TextView mRoomName;
        public final TextView online;
        public final TextView nickName;
        public RoomInfo mItem;

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
