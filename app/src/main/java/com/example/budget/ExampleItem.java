package com.example.budget;
public class ExampleItem {
    private int mImageResource,medit,mdelete,mok;
    private String mText1;
    private String mText2;

    public ExampleItem(int imageResource, String text1, String text2,int edit,int delete,int ok) {
        mImageResource = imageResource;
        mText1 = text1;
        mText2 = text2;
        medit=edit;
        mdelete=delete;
        mok=ok;
    }

    public void changeText1(String text1,String text2){
        mText1=text1;
        mText2=text2;
    }

    public int getedit() {
        return medit;
    }

    public int getdelete() {
        return mdelete;
    }

    public int getok() {
        return mok;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public String getText1() {
        return mText1;
    }

    public String getText2() {
        return mText2;
    }

}

