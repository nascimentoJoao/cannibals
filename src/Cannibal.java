import java.util.concurrent.Semaphore;

public class Cannibal implements Runnable {

    private int id;
    private boolean hasPermissionToEat;

    public Cannibal(int id) {
        this.id = id;
        this.hasPermissionToEat = false;
    }

    public int getId() {
        return this.id;
    }

    public void setPermissionToEat(boolean permission) {
        this.hasPermissionToEat = permission;
    }

    public void eat() {
        System.out.println("Cannibal " + this.id + " is eating.");
    }

    private boolean tableIsServed() {
        return Feast.tableStatus.availablePermits() == 0;
    }

    @Override
    public void run() {
        while(true) {
            // Registra a minha intenção de comer
            Mutex mutex = Mutex.construct();
            mutex.subscribe(this);
            
            // Verifica se é meu turno de comer e se a mesa está totalmente servida
            while (!this.hasPermissionToEat) {
                System.out.println("Cannibal " + this.id + " is waiting for its turn.");
                try {
                    Thread.sleep(500);
                }
                catch (Exception err) {
                    System.out.println(err.getMessage());
                }
            }

            while (!tableIsServed()) {
                System.out.println("Waiting for the cook to serve more portions.");
            }
            
            Feast.portions--;

            System.out.println("Feast portions: "+ Feast.portions);
            // Se não tem mais comida, preciso acordar o cozinheiro
            if (Feast.portions == 0) {
                try {
                    Feast.tableStatus.acquire();
                    System.out.println("Cannibal " +this.id+ " wakes up the cook.");
                }
                catch (Exception err) {}
                
                System.out.println("Cannibal " +this.id+ " wakes up the cook.");
            }

            // Retira minha intenção de comer, liberando a vez para um próximo canibal
            mutex.unsubscribe(this);
            eat();
        }
    }
}