import java.util.ArrayList;

public class Mutex {

    private Cannibal[] waiting;
    private Cannibal selected;
    private int lastPermissionGiven;
    private static Mutex instance;

    private Mutex() {
        this.selected               = null;
        this.waiting                = new Cannibal[Feast.CANNIBALS_AMOUNT];
        this.lastPermissionGiven    = -1;
    }

    public synchronized static Mutex construct() {
        if (instance == null) {
            instance = new Mutex();
        }
        return instance;
    }
    
    public void subscribe(Cannibal cannibal) {
        // Registra a intenção de comer
        if (waiting[cannibal.getId()] == null) {
            // System.out.println("Cannibal "+cannibal.getId()+ " subscribed");
            waiting[cannibal.getId()]   = cannibal;
        }
        // Se só tem eu esperando na fila, Libera o turno pra mim mesmo
        if (getWaitingLineLength() == 1 && this.waiting[cannibal.getId()] != null) {
            cannibal.setPermissionToEat(true);
        }
    }

    public void unsubscribe(Cannibal cannibal) {
        // Altera a permissão do 'cannibal' para false;
        cannibal.setPermissionToEat(false);
        // Remove o canibal do array de 'waiting'
        waiting[cannibal.getId()] = null;
        // Armazena o indice do último canibal a ter permissão concedida
        this.lastPermissionGiven = cannibal.getId();
        // Altera a permissão do próximo canibal para true;
        // System.out.println("Canibal " +cannibal.getId()+ " unsubscribed");
        next();
    }

    private void next() {
        for(int i = this.lastPermissionGiven; i < this.waiting.length - 1; i++) {
            if (this.waiting[i] != null) {
                // System.out.println("Cannibal " + this.waiting[i].getId() + " is next." );
                this.waiting[i].setPermissionToEat(true);
            }
        }
    }

    private int getWaitingLineLength() {
        int count = 0;
        for (int i = 0; i < this.waiting.length - 1; i++) {
            if (this.waiting[i] != null) {
                count += 1;
            }
        }
        return count;
    }


}