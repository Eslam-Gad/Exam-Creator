package com.EX.examcreator;


import com.EX.examcreator.Models.OnBoardingModel;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingSuppliers {

    public static List<OnBoardingModel> getOnBoardingObjects() {
        ArrayList<OnBoardingModel> object = new ArrayList<OnBoardingModel>();
        object.add(new OnBoardingModel(R.drawable.logo,"Exam Creator","You Append Questions, We Create Exam "));
        object.add(new OnBoardingModel(R.drawable.st_wifi,"Network","Must Connect With Internet to Use App"));
        object.add(new OnBoardingModel(R.drawable.st_fav,"Favourite","Favourite any Questions to Browse Offline"));
        object.add(new OnBoardingModel(R.drawable.st_swipe,"Swipe","Swipe list Items to Delete and Update"));
        return object;
    }
}




