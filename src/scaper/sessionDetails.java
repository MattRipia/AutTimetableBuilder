package scaper;

public class sessionDetails {
    
    private String paperCode;
    private String paperName;
    private String day;
    private int    year;
    private int    streamNo;
    private float  startTime;
    private float  endTime;

    public String getPaperCode() {
        return paperCode;
    }

    public void setPaperCode(String paperCode) {
        this.paperCode = paperCode;
    }

    public String getPaperName() {
        return paperName;
    }
    
    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getStreamNo() {
        return streamNo;
    }

    public void setStreamNo(int streamNo) {
        this.streamNo = streamNo;
    }

    public float getStartTime() {
        return startTime;
    }

    public void setStartTime(float startTime) {
        this.startTime = startTime;
    }

    public float getEndTime() {
        return endTime;
    }

    public void setEndTime(float endTime) {
        this.endTime = endTime;
    }
}
