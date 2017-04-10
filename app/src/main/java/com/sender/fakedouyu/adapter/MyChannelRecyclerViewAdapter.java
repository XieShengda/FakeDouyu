package com.sender.fakedouyu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sender.fakedouyu.R;
import com.sender.fakedouyu.bean.SubChannelInfo;
import com.sender.fakedouyu.fragment.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * TODO: Replace the implementation with code for your data type.
 */
public class MyChannelRecyclerViewAdapter extends RecyclerView.Adapter<MyChannelRecyclerViewAdapter.ViewHolder> {

    private final List<SubChannelInfo> mValues;

    public MyChannelRecyclerViewAdapter(List<SubChannelInfo> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_channel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SubChannelInfo subChannelInfo = mValues.get(position);
        Glide.with(holder.mView.getContext()).load(subChannelInfo.getIconUrl()).into(holder.mIcon);
        holder.mTagName.setText(subChannelInfo.getTagName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/4/10 跳转到channelActivity
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mIcon;
        public final TextView mTagName;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIcon = (ImageView) view.findViewById(R.id.channel_icon);
            mTagName = (TextView) view.findViewById(R.id.tag_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTagName.getText() + "'";
        }
    }
}
