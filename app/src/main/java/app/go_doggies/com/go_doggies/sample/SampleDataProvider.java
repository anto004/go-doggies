package app.go_doggies.com.go_doggies.sample;

import java.util.ArrayList;
import java.util.List;

import app.go_doggies.com.go_doggies.model.DataItem;

/**
 * Created by anto004 on 9/11/17.
 */

public class SampleDataProvider {
    public static List<DataItem> dataItemList;

    static{
        dataItemList = new ArrayList<>();
        addItem(new DataItem(
                "94",
                "20",
                "25",
                "30",
                "27",
                "33",
                "35",
                "29",
                "39",
                "45",
                "33",
                "42",
                "55",
                "30",
                "35",
                "35",
                "40",
                "45",
                "45",
                "50",
                "55",
                "35",
                "45",
                "50",
                "42",
                "50",
                "55",
                "55",
                "65",
                "75",
                "65",
                "70",
                "85",
                "50",
                "60",
                "65",
                "55",
                "70",
                "75",
                "60",
                "80",
                "100",
                "65",
                "85",
                "125",
                "10",
                "12",
                "10",
                "6",
                "10",
                "15",
                "5",
                "5",
                "10",
                "10",
                "10",
                "10",
                "5",
                "10",
                "10"
        ));

        addItem(new DataItem(
                "105",
                "20",
                "25",
                "30",
                "27",
                "33",
                "35",
                "29",
                "39",
                "45",
                "33",
                "42",
                "55",
                "30",
                "35",
                "35",
                "40",
                "45",
                "45",
                "50",
                "55",
                "35",
                "45",
                "50",
                "42",
                "50",
                "55",
                "55",
                "65",
                "75",
                "65",
                "70",
                "85",
                "50",
                "60",
                "65",
                "55",
                "70",
                "75",
                "60",
                "80",
                "100",
                "65",
                "85",
                "125",
                "10",
                "12",
                "10",
                "6",
                "10",
                "15",
                "5",
                "5",
                "10",
                "10",
                "10",
                "10",
                "5",
                "10",
                "10"
        ));
    }

    public static void addItem(DataItem item){
        dataItemList.add(item);
    }
}
