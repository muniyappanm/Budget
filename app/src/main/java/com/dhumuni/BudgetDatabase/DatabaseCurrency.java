package com.dhumuni.BudgetDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DatabaseCurrency extends SQLiteOpenHelper {
    String[] Country=new String[]{"UAE","Afghanistan","Albania","Armenia","Angola","Argentina","Australia","Kiribati","Coconut Islands","Nauru","Tuvalu","Aruba","Azerbaijan","Bosnia and Herzegovina","Barbados","Bangladesh","Bulgaria","Bahrain","Burundi","Bermuda","Brunei","Singapore","Bolivia","Brazil","Bahamas","Bhutan (Nu.)","Botswana","Belarus","Belize","Canada","Congo (₣)","Lichtenstein","Switzerland","Chile","China","Colombia","Costa Rica","Cuba","Cape Verde","Czech Republic","Djibouti","Denmark","Dominican Republic","Algeria","Egypt","Eritrea","Ethiopia","Akrotiri and Dhekelia","Andorra","Austria","Belgium","Cyprus","Estonia","Finland","France","Germany","Greece","Ireland","Italy","Bhutan (₹)","Latvia","Lithuania","Luxembourg","Malta","Monaco","Montenegro","Netherlands","Portugal","San-Marino","Slovakia","Slovenia","Spain","Vatican","Fiji","Falkland Islands","Alderney","British Indian Ocean Territory (£)","Great Britain","Isle of Maine","Georgia","British Indian Ocean Territory ($)","Ghana","Gibraltar","Gambia","Guinea","Guatemala","Guyana","Hong Kong","Honduras","Croatia","Haiti (G)","Hungary","Indonesia","Israel","Palestine","Haiti ($)","India","Iraq","Iran","Iceland","Jamaica","Jordan","Japan","Kenya","Kyrgyzstan","Cambodia","North Korea","South Korea","Kuwait","Cayman Islands","Kazakhstan","Laos","Lebanon","Sri Lanka","Liberia","Kosovo (€)","Libya","Morocco","Moldova","Madagascar","Macedonia","Myanmar (K)","Mongolia","Macao","Mauritania","Mauritius","Maldives","Malawi","Mexico","Malaysia","Mozambique","Kosovo (din)","Nigeria","Nicaragua","Norway","Nepal","Cook Islands","New Zealand","Niue","Pitcairn Island","Oman","Lesotho (L)","Peru","Papua New Guinea","Philippines","Pakistan","Poland","Paraguay","Qatar","Romania","Lesotho (R)","Serbia","Russia","Namibia ($)","Rwanda","Saudi Arabia","Solomon Islands","Seychelles","Sudan","Sweden","Ascension Island","Saint Helena","Tristan da Cunha","Sierra Leone","Somalia","Suriname","Sao Tome and Principe","Syria","Swaziland","Thailand","Tajikistan","Turkmenistan","Tunisia","Tonga","North Cyprus","Turkey","Trinidad and Tobago","Taiwan","Tanzania","Ukraine","Uganda","American Samoa","Namibia (R)","British Virgin Islands","Guam","Panama (B/.)","Marshall Islands","Micronesia","Northern Mariana Islands","Pacific Remote islands","Palau","Panama ($)","Puerto Rico","Turks and Caicos Islands","United States of America","US Virgin Islands","Uruguay","Uzbekistan","Venezuela","Vietnam","Vanuatu","Samoa","Benin","Burkina Faso","Cameroon","Central African Republic","Chad","Central African CFA franc","Côte d'Ivoire","Equatorial Guinea","Gabon","Guinea-Bissau","Mali","Niger","Senegal","Togo","Anguilla","Antigua and Barbuda","Dominica","Grenada","Montserrat","Saint Kitts and Nevis","Saint Lucia","Saint Vincent and Grenadine","French Polynesia","New Caledonia","Wallis and Futuna","Yemen","South Ossetia (ლ)","South Ossetia (р.)","South Africa","Zambia","Zimbabwen"};
    String[] Symbol=new String[]{"د.إ","Af","L","Դ","Kz","$","$","$","$","$","$","ƒ","ман","КМ","$","৳","лв","ب.د","₣","$","$","$","Bs.","R$","$","Nu.","P","Br","$","$","₣","₣","₣","$","¥","$","₡","$","$","Kč","₣","kr","$","د.ج","£","Nfk","ብር","€","€","€","€","€","€","€","€","€","€","€","€","₹","€","€","€","€","€","€","€","€","€","€","€","€","€","$","£","£","£","£","£","ლ","$","₵","£","D","₣","Q","$","$","L","Kn","G","Ft","Rp","₪","₪","$","₹","ع.د","﷼","Kr","$","د.ا","¥","Sh","С̲ ","៛","₩","₩","د.ك","$","〒","₭","ل.ل","Rs","$","€","ل.د","د.م.","L","Ar","ден","K","₮","P","UM","₨","ރ.","MK","$","RM","MTn","din","₦","C$","kr","₨","$","$","$","$","ر.ع.","L","S/.","K","₱","₨","zł","₲","ر.ق","L","R","din","р.","$","₣","ر.س","$","₨","£","kr","£","£","£","Le","Sh","$","Db","ل.س","L","฿","ЅМ","m","د.ت","T$","₤","₤","$","$","Sh","₴","Sh","$","R","$","$","B/.","$","$","$","$","$","$","$","$","$","$","$","so'm","Bs F","₫","Vt","T","₣","₣","₣","₣","₣","FCFA","₣","₣","₣","₣","₣","₣","₣","₣","$","$","$","$","$","$","$","$","₣","₣","₣","﷼","ლ","р.","R","ZK","$n"};
    public static final String DATABASE_NAME = "Currency.db";
    public static final String TABLE_NAME = "Item_Table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "Country";
    public static final String COL_3 = "Symbol";
    public static final String COL_4 = "Selected_Currency";
    public DatabaseCurrency(Context context) {
        super(context, DATABASE_NAME, null, 7);
        /*SQLiteDatabase db = this.getWritableDatabase();*/

    }
    public void InitialData(){
        if (Symbol.length==Country.length)
            for (int i=0;i<Symbol.length;i++) {
                insertData(Country[i],Symbol[i]);
            }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,Country TEXT,Symbol TEXT,Selected_Currency TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public void insertData(String Country,String Symbol) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,Country);
        contentValues.put(COL_3,Symbol);
        contentValues.put(COL_4,"Null");
        db.insert(TABLE_NAME, null, contentValues);
    }
    public void updateData(String Selected_Currency) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor res,result=null;
        result = getSelectedSymbol(Selected_Currency);
        result.moveToNext();
        contentValues.put(COL_4,result.getString(2));
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { "1" });
    }
    public Cursor getSelectedData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res=null;
        res = db.rawQuery("select * from "+TABLE_NAME+ " WHERE "+COL_1+"= ?"
                ,new String[] {"1"});
        return res;
    }
    public Cursor getSelectedSymbol(String Selected_Currency) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res,result=null;
        res = db.rawQuery("select * from "+TABLE_NAME+ " WHERE "+COL_2+"= ?"
                ,new String[] {Selected_Currency});
        return res;
    }
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_NAME,null);
    }
}
