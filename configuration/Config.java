package configuration;

/**
 * Definition of the simulation configuration.
 *
 * @author Diogo Paiva (103183)
 * @author João Fonseca (103154)
 * @version 1.0
 */
public final class Config {
    /**
     * Number of games per match.
     */
    public final static int N_GAMES_PER_MATCH = 3;
    /**
     * Number of trials per game.
     */
    public final static int N_TRIALS_PER_GAME = 6;
    /**
     * Number of contestants per team.
     */
    public final static int N_CONTESTANTS_PER_TEAM = 5;
    /**
     * Number of contestants per trial.
     */
    public final static int N_CONTESTANTS_PER_TRIAL = 3;
    /**
     * Maximum strength of a contestant.
     */
    public final static int MAX_STRENGTH = 5;
    /**
     * Maximum sleep time of a contestant in milliseconds.
     */
    public final static int MAX_SLEEP_MS = 250;
    /**
     * Folder where the logs are stored.
     */
    public final static String LOGS_FOLDER = "logs";

    /**
     * It can not be instantiated.
     */
    private Config() {
    }
}
