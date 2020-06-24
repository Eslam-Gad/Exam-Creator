package com.EX.examcreator.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.EX.examcreator.Models.CousesModel;
import com.EX.examcreator.R;
import java.util.List;


public class ChosenCourseAdapter extends RecyclerView.Adapter<ChosenCourseAdapter.NumberViewHolder>  {

    List<CousesModel> courcesTitle;

        final private ListItemClickListener mOnClickListener ;
        private static int viewHolderCount;
        public interface ListItemClickListener {
            //void onListItemClick(int clickedItemIndex);

            void onClick(int position);
        }

        public  ChosenCourseAdapter(List<CousesModel> courcesTitle, ListItemClickListener mOnClickListener)
        {
            this.mOnClickListener = mOnClickListener;
            this.courcesTitle = courcesTitle;
        }

        private static final String TAG = ChosenCourseAdapter.class.getSimpleName();

        public ChosenCourseAdapter(ListItemClickListener mOnClickListener) {
            this.mOnClickListener = mOnClickListener;

        }

        @Override
        public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            Context context = viewGroup.getContext();
            int layoutIdForListItem = R.layout.rv_courses;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;
            View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
            NumberViewHolder viewHolder = new NumberViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(NumberViewHolder holder, int position) {
           holder.textViewTitle.setText(courcesTitle.get(position).getCoursesName());
        }

        @Override
        public int getItemCount() {
            return courcesTitle.size();
        }

        class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView textViewTitle;
            CardView cardView;

            public NumberViewHolder(View itemView) {
                super(itemView);
                textViewTitle=(TextView) itemView.findViewById(R.id.cardviewCourses) ;
                cardView= itemView.findViewById(R.id.cardview) ;
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int clickedPosition = getAdapterPosition();
                mOnClickListener.onClick( getAdapterPosition());
            }
        }

    }

