package Model;

public class Task {
    private int arrivalTime;
    private int serviceTime;
    private int id;
    private int startServiceTime = -1;

    public Task(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getArrivalTime() {
        return arrivalTime;
    }
    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    public int getServiceTime() {
        return serviceTime;
    }
    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }
    public int getStartServiceTime() {
        return startServiceTime;
    }
    public void setStartServiceTime(int startServiceTime) {
        this.startServiceTime = startServiceTime;
    }
    public int getWaitingTime(){
        if(startServiceTime == -1){
            return 0;
        }
        return startServiceTime - arrivalTime;
    }

    @Override
    public String toString() {
        return "(" + id + ", " + arrivalTime + ", " + serviceTime + ")";
    }
}
