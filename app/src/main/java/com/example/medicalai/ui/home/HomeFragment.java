package com.example.medicalai.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.medicalai.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import static com.example.medicalai.MainActivity.HOST;

public class HomeFragment extends Fragment {

    public static int out_fragm = 0; // 0 - Root, 1 - result
    public static int fragm = 0;

    public static View result;

    public static int lastFrag;
    public static View manual, profile, disease, root, last, gallery,bu_gallery;

    public static ViewGroup cont;

    private String SERVER = HOST;
    private final int CAMERA_CODE = 10, GALLERY_CODE = 11;
    FragmentActivity myContext;
    Button add;
    public static LinearLayout linearLayout;


    BarChart barChart;
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        cont = container;
        // Initializing the fragments

        root = inflater.inflate(R.layout.fragment_home,container,false);
        result = inflater.inflate(R.layout.fragment_output,null,false);
        disease = inflater.inflate(R.layout.fragment_disease,container,false);
        manual = inflater.inflate(R.layout.fragment_manual,null,false);
        profile = inflater.inflate(R.layout.fragment_profile,null,false);
        gallery = inflater.inflate(R.layout.fragment_gallery,null,false);
//        disease.getRootView().setVisibility(View.INVISIBLE);

        barChart = root.findViewById(R.id.barchart);
        showBarChart();
        return root;

    }
    public void showBarChart(){
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0, 2093876));
        barEntries.add(new BarEntry(1, 2088849));
        barEntries.add(new BarEntry(2, 1800977));
        barEntries.add(new BarEntry(3, 1276106));
        barEntries.add(new BarEntry(4, 1033701));
        barEntries.add(new BarEntry(5, 841080));
        barEntries.add(new BarEntry(6, 572034));
        barEntries.add(new BarEntry(7, 569947));
        barEntries.add(new BarEntry(8, 567233));
        barEntries.add(new BarEntry(9, 549393));
        barEntries.add(new BarEntry(10, 509590));
        barEntries.add(new BarEntry(11, 458918));
        barEntries.add(new BarEntry(12, 437033));
        barEntries.add(new BarEntry(13, 403262));
        barEntries.add(new BarEntry(14, 354864));
        barEntries.add(new BarEntry(15, 296851));
        barEntries.add(new BarEntry(16, 287723));
        BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");
        ArrayList<String> theDates = new ArrayList<>();
        theDates.add("Lung");
        theDates.add("Breast");
        theDates.add("Colorectal");
        theDates.add("Prostate");
        theDates.add("Stomach");
        theDates.add("Liver");
        theDates.add("Esophagus");
        theDates.add("Uteri");
        theDates.add("Thyroid");
        theDates.add("Bladder");
        theDates.add("Lymphoma");
        theDates.add("Pancreas");
        theDates.add("Leukaemia");
        theDates.add("Kidney");
        theDates.add("Oral");
        theDates.add("Brain");
        theDates.add("Melanoma");


        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(theDates));
        BarData theData = new BarData(barDataSet);

        barChart.setData(theData);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setVisibleXRangeMaximum(6);
        barChart.setDrawGridBackground(false);
        barChart.getDescription().setEnabled(false);
        barDataSet.setValueTextSize(8f);
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barChart.getLegend().setEnabled(false);
        barChart.setScaleEnabled(false);
    }
}




