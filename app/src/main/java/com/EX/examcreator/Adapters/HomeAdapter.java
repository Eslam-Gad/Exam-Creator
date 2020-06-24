package com.EX.examcreator.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.EX.examcreator.Models.HomeData;
import com.EX.examcreator.R;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {

        private  Context context;
        private HomeData homeData;
        List<HomeData> homeDataList;

        private rv_onclickCallback rv_onclickCallback;

        public interface rv_onclickCallback{
            public void onClick(int pos , List<HomeData> homeDataList);
        }


    public HomeAdapter(Context context , rv_onclickCallback rv_onclickCallback) {
        this.context = context;
        this.rv_onclickCallback = rv_onclickCallback;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_card, parent, false);
        HomeHolder holder=new HomeHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
        homeData=homeDataList.get(position);
        holder.textView.setText(homeData.getName());
        holder.imageView.setImageResource(homeData.getImage());
    }


    @Override
    public int getItemCount() {
        return homeDataList!=null?homeDataList.size():0;
    }


    public void setData(List<HomeData> homeDataList) {
        this.homeDataList=homeDataList;
    }

    class HomeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        ImageView imageView;

        public HomeHolder(@NonNull final View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.cardidtext);
            imageView=itemView.findViewById(R.id.cardidimage);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            rv_onclickCallback.onClick(getAdapterPosition() , homeDataList);
        }
    }
}
