package main.java;

import java.util.ArrayList;
import java.util.List;

public class StrategyManager {

    private Strategy STRATEGY;
    private Graph MAP;
    private int MY_ID;

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
        if(STRATEGY == Strategy.RUSH_HQ_SMART) {
            for (Pod p : PodsManager.getInstance().getPods()) {
                if(p.getQuantity() >= 10) {
                    PodsManager.getInstance().splitPod(p, 10);
                }
            }
        }
        else if(STRATEGY == Strategy.RULES) {
            for (Pod p : PodsManager.getInstance().getPods()) {
                int factor = 0;
                List<Node> neighboursWithPlatinium = p.getNodeOn().getLinkedNodesWithPlatinium();
                List<Node> neighboursWithPlatiniumDontBelongPlayer = new ArrayList<>();
                for (Node n : neighboursWithPlatinium) {
                    if (n.getOwnerID() != MY_ID) {
                        neighboursWithPlatiniumDontBelongPlayer.add(n);
                    }
                }
                for (Node n : p.getNodeOn().getLinkedNodes()) {
                    if(n.getOwnerID() != MY_ID && n.getPlatinumProduction() == 0) {
                        factor++;
                    }
                }
                PodsManager.getInstance().splitPod(p, neighboursWithPlatiniumDontBelongPlayer.size() + factor);
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
        else if(STRATEGY == Strategy.RULES) {
            for (Pod p : PodsManager.getInstance().getPodsWithoutTarget()) {
                p.setTarget(MAP.getEnemyQG());
                p.setCanBeMerged(false);
            }
            for (Pod p : PodsManager.getInstance().getPods()) {
                List<Node> dest;
                List<Node> possibleDest = p.getNodeOn().getLinkedNodes();
                dest = getEnemyNodes(possibleDest);
                if(!dest.isEmpty()) dest = getMaxPlatiniumProduction(dest);
                if(!dest.isEmpty()) {
                    p.setTarget(dest.get(0));
                }
                else {
                    dest = getMaxPlatiniumProduction(possibleDest);
                    if (!dest.isEmpty()) dest = getNodesNotTargeted(dest);
                    if (!dest.isEmpty()) dest = getNeutralNodes(dest);
                    if(!dest.isEmpty()){
                        p.setTarget(dest.get(0));
                    }
                    else {
                        dest = getNeutralNodes(possibleDest);
                        if(!dest.isEmpty()) dest = getNodesNotTargeted(dest);
                        if (!dest.isEmpty()) {
                            p.setTarget(dest.get(0));
                        }
                        else {
                            dest = getNodeWithEnemy(possibleDest);
                            if(!dest.isEmpty()) {
                                p.setTarget(dest.get(0));
                            }
                            else {
                                p.setTarget(MAP.getEnemyQG());
                            }
                        }
                        p.setCanBeMerged(true);
                    }
                }
            }
            Pod biggestPod = PodsManager.getInstance().getMaxQuantityPod();
            biggestPod.setTarget(MAP.getEnemyQG());
        }
    }

    private List<Node> getEnemyNodes(List<Node> nodes){
        ArrayList<Node> ret = new ArrayList<>();
        for (Node n : nodes) {
            if (n.getOwnerID() != MY_ID && n.getOwnerID() != -1) {
                ret.add(n);
            }
        }
        return ret;
    }

    private List<Node> getNodeWithEnemy(List<Node> nodes){
        ArrayList<Node> ret = new ArrayList<>();
        for (Node n : nodes) {
            if (n.getEnemyPodsNumber() > 0) {
                ret.add(n);
            }
        }
        return ret;
    }

    private List<Node> getNeutralNodes(List<Node> nodes) {
        ArrayList<Node> ret = new ArrayList<>();
        for (Node n : nodes) {
            if (n.getOwnerID() == -1) {
                ret.add(n);
            }
        }
        return ret;
    }

    private List<Node> getNodesNotTargeted(List<Node> nodes) {
        ArrayList<Node> ret = new ArrayList<>();
        for (Node n : nodes) {
            if (!n.isTargeted()) {
                ret.add(n);
            }
        }
        return ret;
    }

    private List<Node> getMaxPlatiniumProduction(List<Node> nodes) {
        int production = 0;
        ArrayList<Node> ret = new ArrayList<>();
        for(Node n : nodes) {
            if(n.getPlatinumProduction() > production) {
                ret.add(n);
                production = n.getPlatinumProduction();
            }
        }
        return ret;
    }
}
