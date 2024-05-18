package server.objects;

import interfaces.generalrepository.IGeneralRepository_Site;
import interfaces.refereesite.IRefereeSite;
import server.main.ServerRefereeSite;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Referee Site.
 */
public class RefereeSite implements IRefereeSite {
    /**
     * The lock.
     */
    private final ReentrantLock lock;
    /**
     * The condition variable for coaches waiting.
     */
    private final Condition coachesWaited;
    /**
     * The number of coaches waiting.
     */
    private int waiting;
    /**
     * The condition variable for referee command.
     */
    private final Condition refereeCommand;
    /**
     * Flag to indicate if the referee has given a command.
     */
    private boolean isRefereeCommand;
    /**
     * Flag to indicate if the match has ended.
     */
    private boolean isMatchEnd;
    /**
     * The team that won the game.
     */
    private int winTeamGame;
    /**
     * The team that won the match.
     */
    private int winTeamMatch;
    /**
     * The general repository.
     */
    private final IGeneralRepository_Site generalRepository;
    /**
     * Number of entity groups requesting the shutdown.
     */
    private int nEntities;

    /**
     * Instantiation of the referee site.
     *
     * @param generalRepository the general repository
     */
    public RefereeSite(IGeneralRepository_Site generalRepository) {
        lock = new ReentrantLock();
        coachesWaited = lock.newCondition();
        refereeCommand = lock.newCondition();
        waiting = 0;
        isRefereeCommand = false;
        isMatchEnd = false;
        winTeamGame = -1;
        winTeamMatch = -1;
        this.generalRepository = generalRepository;
        nEntities = 0;
    }

    /**
     * The coach waits for the referee command.
     *
     * @param team the team of the coach
     * @return true if the match has not ended, false otherwise
     */
    public boolean reviewNotes(int team) {
        lock.lock();
        try {
            waiting++;
            if (waiting == 2) {
                coachesWaited.signal(); // alerts referee
            }
            while (!isRefereeCommand) {
                refereeCommand.await(); // releases lock and waits for referee command
            }
            waiting--;
            if (waiting == 0) {
                isRefereeCommand = false;
            }
            generalRepository.reviewNotes(team);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return !isMatchEnd;
    }

    /**
     * The referee announces a new game.
     */
    public void announceNewGame() {
        lock.lock();
        try {
            generalRepository.announceNewGame();
        } finally {
            lock.unlock();
        }
    }

    /**
     * The referee calls the trial. The referee waits for the coaches to be ready to receive the command to call the
     * trial. The coaches will know the match has not ended.
     */
    public void callTrial() {
        lock.lock();
        try {
            while (waiting < 2) {
                coachesWaited.await(); // releases lock and waits for coaches
            }
            isMatchEnd = false;
            isRefereeCommand = true;
            generalRepository.callTrial();
            refereeCommand.signalAll(); // alerts coaches
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * The referee declares the team that won the game.
     *
     * @param team     the team that won the game
     * @param knockout true if the game was a knockout, false otherwise
     */
    public void declareGameWinner(int team, boolean knockout) {
        lock.lock();
        try {
            winTeamGame = team;
            generalRepository.declareGameWinner(team, knockout);
        } finally {
            lock.unlock();
        }
    }

    /**
     * The referee declares the team that won the match. The referee waits for the coaches to be ready to receive the
     * command to declare the match winner. The coaches will know the match has ended.
     *
     * @param team the team that won the match
     */
    public void declareMatchWinner(int team) {
        lock.lock();
        try {
            winTeamMatch = team;
            while (waiting < 2) {
                coachesWaited.await(); // releases lock and waits for coaches
            }
            waiting = 0;
            isMatchEnd = true;
            isRefereeCommand = true;
            generalRepository.declareMatchWinner(team);
            refereeCommand.signalAll(); // alerts coaches
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Operation server shutdown.
     */
    public void shutdown() {
        lock.lock();
        try {
            nEntities += 1;
            if (nEntities >= 2) {
                generalRepository.shutdown();
                ServerRefereeSite.shutdown();
            }
        } finally {
            lock.unlock();
        }
    }
}
