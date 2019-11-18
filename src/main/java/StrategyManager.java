package main.java;

import java.util.List;
import java.util.Random;

public class StrategyManager {

    private Strategy STRATEGY;
    private Graph MAP;
    private int MY_ID;

    Random r = new Random(); //TO DELETE TEST ONLY

    /**
     * Private Constructor
     * StrategyManager is use to manage the different strategy use in the game.
     */
    private StrategyManager() {}

    /**
     * Unique StrategyManager instance pre-initialized
     */
    private static StrategyManager INSTANCE = new StrategyManager();

    /**
     * Get the unique instance of StrategyManager
     * @return instance of StrategyManager
     */
    public static StrategyManager getInstance() { return INSTANCE; }

    //Getters

    public int getPlayerID() { return MY_ID; }

    //Setters

    public void setGraph(Graph graph) { MAP = graph; }

    public void setPlayerID(int id) { MY_ID = id; }

    public void setStrategy(Strategy strategy) { STRATEGY = strategy; }

    /**
     * Update event call in the game loop
     */
    public void update() {
        if(STRATEGY == Strategy.TEST) {
            for (Pod p : PodsManager.getInstance().getPods()) {
                if(p.getQuantity() >= 10) {
                    PodsManager.getInstance().splitPod(p, 10);
                }
            }
        }
    }

    /**
     * Run event call in the game loop
     */
    public void run() {
        if (STRATEGY == Strategy.RUSH_HQ_SMART) {
            for(Pod p : PodsManager.getInstance().getPodsWithoutTarget()) {
                p.setTarget(MAP.getEnemyQG());
            }
            for(Pod p : PodsManager.getInstance().getPods()) {
                if(!(p.getID() == PodsManager.getInstance().getFirstRushPodID())){
                    Node maxPlatiniumTarget = MAP.getNeighbourWithMaxPlatinium(p.getNodeOn());
                    if(maxPlatiniumTarget != null && maxPlatiniumTarget.getOwnerID() != MY_ID) {
                        p.setTarget(maxPlatiniumTarget);
                    }
                }
            }
        }
        else if (STRATEGY == Strategy.RUSH_HQ) {
            for(Pod p : PodsManager.getInstance().getPodsWithoutTarget()) {
                p.setTarget(MAP.getEnemyQG());
            }
        }
        else if(STRATEGY == Strategy.TEST) {
            for (Pod p : PodsManager.getInstance().getPodsWithoutTarget()) {
                Node dest;
                List<Node> possibleDest = p.getNodeOn().getLinkedNodes();
                //Search a neighbour node with Max Amount of platinium
                dest = getMaxPlatiniumProduction(possibleDest);
                //Search a neighbour node where an ally don't go
                if (dest == null || dest.isTargeted()) {
                    dest = PathFinding.BFSNearestEnemyOrNeutralNodeNotTargeted(p.getNodeOn());
                }
                p.setTarget(dest);
            }
        }
    }

    private Node getMaxPlatiniumProduction(List<Node> nodes) {
        int production = 0;
        Node ret = null;
        for(Node n : nodes) {
            if(n.getPlatinumProduction() > production) {
                ret = n;
                production = n.getPlatinumProduction();
            }
        }
        return ret;
    }
}
