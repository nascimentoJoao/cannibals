import java.util.Queue;
import java.util.LinkedList;

public final class Dekker {

	private Boolean[] arrayForCannibalTurns;
	public Integer turn;
	private Queue<Integer> turn_queue;

	public Dekker() {
		this.arrayForCannibalTurns = new Boolean[CannibalsFeast.CANNIBALS];

		for (int i = 0; i < CannibalsFeast.CANNIBALS; i++) {
			this.arrayForCannibalTurns[i] = false;
		}

		this.turn_queue = new LinkedList<>();

		this.turn = 1;
	}

	public synchronized Boolean checkPosition(int pos) {
		return this.arrayForCannibalTurns[pos];
	}

	public synchronized void updateArray(int pos) {
		this.arrayForCannibalTurns[pos] = true;
		this.turn_queue.offer(pos);
	}

	public synchronized Integer getTurn() {
		return this.turn;
	}

	public synchronized void dekkerAcquire(int pos) {
		
		this.turn = this.turn_queue.poll();
		
		
			this.arrayForCannibalTurns[pos] = true;
			while (checkArray()) {
				if (this.turn != pos) {
					this.arrayForCannibalTurns[pos] = false;
					while (this.turn != pos) {
						System.out.println("entrou");
					}
					this.arrayForCannibalTurns[pos] = true;
				}

				this.turn = this.turn_queue.poll();
				this.arrayForCannibalTurns[pos] = false;
			}
			CannibalsFeast.BODY_PARTS--;
		
	}

	public synchronized Boolean checkArray() {
		boolean thereIsRoom = true;
		for (boolean value : this.arrayForCannibalTurns) {
			if (value) {
				thereIsRoom = false;
			}
		}
		return thereIsRoom;
	}

	public synchronized Boolean[] getDekkerArray() {
		return this.arrayForCannibalTurns;
	}

}