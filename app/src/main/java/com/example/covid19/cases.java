package com.example.covid19;

public class cases {
    public  long active;
    public  long deaths;
    public  long recovered;
    public  long confirmed;
    public  long delconfirmed;
    public  long delactive;
    public  long deldeaths;
    public  long delrecovered;
    public  String date,murl,mtitle,mImgurl;

    public cases(long ac ,String dd) {
        confirmed = ac;
        date = dd;
    }

    public cases(String title ,String url ,String imgurl,String dd) {

        murl = url;
        date = dd;
        mtitle =title;
        mImgurl = imgurl;
    }

    public cases(long conf , long ac, long re, long dea,long delco, long delde ,long delreeec , String dd) {
        active = ac;
        confirmed=conf;
        recovered = re;
        deaths = dea;
        date = dd;
        delconfirmed =delco;
        delrecovered =delreeec;
        deldeaths =delde;
        delactive = delconfirmed-(delactive +deldeaths + delrecovered);
    }

    public String getmImgurl() {
        return mImgurl;
    }

    public String getMurl() {
        return murl;
    }
}
