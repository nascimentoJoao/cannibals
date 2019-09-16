import java.util.concurrent.Semaphore;

public class Cannibal implements Runnable {
    private int id;
    private Semaphore table;
    private Semaphore tableStatus;
    private Dekker dekker;

    public Cannibal(int cannibalId, Semaphore table, Semaphore tableStatus, Dekker dekker) {
        this.id           = cannibalId;
        this.table        = table;
        this.tableStatus  = tableStatus;
        this.dekker = dekker;
    }

    public void eat() {
        System.out.println("Cannibal " + this.id +" is eating");
        try {
            Thread.sleep(500);
            this.dekker.dekkerAcquire(this.id);
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
        return this.id == this.dekker.getTurn()
            && this.tableStatus.availablePermits() > 0;
    }

    @Override
    public void run() {
        while (true) {
            if (hasFood() && canEat()) {
                eat();
            } else if (hasFood() && !canEat()) {
            	this.dekker.updateArray(this.id);
            }
            else if (!hasFood() && canEat()) {
                wakeUpCook();
            }
        }
    }
}