package org.thomas.annea.solvers.observers;

/**
 * Created by krekle on 06/04/16.
 */
public interface SolverObserver {

    public void fireLog(int generation, double max, double avg);

}
