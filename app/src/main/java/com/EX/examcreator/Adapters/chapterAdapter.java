package com.EX.examcreator.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.EX.examcreator.Models.Chapter;
import com.EX.examcreator.Models.Subject;
import com.EX.examcreator.R;
import com.EX.examcreator.ViewHolders.SubjectViewHolders;
import com.EX.examcreator.ViewHolders.chapterViewHolder;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import java.util.List;

public class chapterAdapter extends ExpandableRecyclerViewAdapter<SubjectViewHolders, chapterViewHolder> {
    public chapterAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public SubjectViewHolders onCreateGroupViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandablerv_subjects , parent , false);

        return new SubjectViewHolders(v);
    }

    @Override
    public chapterViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandablerv_chapters , parent , false);

        return new chapterViewHolder(v);
    }

    @Override
    public void onBindChildViewHolder(chapterViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {

        final Chapter chapter = (Chapter) group.getItems().get(childIndex);
        holder.bind(chapter);

    }

    @Override
    public void onBindGroupViewHolder(SubjectViewHolders holder, int flatPosition, ExpandableGroup group) {
        final Subject subject = (Subject) group;
        holder.bind(subject);
    }
}
