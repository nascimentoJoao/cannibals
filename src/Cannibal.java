import java.util.concurrent.Semaphore;

public class Cannibal implements Runnable {
	private int id;
	private Semaphore tableStatus;
	private Dekker dekker;

	public Cannibal(int cannibalId, Semaphore tableStatus, Dekker dekker) {
		this.id = cannibalId;
		this.tableStatus = tableStatus;
		this.dekker = dekker;
	}

	public void eat() {
		try {

			System.out.println("Cannibal " + this.id + " gets closer to the table.\n");
			Thread.sleep(500);
			this.dekker.dekkerAcquire(this.id);
			
			System.out.println("Cannibal " + this.id + " is eating...\n");
			//System.out.println("## There is " + CannibalsFeast.BODY_PARTS + " body part(s) left to eat.##\n");
			
		} catch (Exception err) {
			System.out.println(err.getMessage());
		}

	}

	public void wakeUpCook() {
		try {
			this.tableStatus.acquire();
			System.out.println("Cannibal " + this.id + " wakes up Jack The Ripper.\n");
		} catch (Exception err) {
			System.out.println(err.getMessage());
		}

	}

	private boolean hasFood() {
		return CannibalsFeast.BODY_PARTS > 0;
	}

	private boolean canEat() {
		return this.id == this.dekker.getTurn() && this.tableStatus.availablePermits() > 0;
	}

	@Override
	public void run() {
		while (true) {
			if (hasFood() && canEat()) {
				eat();
			} else if (hasFood() && !canEat()) {
				this.dekker.updateArray(this.id);
			} else if (!hasFood() && canEat()) {
				wakeUpCook();
			}
		}
	}
}
