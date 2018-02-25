package com.newsapp.newsapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by SaherOs on 2/23/2018.
 */

class AdapterMyNews extends RecyclerView.Adapter<AdapterMyNews.NewsViewHolder> {
    private List<MyNews> myNewsList;
    private OnItemClickListener mListener;


    public AdapterMyNews(List<MyNews> list, OnItemClickListener listener) {
        myNewsList = list;
        mListener = listener;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View newsView = LayoutInflater.from(context).inflate(R.layout.list_news, parent, false);
        return new NewsViewHolder(newsView);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        MyNews myNews = myNewsList.get(position);
        holder.mTitle.setText(myNews.getMyTitle());
        holder.mType.setText(myNews.getMyType());
        holder.mDate.setText(myNews.getMyDate());
        holder.mSection.setText(myNews.getMySection());
        holder.firstName.setText(myNews.getFirstName());
        holder.bind(myNewsList.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return myNewsList.size();
    }

    public void clear() {
        myNewsList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<MyNews> list) {
        myNewsList.addAll(list);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(MyNews myNews);
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView mDate = (TextView) itemView.findViewById(R.id.newsDate);
        TextView mSection = (TextView) itemView.findViewById(R.id.newsSection);
        TextView firstName = (TextView) itemView.findViewById(R.id.firstName);
        TextView mTitle = (TextView) itemView.findViewById(R.id.newsTitle);
        TextView mType = (TextView) itemView.findViewById(R.id.newsType);


        public NewsViewHolder(View v) {
            super(v);
        }

        public void bind(final MyNews myNews, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(myNews);
                }
            });
        }
    }
}

