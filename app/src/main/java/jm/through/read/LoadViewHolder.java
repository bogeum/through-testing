package jm.through.read;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import jm.through.R;

public class LoadViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;
    public LoadViewHolder(View itemView) {
        super(itemView);
        progressBar = (ProgressBar)itemView.findViewById(R.id.read_progress);
    }
}
