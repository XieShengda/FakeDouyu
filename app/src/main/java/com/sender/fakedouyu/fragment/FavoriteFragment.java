package com.sender.fakedouyu.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sender.fakedouyu.R;
import com.sender.fakedouyu.adapter.MyFavoriteRecyclerViewAdapter;
import com.sender.fakedouyu.bean.RoomInfo;
import com.sender.fakedouyu.custom_view.MyRecyclerView;
import com.sender.fakedouyu.listener.RequestHeartRoomsListener;
import com.sender.fakedouyu.model.NetworkRequestImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class FavoriteFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private final List<RoomInfo> mRoomInfos;
    private Context mContext;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FavoriteFragment() {
        mRoomInfos = new ArrayList<>();
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FavoriteFragment newInstance(int columnCount) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_list, container, false);

        RecyclerView recyclerView = (MyRecyclerView) view.findViewById(R.id.favorite_recyclerview);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.favorite_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimary_light));


        // 设置recyclerView的适配器
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, mColumnCount));
        }
        final MyFavoriteRecyclerViewAdapter adapter = new MyFavoriteRecyclerViewAdapter(mRoomInfos);
        recyclerView.setAdapter(adapter);
        new NetworkRequestImpl(mContext).getHeartRooms(new RequestHeartRoomsListener() {
            @Override
            public void onSuccess(RoomInfo roomInfo) {
                mRoomInfos.add(roomInfo);
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onError() {
                Toast.makeText(mContext, "无法获取数据", Toast.LENGTH_SHORT).show();

            }
        });

        //设置刷新监听器
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRoomInfos.clear();

                new NetworkRequestImpl(mContext).getHeartRooms(new RequestHeartRoomsListener() {
                    @Override
                    public void onSuccess(RoomInfo roomInfo) {
                        mRoomInfos.add(roomInfo);
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onError() {
                        Toast.makeText(mContext, "无法获取数据", Toast.LENGTH_SHORT).show();
                    }
                });
                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;

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

}
