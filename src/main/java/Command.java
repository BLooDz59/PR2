package main.java;

public class Command {
    private String command;

    public Command(){ command = ""; }

    public String getCommand() { return command; }

    public void addCommand(int podsCount, int zoneOrigin, int zoneDest){
        command += String.format("%d %d %d ", podsCount, zoneOrigin, zoneDest);
    }
}
