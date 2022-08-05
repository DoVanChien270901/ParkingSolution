package fpt.aptech.parkinggo.domain.response;

public class LoadStatusParking {
    private String parkingname;
    private int columnofrow;
    private String[] locationcode;
    private String[] codebooked;

    public LoadStatusParking() {
    }

    public String getParkingname() {
        return parkingname;
    }

    public void setParkingname(String parkingname) {
        this.parkingname = parkingname;
    }

    public int getColumnofrow() {
        return columnofrow;
    }

    public void setColumnofrow(int columnofrow) {
        this.columnofrow = columnofrow;
    }

    public String[] getLocationcode() {
        return locationcode;
    }

    public void setLocationcode(String[] locationcode) {
        this.locationcode = locationcode;
    }

    public String[] getCodebooked() {
        return codebooked;
    }

    public void setCodebooked(String[] codebooked) {
        this.codebooked = codebooked;
    }
}
