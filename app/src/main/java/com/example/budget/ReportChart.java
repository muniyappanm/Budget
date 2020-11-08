package com.example.budget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ReportChart extends AppCompatActivity {
    ArrayList<String> Label=new ArrayList<>();
    FireBaseHandler total=new FireBaseHandler();
    public ArrayList<MonthDateReport> monthDateReports=new ArrayList<>();
    public ArrayList<String> Datestring=new ArrayList<>();
    private float[] yData={1.0f,2.0f,3.2f};
    private String[] xData={"cricket","football","koko"};
    public PieChart pieChart;
    public BarChart barChart;
    TextView totalexpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Report");
        setContentView(R.layout.activity_pie_chart);
        Bundle extras = getIntent().getExtras();
        String from= extras.getString("from");
        String to= extras.getString("to");
        ReportData(from,to);
        totalexpense=findViewById(R.id.total);
        //pieChart.setDrawEntryLabels(true);
        //PieChart();
        BarCharts();
    }

    private void ReportData(String from,String to){

        Datestring.clear();
        monthDateReports.clear();
        Datestring.add(from);
        int i=0;
       do {
           if(i>32){
               Toast.makeText(ReportChart.this,"Max 31 day allowedwd OR To date can't be before From Date"
                       ,Toast.LENGTH_LONG).show();
               startActivity(new Intent(ReportChart.this,Report.class));
               break;

           }
            Datestring.add(GetAddedDate(Datestring.get(i)));
            monthDateReports.add(new MonthDateReport(Datestring.get(i),Rate(Datestring.get(i))));
            i++;
        } while (!to.equals(Datestring.get(i-1)));
        
    }

 /*   private void PieChart() {
        pieChart=(PieChart)findViewById(R.id.pie);
        pieChart.setDescription(new Description());
        pieChart.getDescription().setText("Description of my chart");
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("ok");
        pieChart.setCenterTextSize(10f);
        ArrayList<PieEntry> yEntry=new ArrayList<>();
        ArrayList<String> xEntry=new ArrayList<>();
        for (int i=0;i<yData.length;i++){
            yEntry.add(new PieEntry(yData[i],i));
        }
        for (int i=0;i<xData.length;i++){
            xEntry.add(xData[i]);

        }
        PieDataSet pieDataSet=new PieDataSet(yEntry,"sales");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

       //add colours
        ArrayList<Integer> colours =new ArrayList<>();
        colours.add(Color.BLUE);
        colours.add(Color.GRAY);
        colours.add(Color.YELLOW);

        pieDataSet.setColors(colours);

         //legend
        Legend legend=pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        //create piechart object
        PieData piedate=new PieData(pieDataSet);
        pieChart.setData(piedate);
        pieChart.invalidate();
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d("value selected",e.toString());
                Log.d("highlight selected",h.toString());
            }

            @Override
            public void onNothingSelected() {

            }
        });


    }*/
    private void Total(){
        Task<QuerySnapshot> data=total.Total();
        if(data.getResult().isEmpty()) return;
        int detail=0;
        List<String> list = new ArrayList<String>();
        for (QueryDocumentSnapshot Qdoc:data.getResult())
        {
            Map<String ,Object> Mlist= Qdoc.getData();
            if(Datestring.contains(Mlist.get("Date").toString()))
            list.add(Mlist.get("Rate").toString());
        }
        for (String x:list) {

            detail+=Integer.parseInt(x);
        }
        totalexpense.setText("Rs."+detail);
    }
    private void BarCharts(){
        barChart=(BarChart)findViewById(R.id.barchart);
        ArrayList<BarEntry> barEntries=new ArrayList<>();
        barChart.setDescription(new Description());
        barChart.getDescription().setText("Date");
        for (int i=0;i<monthDateReports.size();i++)
        {
            String month=monthDateReports.get(i).getMonth();
            int expense=monthDateReports.get(i).getExpense();
            barEntries.add(new BarEntry(i,expense));
            Label.add(month);
        }
        Total();
        BarDataSet barDataSet=new BarDataSet(barEntries,"Daily Expenses in Rs");
        barDataSet.setBarBorderWidth(1f);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextSize(16f);
        Description description=new Description();
        description.setText("Date");
        barChart.setDescription(description);
        BarData Data = new BarData(barDataSet);
        barChart.setData(Data);
        barChart.setTouchEnabled(true);
        XAxis xAxis=barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(Label));
        xAxis.setDrawAxisLine(false);
        xAxis.setTextSize(16f);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(270f);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(Label.size());
        barChart.animateY(2000);
        barChart.invalidate();
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                String month=monthDateReports.get((int) e.getX()).getMonth();
                Task<QuerySnapshot> data=total.View(month);
                if(data.getResult().isEmpty()) return;
                String detail="";
                List<String> list = new ArrayList<String>();
                for (QueryDocumentSnapshot Qdoc:data.getResult())
                {
                    Map<String ,Object> Mlist= Qdoc.getData();
                    list.add(Mlist.get("Item").toString()+"=Rs."+Mlist.get("Rate").toString()+" ");
                }
                for (String x:list) {

                    detail+=x;
                }
                Toast.makeText(ReportChart.this,detail,Toast.LENGTH_SHORT).show();
                Log.d("value selected",detail);
                Log.d("highlight selected",h.toString());
            }

            @Override
            public void onNothingSelected() {

            }
        });



    }
    public String  GetAddedDate(String Date){

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        try{
            //Setting the date to the given date
            c.setTime(sdf.parse(Date));
        }catch(ParseException e){
            e.printStackTrace();
        }
        c.add(Calendar.DAY_OF_MONTH, 1);
        return sdf.format(c.getTime());
    }

    private int Rate(String date){
        int rate = 0;
        Task<QuerySnapshot> data=total.View(date);
        if(data.getResult().isEmpty()) return 0;
        List<String> list = new ArrayList<String>();
        for (QueryDocumentSnapshot Qdoc:data.getResult())
        {
            Map<String ,Object> Mlist= Qdoc.getData();
            list.add(Mlist.get("Rate").toString());
        }
        for (String x:list) {

            rate+= Integer.parseInt(x);
        }
       return rate;
    }
}