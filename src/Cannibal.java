import java.util.concurrent.Semaphore;

public class Cannibal implements Runnable {
    private int id;
    private Semaphore table;
    private Semaphore tableStatus;

    public Cannibal(int cannibalId, Semaphore table, Semaphore tableStatus) {
        this.id           = cannibalId;
        this.table        = table;
        this.tableStatus  = tableStatus;
    }

    public void eat() {
        System.out.println("Cannibal " + this.id +" is eating");
        try {
            Thread.sleep(500);
            this.table.acquire();
        }
        catch (Exception err) {
            System.out.println(err.getMessage());
        }
        
    }

    public void wakeUpCook() {
        try {
            this.tableStatus.acquire();
            System.out.println("Cannibal " + this.id +" wakes up Jack The Ripper");
        }
        catch (Exception err) {
            System.out.println(err.getMessage());
        }
        
    }

    private boolean hasFood() {
        return this.table.availablePermits() > 0;
    }

    private boolean canEat() {
        return this.tableStatus.availablePermits() > 0;
    }

    @Override
    public void run() {
        while (true) {
            if (hasFood() && canEat()) {
                eat();
            }
            else if (!hasFood() && canEat()) {
                wakeUpCook();
            }
        }
    }
}