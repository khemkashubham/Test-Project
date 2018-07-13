package com.testapp.myapplication.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.testapp.myapplication.R;
import com.testapp.myapplication.models.RepoItems;

import java.util.List;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.MyViewHolder> {

    private List<RepoItems> repoItemsList;

    public RepoAdapter(List<RepoItems> repoItemsList) {
        this.repoItemsList = repoItemsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_repo, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        RepoItems repoItems = repoItemsList.get(position);

        holder.tvTitle.setText(repoItems.getName());
        holder.tvDescription.setText(repoItems.getDescription());
        holder.tvCodeLanguage.setText(repoItems.getLanguage());
        holder.tvDebug.setText(String.valueOf(repoItems.getOpenIssuesCount()));
        holder.tvUsers.setText(String.valueOf(repoItems.getWatchersCount()));


        Glide.with(holder.ivAvatar).
                load(repoItems.getOwner().getAvatarUrl())
                .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher))
                .into(holder.ivAvatar);
    }

    @Override
    public int getItemCount() {
        return repoItemsList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvDescription, tvCodeLanguage, tvDebug, tvUsers;

        private ImageView ivAvatar;

        MyViewHolder(View itemView) {
            super(itemView);

            ivAvatar = itemView.findViewById(R.id.imageView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvCodeLanguage = itemView.findViewById(R.id.tvCode);
            tvDebug = itemView.findViewById(R.id.tvDebug);
            tvUsers = itemView.findViewById(R.id.tvUser);

        }
    }
}
