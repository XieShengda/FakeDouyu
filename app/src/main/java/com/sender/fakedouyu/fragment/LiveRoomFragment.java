package com.sender.fakedouyu.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.sender.fakedouyu.R;
import com.sender.fakedouyu.adapter.MyRecyclerViewAdapter;
import com.sender.fakedouyu.bean.RoomInfo;
import com.sender.fakedouyu.listener.RequestSubChannelListener;
import com.sender.fakedouyu.model.NetworkRequestImpl;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * interface.
 */
public class LiveRoomFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column_count";
    private static final String CHANNEL_URL = "channel_url";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private String mChannelUrl;
//    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LiveRoomFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static LiveRoomFragment newInstance(int columnCount, String channelUrl) {
        LiveRoomFragment fragment = new LiveRoomFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(CHANNEL_URL, channelUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mChannelUrl = getArguments().getString(CHANNEL_URL);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liveroom_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(mColumnCount, StaggeredGridLayoutManager.VERTICAL));
            }
            recyclerView.addItemDecoration(new MyItemDecoration());
            new NetworkRequestImpl(getContext()).getSubChannel(mChannelUrl, new MyRequestSubChannelListener());

        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
    private class MyRequestSubChannelListener implements RequestSubChannelListener{
        @Override
        public void onSuccess(List<RoomInfo> roomInfos) {
            recyclerView.setAdapter(new MyRecyclerViewAdapter(roomInfos));
        }

        @Override
        public void onError() {

        }
    }
    private class MyItemDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            Context context = getContext();
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(metrics);
            int density = (int) metrics.density;

            int itemMargin = density * 3;
            outRect.set(itemMargin, itemMargin, itemMargin, itemMargin);
        }
    }
}
