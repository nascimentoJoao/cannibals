package logical;

import java.util.concurrent.Semaphore;

public class CannibalProblem {
	
	static int CANNIBALS = 5;
	static int BODY_PARTS = 5;
	static volatile boolean JACK_IS_KILLING = true;
	static volatile boolean JACK_IS_SLEEPING = false;
	
	static volatile int[] state = new int[CANNIBALS];
	
	static volatile Semaphore DINNER_TABLE = new Semaphore(0);
	static volatile Semaphore DINNER_STATUS = new Semaphore(1);
	
	static String[] PARTS = {"Hand", "Leg", "Toe", "Torso", "Head", "Foot"};
	
	public static class Cannibal implements Runnable {
		
		public static int id;
		
		public Cannibal(int id) {
			this.id = id;
		}

		public void take_body_part(Semaphore mtx, Semaphore table_status) {
			try {
				System.out.println("The cannibal is taking a body part.");
				Thread.sleep(2000);
				mtx.acquire();
				System.out.println("The cannibal is eating a body part.");
				if(mtx.availablePermits() == 0) {
					wake_up_jack(table_status);
				}
			} catch (InterruptedException e) { }
		}
		
		public void wake_up_jack(Semaphore table_status) {
			try {
				table_status.acquire();
			} catch (InterruptedException e) { }
		}
		
		
		@Override
		public void run() {
			System.out.println("Beware, the cannibal " + id + " is eating.");
			take_body_part(DINNER_TABLE, DINNER_STATUS);
		}
		
	}
	
	 public static class JackTheRipper implements Runnable {
		
		public void fill_up_table(Semaphore mtx, Semaphore table_status) {
			while(BODY_PARTS > mtx.availablePermits()) {
				try {
					Thread.sleep(3000);
					System.out.println("Victim was quartered off! Yay!");
					Thread.sleep(2000);
					mtx.release();
					System.out.println("A part of it was served. Dinner almost ready!\n");
					Thread.sleep(2000);
				} catch (InterruptedException e) { }
			}
			System.out.println("Dinner is now ready!\n");
			table_status.release();
		}

		@Override
		public void run() {
			while(JACK_IS_KILLING) {
				try {
					System.out.println("Jack is ripping off some victims...\n");
					Thread.sleep(5000);
					fill_up_table(DINNER_TABLE, DINNER_STATUS);
				} catch (InterruptedException e) { }
			}
		}
		
	}
	
	public static void main (String[] args) {
		/** 
		 * Basic steps:
		 * 1. Table is full.
		 * 2. Jack is asleep.
		 * 3. Cannibals start eating.
		 * 4. Table is empty.
		 * 5. Cannibal wakes up Jack.
		 * 6. All starving cannibals wait up until Jack fills up the table.
		 * 7. Jack finishes his job.
		 * 8. All cannibals start eating again.
		 * 
		 * */
		Thread jack = new Thread(new JackTheRipper());
		jack.start();
		
		for(int i = 0; i < CANNIBALS; i++) {
			Thread cannibal = new Thread(new Cannibal(i));
			cannibal.start();
		}
	}

}