package com.EX.examcreator.Models;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class Subject extends ExpandableGroup<Chapter> {


    public Subject(String title, List<Chapter> items) {
        super(title, items);
    }
}
