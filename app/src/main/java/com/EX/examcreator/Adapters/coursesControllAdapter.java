package com.EX.examcreator.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.EX.examcreator.Models.CousesModel;
import com.EX.examcreator.R;
import java.util.List;


public class coursesControllAdapter extends RecyclerView.Adapter<coursesControllAdapter.CoursesControllViewHolder>  {

    CallbackHandular callbackHandular;

    public interface CallbackHandular{
        public void addChHandular(int pos);
        public void editHandular(int pos);
        public void delHandular(int pos);
    }

    public coursesControllAdapter(List<CousesModel> coursesTitle,   CallbackHandular callbackHandular) {
        this.coursesTitle = coursesTitle;
        this.callbackHandular =callbackHandular;
    }

    private List<CousesModel> coursesTitle;


    @NonNull
    @Override
    public CoursesControllViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.course_controll_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        coursesControllAdapter.CoursesControllViewHolder viewHolder = new coursesControllAdapter.CoursesControllViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesControllViewHolder holder, int position) {

        holder.textViewTitle.setText(coursesTitle.get(position).getCoursesName());
        holder.textViewCode.setText("Course Code: "+coursesTitle.get(position).getCourse_code());
    }

    @Override
    public int getItemCount() {
        return coursesTitle.size();
    }

    class CoursesControllViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textViewTitle;
        TextView textViewCode;

        Button add;
        Button edit;
        Button del;


        public CoursesControllViewHolder(View itemView) {
            super(itemView);
            textViewTitle=(TextView) itemView.findViewById(R.id.courseName) ;
            textViewCode=(TextView) itemView.findViewById(R.id.courseCode) ;

            add= itemView.findViewById(R.id.add_chapter);
            edit= itemView.findViewById(R.id.editCourseName);
            del= itemView.findViewById(R.id.deleteCourse);

            edit.setOnClickListener(this);
            del.setOnClickListener(this);
            add.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if(v.getId()==edit.getId())
            callbackHandular.editHandular(getAdapterPosition());
            else if(v.getId()==del.getId())
                callbackHandular.delHandular(getAdapterPosition());
            else
                callbackHandular.addChHandular(getAdapterPosition());

        }
    }
}
