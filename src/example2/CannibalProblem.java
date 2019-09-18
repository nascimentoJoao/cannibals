import java.util.concurrent.Semaphore;

public class CannibalProblem {

	static int CANNIBALS = 5;
	static int BODY_PARTS = 5;

	static int REFERENCE_BODY_PARTS = 0;

	static volatile Semaphore DINNER_STATUS = new Semaphore(0);
	static volatile Semaphore DINNER_TABLE = new Semaphore(BODY_PARTS);
	/*static volatile Boolean flag[] = new Boolean[2];
	

	private int dekkerDecrement() {

		return 1;	
	}

	private int dekkerIncrement() {
		return 1;
	}*/

	static class Cannibal implements Runnable {

		private int id;

		public Cannibal(int id) {
			this.id = id;
			
		}

		public void take_body_part(Semaphore dinner, Semaphore table_status) {
			try {
				System.out.println("AVAILABLE PERMITS: "+ dinner.availablePermits());

				while(true){

				if (REFERENCE_BODY_PARTS == BODY_PARTS) {
					/*System.out.println("The cannibal " + id + " is taking a body part.");
					Thread.sleep(100);*/
					dinner.acquire();
					System.out.println("The cannibal " + id + " is eating a body part.");
					REFERENCE_BODY_PARTS--;
				} else {
					wake_up_jack(DINNER_STATUS);
				}
			}	
			} catch (InterruptedException e) {
			}
		}

		public void wake_up_jack(Semaphore table_status) {
			try {
				table_status.acquire();
			} catch (InterruptedException e) {
			}
		}

		@Override
		public void run() {
			while(true) {
			take_body_part(DINNER_TABLE, DINNER_STATUS);
			}
		}
		
	}
	
	 static class JackTheRipper implements Runnable {
		 
		Semaphore DINNER_TABLE;
		
		public JackTheRipper(Semaphore table) {
			DINNER_TABLE = table;
		}
		
		public void fill_up_table(Semaphore table, Semaphore table_status) {
			while(REFERENCE_BODY_PARTS > table.availablePermits()) {
				try {
					Thread.sleep(100);
					System.out.println("Victim was quartered off! Yay!");
					Thread.sleep(100);
					table.release();
					System.out.println("A part of it was served. Dinner is almost ready!\n");
					REFERENCE_BODY_PARTS++;
					Thread.sleep(100);
				} catch (InterruptedException e) { }
			}
			System.out.println("Dinner is now ready!\n");
			table_status.release();
		}

		@Override
		public void run() {
			while(DINNER_STATUS.availablePermits() == 0) {
				try {
					System.out.println("Jack is killing some victims...\n");
					Thread.sleep(200);
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


		Thread jack = new Thread(new JackTheRipper(DINNER_TABLE));
		jack.start();


			for(int i = 0; i < CANNIBALS; i++) {
				Thread cannibal = new Thread(new Cannibal(i));
				cannibal.start();
			}
		
	}

}
