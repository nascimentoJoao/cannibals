package logical;

import java.util.concurrent.*;

public class Filosofo {

	static int n_philosofers = 6;
	static volatile int [] state = new int[n_philosofers];
	static int THINKING = 0;
	static int HUNGRY = 1;
	static int EATING = 2;

	static class filosofo implements Runnable {
		Semaphore mutex;
		Semaphore [] s;
		private int tid;

		public filosofo(int id, Semaphore mtx, Semaphore [] sem) {
			this.tid = id;
			mutex = mtx;
			s = sem;
		}

		public int left(int i) {
			return (i + n_philosofers - 1) % n_philosofers;
		}

		public int right(int i) {
			return (i + 1) % n_philosofers;
		}

		public void test(int i, Semaphore [] sem){
			if (state[i] == HUNGRY && state[left(i)] != EATING && state[right(i)] != EATING){
				state[i] = EATING;
				sem[i].release();
			}
		}

		public void take_forks(int i, Semaphore mtx, Semaphore [] sem) {
			try {
				mtx.acquire();
				state[i] = HUNGRY;
				test(i, sem);
				mtx.release();
				sem[i].acquire();
			} catch (InterruptedException e) {
			}
		}

		public void put_forks(int i, Semaphore mtx, Semaphore [] sem) {
			try {
				mtx.acquire();
				state[i] = THINKING;
				test(left(i), sem);
				test(right(i), sem);
				mtx.release();
			} catch (InterruptedException e) {
			}
		}

		public void run(){
			while (true) {
				try {
					System.out.println("thread " + tid + " thinking...");
					Thread.sleep(100);
					System.out.println("thread " + tid + " hungry...");
					take_forks(tid, mutex, s);
					System.out.println("thread " + tid + " eating...");
					Thread.sleep(100);
					put_forks(tid, mutex, s);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public static void main(String[] args) {
		Semaphore mutex = new Semaphore(1);
		Semaphore [] s = new Semaphore [n_philosofers];
		int i;

		for (i = 0; i < n_philosofers; i++) {
			state[i] = THINKING;
			s[i] = new Semaphore(0);
		}

		for (i = 0; i < n_philosofers; i++) {
			Thread t = new Thread(new filosofo(i, mutex, s));
			t.start();
		}
	}
	
}
