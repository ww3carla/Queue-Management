package BusinessLogic;

import java.util.*;
import Model.*;

public class Scheduler {
    private List<Server> servers;
    private Strategy strategy;

    public Scheduler(int nbServers) {
        servers = new ArrayList<>();

        for(int i = 0; i < nbServers; i++) {
            Server server = new Server();
            servers.add(server);
            Thread thread = new Thread(server);
            thread.start();
        }
    }

    public void changeStrategy(SelectionPolicy policy) {
        if(policy == SelectionPolicy.SHORTEST_QUEUE){
            strategy = new ConcreteStrategyQueue();
        }
        if(policy == SelectionPolicy.SHORTEST_TIME){
            strategy = new ConcreteStrategyTime();
        }
    }

    public void dispatchTask(Task task) {
        if(strategy != null){
            strategy.addTask(servers, task);
        }else{
            throw new RuntimeException("Strategy is null. Set strategy first.");
        }
    }

    public List<Server> getServers() {
        return servers;
    }
}
