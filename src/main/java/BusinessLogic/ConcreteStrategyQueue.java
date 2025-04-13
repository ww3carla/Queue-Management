package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.Arrays;
import java.util.List;

public class ConcreteStrategyQueue implements Strategy{
    @Override
    public void addTask (List<Server> servers, Task task){
        Server bestServer = servers.get(0);

        for(Server server : servers){
            if(server.getTasks().length < bestServer.getTasks().length){
                bestServer = server;
            }
        }
        bestServer.addTask(task);

        System.out.println("Added task: " + task + " to server with waiting period: " + bestServer.getWaitingPeriod());
        System.out.println("Current queue: " + Arrays.toString(bestServer.getTasks()));
    }
}
