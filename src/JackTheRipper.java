import java.util.concurrent.Semaphore;

public class JackTheRipper implements Runnable {

    private static JackTheRipper instance;

    private JackTheRipper() {}

    public synchronized static JackTheRipper construct() {
        if (instance == null) {
            instance = new JackTheRipper();
        }
        return instance;
    }

    public void fillTable() {
        // int count = 1;

        while (Feast.portions <= Feast.MAX_PORTIONS) {
            System.out.println("Jack is filling up the table: " + Feast.portions + " of " +Feast.MAX_PORTIONS);
            Feast.portions++;
        }

        try {
            Feast.tableStatus.release();
        }
        catch (Exception err) {}
        
        System.out.println("Dinner is served.");
    }

    public void run() {

        while(true) {
            if (Feast.tableStatus.availablePermits() < 0) {
                fillTable();
            }
        }
    }

    

    // public void fillUpTable() {
    // int count = 1;
    // while (count <= this.maxBodyParts) {
    // System.out.println("Jack is filling up the table: " + count + " of " +
    // this.maxBodyParts);
    // this.table.release();
    // count++;
    // }
    // System.out.println("Dinner is now served, you filthy hungry bastards!");
    // this.tableStatus.release();
    // }

    // private boolean canWakeUp() {
    // return this.tableStatus.availablePermits() <= 0;
    // }

    // @Override
    // public void run() {
    // while(true) {
    // if (canWakeUp()) {
    // System.out.println();
    // fillUpTable();
    // System.out.println();
    // this.tableStatus.release();
    // }
    // }
    // }
}