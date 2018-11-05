package jm.through.read;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import jm.through.R;

public class MyFragment extends Fragment implements RecyclerViewAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private ArrayList<ReadData> itemList;
    private RecyclerViewAdapter recyclerViewAdapter;
    private SwipeRefreshLayout swipeRefresh; // 새로고침

    public MyFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.check_board_item, container, false);
        itemList = new ArrayList<>(); //어레이리스트 생성
        swipeRefresh = (SwipeRefreshLayout)v.findViewById(R.id.frag_swipe);
        RecyclerView mRecyclerView = (RecyclerView)v.findViewById(R.id.recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager); // 어뎁터에 LinearLayoutManager 보내기
        recyclerViewAdapter = new RecyclerViewAdapter(this); // LinearLayoutManager 설정
        recyclerViewAdapter.setLinearLayoutManager(mLayoutManager); // 어뎁터에 RecyclerView 정보 보내기
//        recyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener(){ // 클릭시 할 행동
//
//
//        });
        mRecyclerView.setAdapter(recyclerViewAdapter); // RecyclerView 어댑터 설정
        swipeRefresh.setOnRefreshListener(this); // SwipeRefresh 리스너

        return v;
    }

    // 시작시 데이터 주입
    public void onStart() {
        super.onStart();
        loadData();
    }

    // 스크롤 위에서 아래로 내렸을 떄 새로고침
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(false);
                loadData();
            }
        },2000);
    }

    //스크롤 아래에서 위로 했을 시 더보기
    @Override
    public void onLoadMore() {
        recyclerViewAdapter.setProgressMore(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                itemList.clear();
                recyclerViewAdapter.setProgressMore(false);
                int start = recyclerViewAdapter.getItemCount();
                int end = start + 15;
//                for(int i = start + 1; i <= end; i++){
//                    ReadData readData = new ReadData();
//                    itemList.add(readData);
//                }
                recyclerViewAdapter.addItemMore(itemList);
                recyclerViewAdapter.setMoreLoading(false);
            }
        }, 1000);

    }

    //데이터 주입
    private void loadData() {
        itemList.clear();
//        for(int i = 1; i <= 20; i++){
//            ReadData readData = new ReadData();
//            itemList.add(readData);
//        }
        recyclerViewAdapter.addAll(itemList);
    }
}
