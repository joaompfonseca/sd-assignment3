package server.objects;

import interfaces.generalrepository.IGeneralRepository;
import interfaces.generalrepository.IGeneralRepository_Playground;
import interfaces.playground.IPlayground;
import server.main.ServerPlayground;

import java.rmi.RemoteException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Playground.
 */
public class Playground implements IPlayground {
    /**
     * Representation of team specific data.
     */
    private static class TeamData {
        /**
         * The condition variable for trial ready.
         */
        private final Condition trialReady;
        /**
         * The number of contestants ready.
         */
        private int contestantsReady;

        /**
         * Instantiation of team specific data.
         *
         * @param lock the lock from the playground
         */
        public TeamData(ReentrantLock lock) {
            trialReady = lock.newCondition();
            contestantsReady = 0;
        }
    }

    /**
     * The number of contestants per trial.
     */
    private final int contestantsPerTrial;
    /**
     * The team specific data.
     */
    private final TeamData[] teamData = new TeamData[2];
    /**
     * The lock.
     */
    private final ReentrantLock lock;
    /**
     * The condition variable for referee informed.
     */
    private final Condition refereeInformed;
    /**
     * The number of coaches informed.
     */
    private int countInformed;
    /**
     * The condition variable for trial started.
     */
    private final Condition trialStarted;
    /**
     * Flag to indicate if the trial has started.
     */
    private boolean isTrialStarted;
    /**
     * The rope position.
     */
    private int ropePosition;
    /**
     * The condition variable for trial ended.
     */
    private final Condition trialEnded;
    /**
     * The number of contestants done.
     */
    private int contestantsDone;
    /**
     * The condition variable for trial decided.
     */
    private final Condition trialDecided;
    /**
     * Flag to indicate if the trial has been decided.
     */
    private boolean isTrialDecided;
    /**
     * The general repository.
     */
    private final IGeneralRepository_Playground generalRepository;
    /**
     * Number of entity groups requesting the shutdown.
     */
    private int nEntities;

    /**
     * Instantiation of the playground monitor.
     *
     * @param contestantsPerTrial the number of contestants per trial
     * @param generalRepository   the general repository
     */
    public Playground(int contestantsPerTrial, IGeneralRepository_Playground generalRepository) {
        this.contestantsPerTrial = contestantsPerTrial;
        lock = new ReentrantLock();
        teamData[0] = new TeamData(lock);
        teamData[1] = new TeamData(lock);
        refereeInformed = lock.newCondition();
        countInformed = 0;
        trialStarted = lock.newCondition();
        isTrialStarted = false;
        ropePosition = 0;
        trialEnded = lock.newCondition();
        contestantsDone = 0;
        trialDecided = lock.newCondition();
        isTrialDecided = false;
        this.generalRepository = generalRepository;
        nEntities = 0;
    }

    /**
     * The referee sets the rope position.
     *
     * @param ropePosition the rope position
     */
    public void setRopePosition(int ropePosition) throws RemoteException {
        try {
            lock.lock();
            this.ropePosition = ropePosition;
        } finally {
            lock.unlock();
        }
    }

    /**
     * The last contestant from each team informs the coach that they are ready. The contestant waits for the trial to
     * start by the referee.
     *
     * @param team       the team
     * @param contestant the contestant
     */
    public void getReady(int team, int contestant) throws RemoteException {
        TeamData teamData = this.teamData[team];
        lock.lock();
        try {
            teamData.contestantsReady++;
            generalRepository.getReady(team, contestant);
            if (teamData.contestantsReady == contestantsPerTrial) {
                teamData.trialReady.signal(); // alerts coach
            }
            while (!isTrialStarted) {
                trialStarted.await(); // releases lock and waits for referee
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * The coach waits for the contestants from his team to be ready. The last coach informs the referee that the team is
     * ready. The coach waits for the trial to be decided by the referee.
     *
     * @param team the team
     */
    public void informReferee(int team) throws RemoteException {
        TeamData teamData = this.teamData[team];
        lock.lock();
        try {
            while (teamData.contestantsReady < contestantsPerTrial) {
                teamData.trialReady.await(); // releases lock and waits
            }
            countInformed++;
            generalRepository.informReferee(team);
            if (countInformed == 2) {
                refereeInformed.signal(); // alerts referee
            }
            while (!isTrialDecided) {
                trialDecided.await(); // releases lock and waits for referee decision
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * The referee waits for the coaches to be ready to announce the start of the trial.
     */
    public void startTrial() throws RemoteException {
        lock.lock();
        try {
            while (countInformed < 2) {
                refereeInformed.await(); // releases lock and waits for the last coach
            }
            isTrialDecided = false;
            isTrialStarted = true;
            generalRepository.startTrial();
            trialStarted.signalAll(); // alerts contestants
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * The contestant pulls the rope with a similar distance as his current strength. The contestant loses 1 unit of
     * strength after pulling the rope.
     *
     * @param team       the team
     * @param contestant the contestant
     * @param strength   the current strength
     * @return the updated strength
     */
    public int pullTheRope(int team, int contestant, int strength) throws RemoteException {
        lock.lock();
        try {
            ropePosition += (team == 0) ? -strength : strength;
            // If strength is already the minimum (1), it will remain like that
            if (strength > 1) {
                strength--;
                generalRepository.pullTheRope(team, contestant, true);
            } else {
                generalRepository.pullTheRope(team, contestant, false);
            }
        } finally {
            lock.unlock();
        }
        return strength;
    }

    /**
     * The last contestant informs the referee that the trial is over. The contestant waits for the referee to decide
     * the result of the trial.
     */
    public void amDone() throws RemoteException {
        lock.lock();
        try {
            contestantsDone++;
            generalRepository.amDone();
            if (contestantsDone == 2 * contestantsPerTrial) {
                trialEnded.signal(); // alerts referee
            }
            while (!isTrialDecided) {
                trialDecided.await(); // releases lock and waits for referee decision
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * The referee waits for the contestants to be done to decide the result of the trial.
     *
     * @return the rope position
     */
    public int assertTrialDecision() throws RemoteException {
        lock.lock();
        try {
            while (contestantsDone < 2 * contestantsPerTrial) {
                trialEnded.await(); // releases lock and waits for the last contestant
            }
            teamData[0].contestantsReady = 0;
            teamData[1].contestantsReady = 0;
            countInformed = 0;
            contestantsDone = 0;
            isTrialStarted = false;
            isTrialDecided = true;
            generalRepository.assertTrialDecision(ropePosition);
            trialDecided.signalAll(); // alerts contestants and coaches
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return ropePosition;
    }

    /**
     * Operation server shutdown.
     */
    public void shutdown() throws RemoteException {
        lock.lock();
        try {
            nEntities += 1;
            if (nEntities >= 3) {
                ((IGeneralRepository)generalRepository).shutdown();
                ServerPlayground.shutdown();
            }
        } finally {
            lock.unlock();
        }
    }
}
