package server.sharedregions;

/**
 * Possible states of the contestant.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public enum EGeneralRepository_Contestant {
    /**
     * Seat at the bench state.
     */
    SEAT_AT_THE_BENCH("001"),
    /**
     * Stand in position state.
     */
    STAND_IN_POSITION("002"),
    /**
     * Do your best state.
     */
    DO_YOUR_BEST("003"),
    ;
    /**
     * The label of the state.
     */
    public final String label;

    /**
     * Constructor for the contestant state.
     *
     * @param label the label of the state
     */
    EGeneralRepository_Contestant(String label) {
        this.label = label;
    }
}
