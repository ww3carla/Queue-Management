package BusinessLogic;

import java.util.ArrayList;
import java.util.List;

import Model.*;


public interface Strategy {
    public void addTask(List<Server> servers, Task task);
}
