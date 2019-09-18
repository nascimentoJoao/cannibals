import java.util.concurrent.Semaphore;

public class Feast {

    static final int CANNIBALS_AMOUNT = 5;
    static final int MAX_PORTIONS     = 3;
    static int portions               = 3;
    static Semaphore tableStatus = new Semaphore(0);

    public static void main (String[] args) {

        Thread cook = new Thread(JackTheRipper.construct());
        cook.start();

        for (int i = 0; i < CANNIBALS_AMOUNT; i++) {
            Thread cannibal = new Thread(new Cannibal(i));
            cannibal.start();
        }

    }
}