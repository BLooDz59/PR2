package main.java;

public class Timer {

    private long time1;
    private long time2;

    public Timer(){
        time1 = System.currentTimeMillis();
    }

    public void displayDeltaTime(){
        time2 = System.currentTimeMillis();
        System.err.println(time2 - time1);
    }
}
