package main.java;

public class StrategyManager {

    private Strategy STRATEGY;
    private Graph MAP;
    private int MY_ID;

    private StrategyManager() {}

    private static StrategyManager INSTANCE = new StrategyManager();

    public static StrategyManager getInstance() { return INSTANCE; }

    public void setGraph(Graph graph) { MAP = graph; }

    public void setPlayerID(int id) { MY_ID = id; }

    public void setStrategy(Strategy strategy) { STRATEGY = strategy; }

    public void run() {
        if (STRATEGY == Strategy.RUSH_HQ_SMART) {
            for(Pod p : PodsManager.getInstance().getPodsWithoutTarget()) {
                p.setTarget(MAP.getEnemyQG());
            }
            for(Pod p : PodsManager.getInstance().getPods()) {
                if(!(p.getID() == PodsManager.getInstance().getFirstRushPodID())){
                    System.err.println(p.getNodeOn().getId());
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
    }

}
