package com.EX.examcreator.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.EX.examcreator.Models.ChapterModel;
import com.EX.examcreator.R;
import java.util.List;


public class AddChapterAdapter extends RecyclerView.Adapter<AddChapterAdapter.AddChapterAdapterViewHolder>  {

    private List<ChapterModel> chapters;

    public AddChapterAdapter(List<ChapterModel> chapters){
        this.chapters = chapters;
    }

    @NonNull
    @Override
    public AddChapterAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.chapter_rv_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        AddChapterAdapter.AddChapterAdapterViewHolder viewHolder = new AddChapterAdapter.AddChapterAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddChapterAdapterViewHolder holder, int position) {

        holder.chapterName.setText(chapters.get(position).getName());
        holder.chapterNumber.setText("Chapter : "+chapters.get(position).getNumber());
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    class AddChapterAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView chapterName;
        TextView chapterNumber;


        public AddChapterAdapterViewHolder(View itemView) {
            super(itemView);
            chapterName = (TextView) itemView.findViewById(R.id.chapterName);
            chapterNumber = (TextView) itemView.findViewById(R.id.chapterNumber);
        }
    }

}
