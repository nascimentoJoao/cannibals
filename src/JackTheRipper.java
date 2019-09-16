import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class JackTheRipper implements Runnable {

	private static JackTheRipper instance;

	private Semaphore tableStatus;
	private int maxBodyParts;
	private Dekker dekker;

	private JackTheRipper(Semaphore tableStatus, int maxBodyParts, Dekker dekker) {
		this.tableStatus = tableStatus;
		this.maxBodyParts = maxBodyParts;
		this.dekker = dekker;
	}

	public synchronized static JackTheRipper construct(Semaphore tableStatus, int maxBodyParts, Dekker dekker) {
		if (instance == null) {
			instance = new JackTheRipper(tableStatus, maxBodyParts, dekker);
		}
		return instance;
	}

	public void fillUpTable() {

		int count = 1;
		while (count <= this.maxBodyParts) {
			System.out.println("Jack is filling up the table: " + count + " of " + this.maxBodyParts);
			CannibalsFeast.BODY_PARTS++;
			count++;
		}
		System.out.println("Dinner is now served, you filthy hungry bastards!\n");

		this.dekker.changeTurn(this.dekker.getPriorityQueue().poll());

		this.tableStatus.release();
	}

	private boolean canWakeUp() {
		return this.tableStatus.availablePermits() <= 0;
	}

	@Override
	public void run() {
		while (true) {
			if (canWakeUp()) {
				System.out.println();
				fillUpTable();
				System.out.println();
				this.tableStatus.release();
			}
		}
	}
}
