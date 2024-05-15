package server.sharedregions;

/**
 * Possible states of the coach.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public enum EGeneralRepository_Coach {
    /**
     * Wait for referee command state.
     */
    WAIT_FOR_REFEREE_COMMAND("001"),
    /**
     * Assemble team state.
     */
    ASSEMBLE_TEAM("002"),
    /**
     * Watch trial state.
     */
    WATCH_TRIAL("003"),
    ;
    /**
     * The label of the state.
     */
    public final String label;

    /**
     * Constructor for the coach state.
     *
     * @param label the label of the state
     */
    EGeneralRepository_Coach(String label) {
        this.label = label;
    }
}
