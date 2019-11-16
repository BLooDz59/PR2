package main.java;

public class Command {
    private String command;

    public Command(){ command = ""; }

    /**
     * Get the current value of the command
     * If the command is empty, the method return "WAIT" to avoid bugs in the game
     * @return String the value of the command
     */
    public String getCommand() {
        if(command.equals("")){
            return "WAIT";
        }
        else {
            return command;
        }
    }

    /**
     * Test if the command that will be created will be valid according to the game rules.
     * @param podsCount
     * @param zoneOrigin
     * @param zoneDest
     * @param playerID
     * @return True if the command will be valid, False if not
     */
    public boolean isValidCommand(int podsCount, Node zoneOrigin, Node zoneDest, int playerID) {
        boolean ret = true;
        if(!zoneOrigin.getLinkedNodes().contains(zoneDest)){
            ret = false;
        }
        else if(zoneOrigin.getEnemyPodsNumber() > 0 && zoneDest.getOwnerID() != playerID){
            ret = false;
        }
        return ret;
    }

    /**
     * Add the command created with the parameters to the global command
     * @param podsCount
     * @param zoneOrigin
     * @param zoneDest
     */
    public void addCommand(int podsCount, int zoneOrigin, int zoneDest){
        command += String.format("%d %d %d ", podsCount, zoneOrigin, zoneDest);
    }

    /**
     * Reset the value of the command
     */
    public void reset() { command = ""; }
}
