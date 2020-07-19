package com.EX.examcreator.Activities;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.EX.examcreator.Models.ChapterModel;
import com.EX.examcreator.Models.CousesModel;
import com.EX.examcreator.Models.Question;
import com.EX.examcreator.PDFCreate.CommonPdf;
import com.EX.examcreator.PDFCreate.pdfDocumentAdapter;
import com.EX.examcreator.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import es.dmoral.toasty.Toasty;


public class CreateExam_DetermineQuestions extends AppCompatActivity {

    ArrayList<ChapterModel> listOfChapters;
    CousesModel Course;

    DatabaseReference databaseQuestions;
    DatabaseReference databaseCources;
    List<Question> questionsListEasy;
    List<Question> questionsListMedium;
    List<Question> questionsListHard;


    Set<Question> total_question;

    String dr_name;
    String university;
    String faculty;
    String department;
    String total_degrees;
    String exam_time;
    String exam_type;
    int selectedYear;
    int selectedMonth;
    int selectedDay;
    String exam_date;

    EditText total_easy, total_medium, total_hard, total_degree;
    TextView available_questiions, hardt, medt, easyt;
    Button time, exam;
    DatePicker datePicker;
    Button btn_create_pdf;

    int hard, medium, easy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exam__determine_questions);
        btn_create_pdf = findViewById(R.id.btn_create_pdf);

        available_questiions = (TextView) findViewById(R.id.avalable_q);
        easyt = (TextView) findViewById(R.id.easy);
        medt = (TextView) findViewById(R.id.medium);
        hardt = (TextView) findViewById(R.id.hard);
        total_easy = (EditText) findViewById(R.id.to_eas);
        total_medium = (EditText) findViewById(R.id.to_med);
        total_hard = (EditText) findViewById(R.id.tot_ha);
        total_degree = (EditText) findViewById(R.id.tot_deg);
        exam = (Button) findViewById(R.id.popMenu);
        time = (Button) findViewById(R.id.popMenutime);
        datePicker = (DatePicker) findViewById(R.id.dpicker);

        listOfChapters = new ArrayList<>();
        listOfChapters = this.getIntent().getExtras().getParcelableArrayList("checkedChapters");
        Course = this.getIntent().getExtras().getParcelable("Course_choosing");

        databaseCources = FirebaseDatabase.getInstance().getReference("questions").child(Course.getId());
        questionsListEasy = new ArrayList<>();
        questionsListHard = new ArrayList<>();
        questionsListMedium = new ArrayList<>();

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        btn_create_pdf.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onClick(View v) {

                                create();
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.home_ic) {

            Intent intent = new Intent(CreateExam_DetermineQuestions.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();

        databaseCources.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot node : dataSnapshot.getChildren()) {
                    for (ChapterModel chapterModel : listOfChapters) {
                        if (node.getKey().equals(chapterModel.getId())) {
                            databaseQuestions = FirebaseDatabase.getInstance().getReference("questions").
                                    child(Course.getId()).child(node.getKey());
                            databaseQuestions.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        Question question = dataSnapshot1.getValue(Question.class);
                                        if (question.getDifficulty().equals("1")) {
                                            easy++;
                                            questionsListEasy.add(question);


                                        } else if (question.getDifficulty().equals("2")) {
                                            medium++;
                                            questionsListMedium.add(question);

                                        } else if (question.getDifficulty().equals("3")) {
                                            hard++;
                                            questionsListHard.add(question);
                                        }

                                        final int size = easy + medium + hard;
                                        hardt.setText(hard + "");
                                        easyt.setText(easy + "");
                                        medt.setText(medium + "");
                                        available_questiions.setText("Available questions: " + size);


                                        total_easy.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                            }

                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                int length = total_easy.getText().toString().length();
                                                if (length > 0) {
                                                    if (Integer.parseInt(s.toString()) > easy || Integer.parseInt(s.toString()) == 0) {
                                                        total_easy.getText().delete(length - 1, length);
                                                        Toasty.error(CreateExam_DetermineQuestions.this, "It cannot exceed the available questions ", Toast.LENGTH_LONG, true).show();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void afterTextChanged(Editable s) {
                                            }
                                        });

                                        total_medium.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                            }

                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                int length = total_medium.getText().toString().length();
                                                if (length > 0) {
                                                    if (Integer.parseInt(s.toString()) > medium || Integer.parseInt(s.toString()) == 0) {
                                                        total_medium.getText().delete(length - 1, length);
                                                        Toasty.error(CreateExam_DetermineQuestions.this, "It cannot exceed the available questions ", Toast.LENGTH_LONG, true).show();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void afterTextChanged(Editable s) {
                                            }
                                        });

                                        total_hard.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                            }

                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                int length = total_hard.getText().toString().length();
                                                if (length > 0) {
                                                    if (Integer.parseInt(s.toString()) > hard || Integer.parseInt(s.toString()) == 0) {
                                                        total_hard.getText().delete(length - 1, length);
                                                        Toasty.error(CreateExam_DetermineQuestions.this, "It cannot exceed the available questions ", Toast.LENGTH_LONG, true).show();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void afterTextChanged(Editable s) {
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            break;
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void listpopu(final View view) {

        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.list_exam, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ((Button) view).setText(item.getTitle());
                return true;
            }
        });
        popupMenu.show();
    }

    public void listpoputime(final View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.list_time, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ((Button) view).setText(item.getTitle());
                return true;
            }
        });
        popupMenu.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void create() {

        /* ---- get all data to pass to PDF (Date, Time, Dr_name, questions, Faculty, University, Department, Total degrees, Exam type)*/

        if (!TextUtils.isEmpty(total_degree.getText().toString()) &&
                !exam.getText().toString().equals("Exam Type") &&
                !time.getText().toString().equals("Exam Time")) {

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

            dr_name = sharedPreferences.getString("signature_key", null);
            university = sharedPreferences.getString("univ_key", null);
            faculty = sharedPreferences.getString("faculty_key", null);
            department = sharedPreferences.getString("dept_key", null);
            total_degrees = total_degree.getText().toString();
            exam_time = time.getText().toString();
            exam_type = exam.getText().toString();
            selectedYear = datePicker.getYear();
            selectedMonth = datePicker.getMonth();
            selectedDay = datePicker.getDayOfMonth();
            exam_date = selectedDay + "/" + selectedMonth + "/" + selectedYear;

            int hardWanted = 0, easyWanted = 0, mediumWanted = 0, total = 0;

            if (!TextUtils.isEmpty(total_easy.getText())) {
                easyWanted = Integer.parseInt(total_easy.getText().toString());
            }

            if (!TextUtils.isEmpty(total_medium.getText())) {
                mediumWanted = Integer.parseInt(total_medium.getText().toString());
            }

            if (!TextUtils.isEmpty(total_hard.getText())) {
                hardWanted = Integer.parseInt(total_hard.getText().toString());
            }


            if (total != (easyWanted + hardWanted + mediumWanted) && total != 0) {
                Toasty.info(CreateExam_DetermineQuestions.this, "total should be the sum of easy, medium and hard!!", Toast.LENGTH_LONG, true).show();
                return;
            }


            /* get randam questions */

            Set<Question> question_easySet = new HashSet<>();   // easy_questions
            Set<Question> question_mediumSet = new HashSet<>(); // medium_questions
            Set<Question> question_hardSet = new HashSet<>();  // hard_questions


            while (question_easySet.size() != easyWanted) {
                int randam_easy = (int) (Math.random() * easy);
                question_easySet.add(questionsListEasy.get(randam_easy));
            }

            while (question_mediumSet.size() != mediumWanted) {
                int randam_med = (int) (Math.random() * medium);
                question_mediumSet.add(questionsListMedium.get(randam_med));
            }

            while (question_hardSet.size() != hardWanted) {
                int randam_hard = (int) (Math.random() * hard);
                question_hardSet.add(questionsListHard.get(randam_hard));
            }

            total_question = new HashSet<>();
            total_question.addAll(question_easySet);
            total_question.addAll(question_mediumSet);
            total_question.addAll(question_hardSet);



            /*--------------------------------------------------------------------------------------------*/

            /* PDF Code : you will find  all data that you want */

            creatPDFFile(CommonPdf.getApppath(CreateExam_DetermineQuestions.this) + "test_pdf.pdf");

            /*
             * ========
             * ========
             * ========
             *
             * */
            /*------------- PDF Code ----------------*/


        } else {
            Toasty.info(CreateExam_DetermineQuestions.this, "Pls: all fields required !!", Toast.LENGTH_LONG, true).show();
        }

    }


//-----------------------------------------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void creatPDFFile(String path) {
        if (new File(path).exists())
            new File(path).delete();
        try {
            Document document = new Document();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Toast.makeText(this, "Not Support Android 'Q'", Toast.LENGTH_LONG).show();
                return;
            }

            //Save
            PdfWriter.getInstance(document, new FileOutputStream(path));

            //Open
            document.open();


            //Setting
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Exam Creator");
            document.addCreator("Exam Creator Team");


//            document.add(new Image() {
//                @Override
//                public void setUrl(URL url) {
//                    super.setUrl(url);
//                }
//            });


            //Font Setting
            BaseColor colorAccent = new BaseColor(0, 153, 204, 255);
            float fontsize = 20.0f;
            float valueFontSize = 26.0f;


            //Custom font
            BaseFont fontName = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);

//----------------------------------------------------------------------------------------------------------------------------
            //Create Tite PDF
            Font titlefont = new Font(fontName, 29.0f, Font.NORMAL, BaseColor.BLACK);

            Font ftitle = new Font(fontName, 27.0f, Font.NORMAL, BaseColor.BLACK);
            Font orederNumberValueFont = new Font(fontName, valueFontSize, Font.NORMAL, BaseColor.BLACK);

            addNewItem(document, university + " University", Element.ALIGN_CENTER, titlefont);
            addNewItem(document, "Faculty of " + faculty, Element.ALIGN_CENTER, ftitle);
            addNewItem(document, exam_type + " " + selectedYear, Element.ALIGN_CENTER, titlefont);

//----------------------------------------------------------------------------------------------------------------------------


            addNewItemWithLeftAndRight(document, "Doctor:" + dr_name, "Data:" + exam_date, ftitle, ftitle);
            addNewItemWithLeftAndRight(document, "Course:" + Course.getCoursesName(), "Time:" + exam_time, ftitle, ftitle);
            addNewItemWithLeftAndRight(document, "Department:" + department, "Grade:" + total_degrees, ftitle, ftitle);
            addLineSpace(document);
            addLineSperator(document);
            //-------------------------------------------------------------------------------------------------------------------------
            addNewItem(document, "Choose the Correct Answer:\n(One Mark for Each Part)", Element.ALIGN_LEFT, ftitle);
            addLineSpace(document);
            addLineSpace(document);

//------------------------------------------------------------------------------------------------------------------------

            int i = 1;

            for (Question question : total_question) {

                addNewItem(document, "(" + i + ") " + question.getQuestionHead(), Element.ALIGN_LEFT, titlefont);

                addLineSpace(document);

                addNewItem(document, "A) " + question.getChooseOne(), Element.ALIGN_LEFT, orederNumberValueFont);
                addNewItem(document, "B) " + question.getChooseSecond(), Element.ALIGN_LEFT, orederNumberValueFont);
                addNewItem(document, "C) " + question.getChooseThird(), Element.ALIGN_LEFT, orederNumberValueFont);
                addNewItem(document, "D) " + question.getChoosefourth(), Element.ALIGN_LEFT, orederNumberValueFont);

                addLineSpace(document);
                addLineSperator(document);

                i++;
            }

//--------------------------------------------------------------------------------------------------------------------------
            addLineSpace(document);
            addNewItem(document, "Best Wishes", Element.ALIGN_CENTER, titlefont);
            addLineSpace(document);
            addLineSpace(document);
            addNewItem(document, "Dr : "+dr_name, Element.ALIGN_CENTER, titlefont);


            document.newPage();
            addNewItem(document, Course.getCoursesName() + " Exam Answers " + exam_date, Element.ALIGN_LEFT, titlefont);
            addLineSpace(document);
            addLineSpace(document);

            int answerCounter=1;

            for (Question question : total_question) {

                addNewItem(document, "(" + answerCounter + ") " + " : " + question.getRightanswer(), Element.ALIGN_LEFT, titlefont);

                addLineSpace(document);

                answerCounter++;
            }


//--------------------------------------------------------------------------------------------------------------------------
  /*          //add More
          //  Font orederNumberFont= new Font(fontName,fontsize,Font.NORMAL,colorAccent);
            addNewItem(document,"Order No",Element.ALIGN_LEFT,orederNumberFont);



           // Font orederNumberValueFont= new Font(fontName,valueFontSize,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"#717171",Element.ALIGN_LEFT,orederNumberValueFont);

            addLineSperator(document);

            addNewItem(document,"Order Date",Element.ALIGN_LEFT,orederNumberFont);
            addNewItem(document,"3/8/2019",Element.ALIGN_LEFT,orederNumberValueFont);

            addLineSperator(document);

            addNewItem(document,"ACcount Name",Element.ALIGN_LEFT,orederNumberFont);
            addNewItem(document,"Walid Bash",Element.ALIGN_LEFT,orederNumberValueFont);

            addLineSperator(document);

            //add product order detail
            addLineSpace(document);
            addNewItem(document,"Product Detail",Element.ALIGN_CENTER,titlefont);
            addLineSperator(document);


            //Item 1
            addNewItemWithLeftAndRight(document,"Pizza 25","(0.0%)",titlefont,orederNumberValueFont);
            addNewItemWithLeftAndRight(document,"12.0*1000","12000.0",titlefont,orederNumberValueFont);
            addLineSperator(document);


            //Item 2
            addNewItemWithLeftAndRight(document,"Pizza 26","(0.0%)",titlefont,orederNumberValueFont);
            addNewItemWithLeftAndRight(document,"12.0*1000","12000.0",titlefont,orederNumberValueFont);
            addLineSperator(document);

            //Total
            addLineSpace(document);
            addLineSperator(document);
            addNewItemWithLeftAndRight(document,"Total","24000.0",titlefont,orederNumberValueFont);  */
            //--------------------------------------------------------------------------------------------------------------------------
            document.close();
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();


            printPDF();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Success1", Toast.LENGTH_SHORT).show();

        } catch (DocumentException e) {
            e.printStackTrace();
            Toast.makeText(this, "Success2", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Success3", Toast.LENGTH_SHORT).show();

        }


    }

    //----------------------------------------------------------------------------------------------------
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void printPDF() {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        try {
            PrintDocumentAdapter printDocumentAdapter = new pdfDocumentAdapter(CreateExam_DetermineQuestions.this, CommonPdf.getApppath(CreateExam_DetermineQuestions.this) + "test_pdf.pdf");
            printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());
        } catch (Exception ex) {
            Log.e("EDMTDev", "" + ex.getMessage());
        }
    }

    //------------------------------------------------------------------------------------------------------
    private void addNewItemWithLeftAndRight(Document document, String textLeft, String textRight, Font textLeftFont, Font textRightFont) throws DocumentException {

        Chunk chunkTextLeft = new Chunk(textLeft, textLeftFont);
        Chunk chunkTextRight = new Chunk(textRight, textRightFont);
        Paragraph p = new Paragraph(chunkTextLeft);
        p.add(new Chunk(new VerticalPositionMark()));
        p.add(chunkTextRight);
        document.add(p);
    }

    //---------------------------------------------------------------------------------------------------------
    private void addLineSperator(Document document) throws DocumentException {

        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);
    }

    //--------------------------------------------------------------------------------------------------------------
    private void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));

    }

    //-----------------------------------------------------------------------------
    private void addNewItem(Document document, String text, int align, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text, font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);
    }
}
