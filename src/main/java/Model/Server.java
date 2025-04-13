package Model;

import BusinessLogic.SimulationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private List<Task> history;

    public Server() {
        this.tasks = new LinkedBlockingQueue<>();
        this.waitingPeriod = new AtomicInteger(0);
        this.history = new ArrayList<>();
    }

    public void addTask(Task task) {
            tasks.add(task);
            waitingPeriod.addAndGet(task.getServiceTime());
            synchronized(history) {
                history.add(task);
            }
    }

    @Override
    public void run(){
        while(true){
            try {
                Task t = tasks.take();
                t.setStartServiceTime(SimulationManager.getCurrentGlobalTime());
                Thread.sleep(t.getServiceTime() * 1000);
                waitingPeriod.addAndGet(-t.getServiceTime());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

        }
    }

    public Task[] getTasks(){
        return tasks.toArray(new Task[0]);
    }
    public int getWaitingPeriod(){
        return waitingPeriod.get();
    }
    public void setWaitingPeriod(AtomicInteger waitingPeriod){
        this.waitingPeriod = waitingPeriod;
    }
    public void setTasks(BlockingQueue<Task> tasks){
        this.tasks = tasks;
    }
    public List<Task> getHistory(){
        synchronized(history) {
            return new ArrayList<>(history);
        }
    }
}
