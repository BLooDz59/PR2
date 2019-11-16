package main.java;

import java.util.List;

public class StrategyManager {

    public enum Strategy {
        RUSH_QG;
    }

    private Strategy STRATEGY;
    private Graph GRAPH;

    private StrategyManager() {}

    private static StrategyManager INSTANCE = new StrategyManager();

    public static StrategyManager getInstance() { return INSTANCE; }

    public void setGraph(Graph graph) { GRAPH = graph; }

    public void setStrategy(Strategy strategy) { STRATEGY = strategy; }

    public void run() {
        if (STRATEGY == Strategy.RUSH_QG) {
            List<Pod> pods = PodsManager.getInstance().getPods();
            for(Pod p : pods) {
                if(!p.hasPath() && !p.hasReachedPath()) {
                    p.setTarget(GRAPH.getEnemyQG());
                }
            }
        }
    }

}
