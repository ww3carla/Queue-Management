package BusinessLogic;
import Model.*;
import java.util.*;

public class ConcreteStrategyTime implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task task) {
        Server bestServer = servers.get(0);

        for(Server server : servers){
            if(server.getWaitingPeriod() < bestServer.getWaitingPeriod()){
                bestServer = server;
            }
        }
        bestServer.addTask(task);

        System.out.println("Added task: " + task + " to server with waiting period: " + bestServer.getWaitingPeriod());
        System.out.println("Current queue: " + Arrays.toString(bestServer.getTasks()));
    }
}

