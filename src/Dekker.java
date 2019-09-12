public final class Dekker {
    
    private static Dekker instance;
    private Boolean[] arrayForCannibalTurns;
    private Integer turn;

    private Dekker() {
        this.arrayForCannibalTurns = new Boolean[CannibalsFeast.CANNIBALS];
        for(int i = 0; i < CannibalsFeast.CANNIBALS; i++) {
            this.arrayForCannibalTurns[i] = false;
        }
        this.turn = 0;
    }

    public synchronized static Dekker construct() {
        if (instance == null) {
            instance = new Dekker();
        }
        return instance;
    }

    public synchronized void updateArray(int pos) {
        this.arrayForCannibalTurns[pos] = true;
        this.turn = pos;
    }

    public synchronized Integer getTurn() {
        return this.turn;
    }
}