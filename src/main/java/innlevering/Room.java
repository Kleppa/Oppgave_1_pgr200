package innlevering;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Kleppa on 04/09/2017.
 */
@DatabaseTable(tableName="Room")

public class Room implements DatabaseContent {
    @DatabaseField
    private String roomCode;
    @DatabaseField
    private String facilitiesSupports;
    @DatabaseField
    private int maxCapasity;

    public void setColsAndDataTypes(String colsAndDataTypes) {
        this.colsAndDataTypes = colsAndDataTypes;
    }
    @DatabaseField
    private String colsAndDataTypes;
    @Override
    public String getColsAndDataTypes() {
        return colsAndDataTypes;
    }

    @Override
    public String toString() {
        return "Room{" +
                "\"roomCode\":" + "\""+roomCode + "\""+
                ", \"facilitiesSupports\":" + "\""+ facilitiesSupports + "\""+
                ", \"maxCapasity\":" + "\""+ maxCapasity + "\""+
                ", \"roomSize\":" + "\""+roomSize + "\""+
                '}';
    }

    private String roomSize;

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getFacilitiesSupports() {
        return facilitiesSupports;
    }

    public void setFacilitiesSupports(String facilitiesSupports) {
        this.facilitiesSupports = facilitiesSupports;
    }

    public int getMaxCapasity() {
        return maxCapasity;
    }

    public void setMaxCapasity(int maxCapasity) {
        this.maxCapasity = maxCapasity;
    }

    public String getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(String roomSize) {
        this.roomSize = roomSize;
    }



}
