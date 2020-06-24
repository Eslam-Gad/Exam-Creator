package com.EX.examcreator.ViewHolders;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.EX.examcreator.Models.Subject;
import com.EX.examcreator.R;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class SubjectViewHolders extends GroupViewHolder {

    private TextView mtextview;
    private ImageView mimageView;
    private ImageView drawableText;


    public SubjectViewHolders(View itemView) {
        super(itemView);

        mtextview = itemView.findViewById(R.id.textview);
        mimageView = itemView.findViewById(R.id.img);

        //drawableText = itemView.findViewById(R.id.drawableText);


    }
    public void bind(Subject subject){
        mtextview.setText(subject.getTitle());

//        ColorGenerator generator = ColorGenerator.MATERIAL;
//        int color1 = generator.getRandomColor();
//
//        TextDrawable.IBuilder builder = TextDrawable.builder()
//                .beginConfig()
//                .withBorder(4)
//                .endConfig()
//                .rect();
//        TextDrawable textDrawable = builder.build(Character.toUpperCase(subject.getTitle().charAt(0))+"", color1);
//
//        drawableText.setImageDrawable(textDrawable);

    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        mimageView.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        mimageView.setAnimation(rotate);
    }
}
