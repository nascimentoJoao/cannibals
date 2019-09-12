import java.util.concurrent.Semaphore;

public class JackTheRipper implements Runnable {
    
    private static JackTheRipper instance;
    
    private Semaphore table;
    private Semaphore tableStatus;
    private int maxBodyParts;

    private JackTheRipper(Semaphore table, Semaphore tableStatus, int maxBodyParts) {
        this.table        = table;
        this.tableStatus  = tableStatus;
        this.maxBodyParts = maxBodyParts;
    }

    public synchronized static JackTheRipper construct(Semaphore table, Semaphore tableStatus, int maxBodyParts) {
        if (instance == null) {
            instance = new JackTheRipper(table, tableStatus, maxBodyParts);
        }
        return instance;
    }

    public void fillUpTable() {
        int count = 1;
        while (count <= this.maxBodyParts) {
            System.out.println("Jack is filling up the table: " + count + " of " + this.maxBodyParts);
            this.table.release();
            count++;
        }
        System.out.println("Dinner is now served, you filthy hungry bastards!");
        this.tableStatus.release();
    }

    private boolean canWakeUp() {
        return this.tableStatus.availablePermits() <= 0;
    }

    @Override
    public void run() {
        while(true) {
            if (canWakeUp()) {
                System.out.println();
                fillUpTable();
                System.out.println();
                this.tableStatus.release();
            }
        }
    }
}