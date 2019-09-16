import java.util.concurrent.Semaphore;

public class CannibalsFeast {

	static int CANNIBALS 	= 2;
	static int BODY_PARTS = 2;

	static volatile Semaphore DINNER_STATUS = new Semaphore(1);
	static volatile Semaphore DINNER_TABLE 	= new Semaphore(BODY_PARTS);
	static volatile Dekker    DEKKER        = new Dekker();

	public static void main(String[] args) {
		/**
		 * Basic steps: 1. Table is full. 2. Jack is asleep. 3. Cannibals start eating.
		 * 4. Table is empty. 5. Cannibal wakes up Jack. 6. All starving cannibals wait
		 * up until Jack fills up the table. 7. Jack finishes his job. 8. All cannibals
		 * start eating again.
		 * 
		 */
		

		Thread jack = new Thread(JackTheRipper.construct(DINNER_TABLE, DINNER_STATUS, BODY_PARTS));
		jack.start();

		for (int i = 0; i < CANNIBALS; i++) {
			Thread cannibal = new Thread(new Cannibal(i, DINNER_TABLE, DINNER_STATUS, DEKKER));
			cannibal.start();
		}

	}

}
