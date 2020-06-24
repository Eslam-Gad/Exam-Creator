package com.EX.examcreator.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.EX.examcreator.Models.ChapterModel;
import com.EX.examcreator.R;
import java.util.List;


public class ChosenChapterAdapter  extends RecyclerView.Adapter<com.EX.examcreator.Adapters.ChosenChapterAdapter.NumberViewHolder>  {


    List<ChapterModel> chapterModels;


    private ListItemClickListener mOnClickListener ;
      private static int viewHolderCount;
      public interface ListItemClickListener {
          //void onListItemClick(int clickedItemIndex);

          void onClick(View view, int position);
      }

      public  ChosenChapterAdapter(List<ChapterModel> chapterModels , ListItemClickListener mOnClickListener)
      {
          this.mOnClickListener = mOnClickListener;
          this.chapterModels = chapterModels;
      }

      public ChosenChapterAdapter(ListItemClickListener mOnClickListener) {
          this.mOnClickListener = mOnClickListener;

      }

      @Override
      public com.EX.examcreator.Adapters.ChosenChapterAdapter.NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
          Context context = viewGroup.getContext();
          int layoutIdForListItem = R.layout.rv_chapters;
          LayoutInflater inflater = LayoutInflater.from(context);
          boolean shouldAttachToParentImmediately = false;
          View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
          com.EX.examcreator.Adapters.ChosenChapterAdapter.NumberViewHolder viewHolder = new com.EX.examcreator.Adapters.ChosenChapterAdapter.NumberViewHolder(view);
          return viewHolder;
      }

    @Override
    public void onBindViewHolder(@NonNull NumberViewHolder holder, int position) {
           holder.checkBox.setText(chapterModels.get(position).getName());

    }

      @Override
      public int getItemCount() {
          return chapterModels.size();
      }

      class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
          CheckBox checkBox;
          CardView cardView;

          public NumberViewHolder(View itemView) {
              super(itemView);
              checkBox= itemView.findViewById(R.id.checkboxChapters) ;
              checkBox.setOnClickListener(this);
          }

          @Override
          public void onClick(View v) {
              int clickedPosition = getAdapterPosition();
              mOnClickListener.onClick( v , getAdapterPosition());
          }
      }

  }

