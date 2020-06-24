package com.EX.examcreator.Upload_Pdf;

public class UploadPdf {
    public String id;
    public String exam_Name;
    public String url;

    public String dr_id;

    public UploadPdf() {
    }

    public UploadPdf(String id,String exam_Name, String url ,String dr_id) {
        this.exam_Name = exam_Name;
        this.url = url;
        this.id=id;

        this.dr_id=dr_id;
    }

    public String getExam_Name() {
        return exam_Name;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public String getDr_id() {
        return dr_id;
    }
}
