package innlevering.com.javaeightwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Leo-Andreas Ervik
 */
public class LakeDB {

    public static List<Lake> lakes = new ArrayList<>();

    static {
        lakes.add(new Lake("Caspian Sea", Arrays.asList("Kazakhstan", "Russia", "Turkmenistan", "Azerbaijan", "Iran"), "Asia", 436000.0, 1199.0, 1025.0));
        lakes.add(new Lake("Superior", Arrays.asList("Canada", "United States"), "North America", 82100.0, 616.0, 406.3));
        lakes.add(new Lake("Victoria", Arrays.asList("Uganda", "Kenya", "Tanzania"), "Africa", 68870.0, 322.0, 84.0));
        lakes.add(new Lake("Huron", Arrays.asList("Canada", "United States"), "North America", 59600.0, 332.0, 229.0));
        lakes.add(new Lake("Michigan", Collections.singletonList("United States"), "North America", 58000.0, 494.0, 281.0));
        lakes.add(new Lake("Tanganyika", Arrays.asList("Burundi", "Democratic Republic of the Congo", "Zambia", "Tanzania"), "Africa", 32600.0, 676.0, 1470.0));
        lakes.add(new Lake("Baikal", Collections.singletonList("Russia"), "Asia", 31500.0, 636.0, 1637.0));
        lakes.add(new Lake("Great Bear Lake", Collections.singletonList("Canada"), "North America", 31000.0, 373.0, 446.0));
        lakes.add(new Lake("Malawi", Arrays.asList("Malawi", "Tanzania", "Mozambique"), "Africa", 29500.0, 579.0, 706.0));
        lakes.add(new Lake("Great Slave Lake", Collections.singletonList("Canada"), "North America", 27000.0, 480.0, 614.0));
        lakes.add(new Lake("Erie", Arrays.asList("Canada", "United States"), "North America", 25700.0, 388.0, 64.0));
        lakes.add(new Lake("Winnipeg", Collections.singletonList("Canada"), "North America", 24514.0, 425.0, 36.0));
        lakes.add(new Lake("Ontario", Arrays.asList("Canada", "United States"), "North America", 18960.0, 311.0, 244.0));
        lakes.add(new Lake("Ladoga", Collections.singletonList("Russia"), "Europe", 18130.0, 219.0, 230.0));
        lakes.add(new Lake("Balkhash", Collections.singletonList("Kazakhstan"), "Asia", 16400.0, 605.0, 26.0));
        lakes.add(new Lake("Vostok", Collections.singletonList("Antarctica"), "Antarctica", 12500.0, 250.0, 9001000.0));
        lakes.add(new Lake("Onega", Collections.singletonList("Russia"), "Europe", 9720.0, 248.0, 120.0));
        lakes.add(new Lake("Titicaca", Arrays.asList("Peru", "Bolivia"), "South America", 8372.0, 177.0, 281.0));
        lakes.add(new Lake("Nicaragua", Collections.singletonList("Nicaragua"), "North America", 8264.0, 177.0, 26.0));
        lakes.add(new Lake("Athabasca", Collections.singletonList("Canada"), "North America", 7850.0, 335.0, 243.0));
        lakes.add(new Lake("Taymyr", Collections.singletonList("Russia"), "Asia", 6990.0, 250.0, 26.0));
        lakes.add(new Lake("Turkana", Arrays.asList("Ethiopia", "Kenya"), "Africa", 6405.0, 248.0, 109.0));
        lakes.add(new Lake("Reindeer Lake", Collections.singletonList("Canada"), "North America", 6330.0, 245.0, 337.0));
        lakes.add(new Lake("Issyk-Kul", Collections.singletonList("Kyrgyzstan"), "Asia", 6200.0, 182.0, 668.0));
        lakes.add(new Lake("Urmia", Collections.singletonList("Iran"), "Asia", 6001.0, 130.0, 16.0));
        lakes.add(new Lake("Vänern", Collections.singletonList("Sweden"), "Europe", 5545.0, 140.0, 106.0));
        lakes.add(new Lake("Winnipegosis", Collections.singletonList("Canada"), "North America", 5403.0, 245.0, 18.0));
        lakes.add(new Lake("Albert", Arrays.asList("Uganda", "Democratic Republic of the Congo"), "Africa", 5299.0, 161.0, 58.0));
        lakes.add(new Lake("Mweru", Arrays.asList("Zambia", "Democratic Republic of the Congo"), "Africa", 5120.0, 131.0, 27.0));
        lakes.add(new Lake("Nettilling", Collections.singletonList("Canada"), "North America", 5066.0, 113.0, 132.0));
        lakes.add(new Lake("Sarygamysh Lake", Arrays.asList("Uzbekistan", "Turkmenistan"), "Asia", 5000.0, 125.0, 40.0));
        lakes.add(new Lake("Nipigon", Collections.singletonList("Canada"), "North America", 4843.0, 116.0, 165.0));
        lakes.add(new Lake("Manitoba", Collections.singletonList("Canada"), "North America", 4706.0, 225.0, 7.0));
        lakes.add(new Lake("Great Salt Lake", Collections.singletonList("United States"), "North America", 4662.0, 121.0, 10.0));
        lakes.add(new Lake("Saimaa", Collections.singletonList("Finland"), "Europe", 4400.0, 13700.0, 82.0));
        lakes.add(new Lake("Khanka", Arrays.asList("China", "Russia"), "Asia", 4190.0, 90.0, 10.6));
    }
}
