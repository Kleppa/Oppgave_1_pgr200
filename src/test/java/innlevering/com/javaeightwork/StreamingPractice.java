package innlevering.com.javaeightwork;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamingPractice {
    public static void main(String[] arg){


    }
    public static List<Lake> startsWithC() {
        return   LakeDB.lakes.stream().filter(lake -> lake.getName().startsWith("C")).collect(Collectors.toList());
    }
    public  static List<Lake> hasMoreThanOneWord(){
        return LakeDB.lakes.stream().filter(lake -> lake.getName().contains(" ")).collect(Collectors.toList());
    }
    public static List<Lake> hasMultipleBorders(){
        return LakeDB.lakes.stream().filter(lake -> lake.getCountry().size()>1).collect(Collectors.toList());
    }
    public static List<Lake> lakesEuMoreThan10kkkm(){
        return LakeDB.lakes.stream()
                .filter(lake -> lake.getContinent().equalsIgnoreCase("Europe")&&lake.getArea()>10_000)
                .collect(Collectors.toList());
    }
    public static Lake findFirstLakeLargerThanFiveKm(){
        return LakeDB.lakes.stream().filter(lake -> lake.getArea()>5000).findFirst().get();
    }
    public static List<String> lakeNames(){
        return LakeDB.lakes.stream().map(lake -> lake.getName()).collect(Collectors.toList());
    }
//    public static List<String> upperCaseLettersInName(){
//        return LakeDB.lakes.stream().map(lake -> lake.getName()).filter();
//    }



}
