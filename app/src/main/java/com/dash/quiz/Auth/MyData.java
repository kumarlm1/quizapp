package com.dash.quiz.Auth;

public class MyData {
    String subject,staff;
    public MyData(String subject){
        this.subject=subject;
    }
    public MyData(String subject,String staff){
        this.subject=subject;
        this.staff=staff;
    }


    public String get_subject(){return this.subject;}
    public String get_staff(){return this.staff;}
}
