package geo.geopointsTgBot;



public class Ggs {

    String name;
    String index;
    String mark;
    String sighType;
    String centerType;
    double[] coordinates;


    public Ggs() {
    }

    public Ggs(String name, String index, String mark,
               String sighType, String centerType, double[] coordinates) {
        this.name = name;
        this.index = index;
        this.mark = mark;
        this.sighType = sighType;
        this.centerType = centerType;
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getSighType() {
        return sighType;
    }

    public void setSighType(String sighType) {
        this.sighType = sighType;
    }

    public String getCenterType() {
        return centerType;
    }

    public void setCenterType(String centerType) {
        this.centerType = centerType;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }
}
