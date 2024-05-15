package server.sharedregions;

/**
 * Possible states of the referee.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public enum EGeneralRepository_Referee {
    /**
     * Start of the match state.
     */
    START_OF_THE_MATCH("001"),
    /**
     * Start of a game state.
     */
    START_OF_A_GAME("002"),
    /**
     * Teams ready state.
     */
    TEAMS_READY("003"),
    /**
     * Wait for trial conclusion state.
     */
    WAIT_FOR_TRIAL_CONCLUSION("004"),
    /**
     * End of a game state.
     */
    END_OF_A_GAME("005"),
    /**
     * End of the match state.
     */
    END_OF_THE_MATCH("006"),
    ;
    /**
     * The label of the state.
     */
    public final String label;

    /**
     * Constructor for the referee state.
     *
     * @param label the label of the state
     */
    EGeneralRepository_Referee(String label) {
        this.label = label;
    }
}
