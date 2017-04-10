package com.sender.fakedouyu.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;

import com.sender.fakedouyu.R;
import com.sender.fakedouyu.adapter.MyChannelRecyclerViewAdapter;
import com.sender.fakedouyu.bean.SubChannelInfo;
import com.sender.fakedouyu.listener.RequestAllSubChannelsListener;
import com.sender.fakedouyu.model.NetworkRequestImpl;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class ChannelFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 3;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ChannelFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ChannelFragment newInstance(int columnCount) {
        ChannelFragment fragment = new ChannelFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Channel" ,"onCreate" );

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Channel" ,"onCreateView" );

        View view = inflater.inflate(R.layout.fragment_channel_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.addItemDecoration(new MyItemDecoration(16));
            ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1);
            sa.setDuration(2000);
            LayoutAnimationController lac = new  LayoutAnimationController(sa, 0.5f);
            lac.setOrder( LayoutAnimationController.ORDER_NORMAL);
            recyclerView.setLayoutAnimation(lac);

            new NetworkRequestImpl(getContext()).getAllSubChannels(new RequestAllSubChannelsListener() {
                @Override
                public void onSuccess(List<SubChannelInfo> subChannelInfos) {
                    recyclerView.setAdapter(new MyChannelRecyclerViewAdapter(subChannelInfos));
                }

                @Override
                public void onError() {

                }
            });
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("Channel" ,"onAttach" );

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("Channel" ,"onDetach" );

    }
    private class MyItemDecoration extends RecyclerView.ItemDecoration{
        private int offset;

        public MyItemDecoration(int offset) {
            this.offset = offset;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            Context context = getContext();
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(metrics);
            int density = (int) metrics.density;

            int itemMargin = density * offset;
            outRect.set(itemMargin, itemMargin, itemMargin, itemMargin);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
}
