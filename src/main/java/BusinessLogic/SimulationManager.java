package BusinessLogic;

import GUI.SimulationFrame;
import Model.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class SimulationManager implements Runnable {
    public int timeLimit;
    public int maxProcessingTime;
    public int minProcessingTime;
    public int numberOfServers;
    public int numberOfClients;
    public int minArrivalTime;
    public int maxArrivalTime;
    public SelectionPolicy selectionPolicy;

    private Scheduler scheduler;
    private SimulationFrame frame;
    private List<Task> generatedTasks;
    private BufferedWriter logWriter;
    private int maxClients = -1;
    private int peakHour = -1;
    private static int currentTime = 0;

    public SimulationManager() {
        try{
            logWriter = new BufferedWriter(new FileWriter("log2.txt"));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void setParams(int timeLimit,int minArrivalTime, int maxArrivalTime
            , int minProcessingTime, int maxProcessingTime,
                          int numberOfServers, int numberOfClients, SelectionPolicy selectionPolicy) {
        this.timeLimit = timeLimit;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minProcessingTime = minProcessingTime;
        this.maxProcessingTime = maxProcessingTime;
        this.numberOfServers = numberOfServers;
        this.numberOfClients = numberOfClients;
        this.selectionPolicy = selectionPolicy;

        scheduler = new Scheduler(numberOfServers);
        scheduler.changeStrategy(selectionPolicy);
        generateNRandomTasks();
    }

    public static int getCurrentGlobalTime(){
        return currentTime;
    }

    private void generateNRandomTasks() {

        generatedTasks = new ArrayList<>();
        Random rand = new Random();

        for(int i = 0; i < numberOfClients; i++) {
            int arrivalTime = minArrivalTime + rand.nextInt(maxArrivalTime - minArrivalTime + 1);
            int serviceTime = minProcessingTime + rand.nextInt(maxProcessingTime - minProcessingTime + 1);
            Task task = new Task(i, arrivalTime, serviceTime);
            generatedTasks.add(task);
        }

        generatedTasks.sort(Comparator.comparingInt(Task::getArrivalTime));
    }

    @Override
    public void run() {
        List<Task> waitingClients = new ArrayList<>(generatedTasks);
        int serviceTimeSum = 0;

        while(currentTime <= timeLimit) {
            log("Time: " + currentTime);

            //waiting clients
            StringBuilder waitingBuilder = new StringBuilder("Waiting clients: ");
            boolean hasWaiting = false;
            for(Task task : waitingClients) {
                if(task.getArrivalTime() > currentTime) continue;
                waitingBuilder.append(task.toString()).append(" ");
                hasWaiting = true;
            }
            if(!hasWaiting) waitingBuilder.append(" none");
            log(waitingBuilder.toString());

            //dispatch clients
            Iterator<Task> iterator = waitingClients.iterator();
            while(iterator.hasNext()) {
                Task task = iterator.next();
                if(task.getArrivalTime() == currentTime) {
                    scheduler.dispatchTask(task);
                    serviceTimeSum+= task.getServiceTime();
                    iterator.remove();
                }
            }

            //server status
            int activeClients = 0;
            int q = 1;
            for(Server server : scheduler.getServers()) {
                List<Task> tasks = List.of(server.getTasks());
                if(tasks.isEmpty()){
                    log("Queue " + q + ": closed");
                }else{
                    StringBuilder queueBuilder = new StringBuilder("Queue: " + q + ":");
                    for(Task t : tasks){
                        queueBuilder.append(t.toString()).append(" ");
                        activeClients++;
                    }
                    log(queueBuilder.toString());
                }
                q++;
            }

            if(activeClients > maxClients) {
                maxClients = activeClients;
                peakHour = currentTime;
            }

            currentTime++;
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        log("Simulation finished!");

        int totalWaitingTime = 0;
        int servedClients = 0;
        for(Server server : scheduler.getServers()) {
            for(Task task : server.getHistory()) {
                if(task.getArrivalTime() != -1){
                    int wait = task.getWaitingTime();
                    if(wait >= 0) {
                        totalWaitingTime += wait;
                        servedClients++;
                    }
                }
            }
        }

        float averageWaitingTime = serviceTimeSum == 0 ? 0 : (float) totalWaitingTime / servedClients;
        float avgServiceTime = numberOfClients == 0 ? 0 : (float)serviceTimeSum / numberOfClients;

        log("Average service time: " + String.format("%.2f", avgServiceTime));
        log("Average waiting time: " + String.format("%.2f", averageWaitingTime));
        log("Peak hour: " + peakHour);
        try{
            logWriter.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void log(String s) {
        System.out.println(s);
        try{
            if(logWriter != null) {
                logWriter.write(s);
                logWriter.newLine();
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        if(frame != null) {
            frame.appendToOutput(s);
        }
    }

    public void setFrame(SimulationFrame frame) {
        this.frame = frame;
    }

    public static void main(String[] args) {
        SimulationManager gen = new SimulationManager();
        Thread thread = new Thread(gen);
        thread.start();
    }
}
