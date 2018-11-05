package jm.through.read;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import jm.through.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private AdapterView.OnItemClickListener mItemClickListener;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private ArrayList<ReadData> itemList;

    private OnLoadMoreListener onLoadMoreListener;
    private LinearLayoutManager mLinearLayoutManager;

    private boolean isMoreLoading = false;
    private int visibleThreshold = 1;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    //more 인터페이스
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnItemClickListener{
        public void onItemClick(RecyclerView.ViewHolder holder, View view, ReadData readData, int position);
    }

    //생성자
    public RecyclerViewAdapter(OnLoadMoreListener listener){
        onLoadMoreListener = listener;
        itemList = new ArrayList<>();
    }

    //리사이클러뷰 레이아웃매니져 가져오기
    public void setLinearLayoutManager(LinearLayoutManager linearLayoutManager){
        this.mLinearLayoutManager = linearLayoutManager;
    }

    //리사이클러뷰에 스크롤 리스너 달기
    public void setRecyclerView(RecyclerView mView){
        mView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mLinearLayoutManager.getItemCount();
                firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
                if(!isMoreLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem+visibleThreshold)){
                    if(onLoadMoreListener != null){
                        onLoadMoreListener.onLoadMore();
                    }
                    isMoreLoading = true;
                }
            }
        });
    }

    public int getItemViewType(int position){
        return itemList.get(position) != null ? VIEW_ITEM:VIEW_PROG;
    }



    //클릭리스너
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener){
        mItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_ITEM){
            return new ReadViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_check,parent,false));
        } else {
            return new LoadViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_progress,parent,false));
        }
    }

    //데이터 넣기
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //ReadViewHolder readViewHolder = (ReadViewHolder) holder;
        if(holder instanceof ReadViewHolder){
            //((ReadViewHolder)holder).setMailDate(itemList.get(position));
            //((ReadViewHolder)holder).setOnItemClickListener(mItemClickListener);
        }
    }

    // 데이터 체인지
    public void addAll(List<ReadData> lst){
        itemList.clear();
        itemList.addAll(lst);
        notifyDataSetChanged();
    }

    //더보기 시 데이터 설정
    public void addItemMore(List<ReadData> lst){
        itemList.addAll(lst);
        notifyItemRangeChanged(0,itemList.size());
    }

    public void setMoreLoading(boolean isMoreLoading){
        this.isMoreLoading = isMoreLoading;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    //프로그래스 설정
    public void setProgressMore(final boolean isProgress){
        if(isProgress){
            new android.os.Handler().post(new Runnable() {
                @Override
                public void run() {
                    itemList.add(null);
                    notifyItemInserted(itemList.size() - 1);
                }
            });
        }else{
            itemList.remove(itemList.size() -1);
            notifyItemRemoved(itemList.size());
        }
    }
}
