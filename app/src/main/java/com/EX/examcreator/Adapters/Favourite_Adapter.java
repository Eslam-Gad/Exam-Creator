package com.EX.examcreator.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.EX.examcreator.Database_OFF_Line.Model_Favourite;
import com.EX.examcreator.R;
import java.util.List;

public class Favourite_Adapter extends RecyclerView.Adapter<Favourite_Adapter.NumberViewHolder>  {

    List<Model_Favourite> model_favourites;

    final private ListItemClickListener mOnClickListener ;
    public interface ListItemClickListener {
        void onClick(Model_Favourite model_favourites);
    }

    public Favourite_Adapter(ListItemClickListener mOnClickListener, List<Model_Favourite> model_favourites) {
        this.mOnClickListener = mOnClickListener;
        this.model_favourites = model_favourites;
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.content_favourite;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        holder.qustionFav.setText((position+1)+" - "+model_favourites.get(position).getQuestions());
        holder.ch1.setText(model_favourites.get(position).getChoice1());
        holder.ch2.setText(model_favourites.get(position).getChoice2());
        holder.ch3.setText(model_favourites.get(position).getChoice3());
        holder.ch4.setText(model_favourites.get(position).getChoice4());
        holder.answer.setText("Answer is: "+model_favourites.get(position).getAnswer());
        boolean isExpanded=model_favourites.get(position).isExpanded();
        holder.expandLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.difficulty.setText("Difficulty: "+model_favourites.get(position).getDifficulty());
        holder.courseName.setText("Course: "+model_favourites.get(position).getCourseName());
        holder.chaptername.setText("Chapter: "+model_favourites.get(position).getChapterName());
    }

    @Override
    public int getItemCount() {
        if (model_favourites !=null)
            return model_favourites.size();
        else
            return 3;
    }

    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
         TextView qustionFav,ch1,ch2,ch3,ch4,answer,difficulty,chaptername, courseName;
         LinearLayout expandLayout;
         ImageButton deletFav;

        public NumberViewHolder(View itemView) {
            super(itemView);
            qustionFav=(TextView) itemView.findViewById(R.id.question_fav);
            ch1=(TextView) itemView.findViewById(R.id.choice1);
            ch2=(TextView) itemView.findViewById(R.id.choice2);
            ch3=(TextView) itemView.findViewById(R.id.choice3);
            ch4=(TextView) itemView.findViewById(R.id.choice4);
            chaptername=(TextView) itemView.findViewById(R.id.chapter);
            courseName=(TextView) itemView.findViewById(R.id.course);
            answer=(TextView) itemView.findViewById(R.id.answer);
            difficulty=(TextView) itemView.findViewById(R.id.difficulty);
            deletFav=(ImageButton) itemView.findViewById(R.id.delet_Fav);
            expandLayout = (LinearLayout)itemView.findViewById(R.id.expandable_layout);
            qustionFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Model_Favourite modelFavourite=model_favourites.get(getAdapterPosition());
                    modelFavourite.setExpanded(!modelFavourite.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

            deletFav.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            mOnClickListener.onClick(model_favourites.get(getAdapterPosition()));
        }
    }
}
