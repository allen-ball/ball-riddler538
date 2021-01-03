package ball.riddler538.ant.taskdefs;
/*-
 * ##########################################################################
 * Solutions for the 538 Riddler
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2015 - 2021 Allen D. Ball
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ##########################################################################
 */
import ball.swing.table.MapsTableModel;
import ball.swing.table.SimpleTableModel;
import ball.util.ant.taskdefs.AntTask;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.tools.ant.BuildException;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * {@link.uri http://ant.apache.org/ Ant} {@link org.apache.tools.ant.Task}
 * to solve
 * {@link.uri
 * http://fivethirtyeight.com/features/can-you-win-this-hot-new-game-show/
 * Can You Win This Hot New Game Show?}
 * <p>
 * Two players go on a hot new game show "Higher Number Wins."  The two go
 * into separate booths, and each presses a button, and a random number
 * between zero and one appears on a screen.  (At this point, neither knows
 * the s number, but they do know the numbers are chosen from a standard
 * uniform distribution.)  They can choose to keep that first number, or to
 * press the button again to discard the first number and get a second
 * random number, which they must keep. Then, they come out of their booths
 * and see the final number for each player on the wall.  The lavish grand
 * prize -- a case full of bullion gold -- is awarded to the player who kept
 * the higher number.  Which number is the optimal cutoff for players to
 * discard their first number and choose another?  Put another way, within
 * which range should they choose to keep the first number, and within which
 * range should they reject it and try their luck with a second number?
 * </p>
 * Solution uses the Monte Carlo method.
 *
 * {@ant.task}
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
@AntTask("solve-riddle-2016-03-04")
@NoArgsConstructor @ToString
public class SolveRiddle20160304Task extends AbstractSimulationTask {
    @Override
    public void execute() throws BuildException {
        super.execute();

        try {
            ArrayList<Simulation> simulations = new ArrayList<>(getCount());

            for (int i = 0, n = getCount(); i < n; i += 1) {
                simulations.add(new Simulation());
            }

            ArrayList<BucketMap> maps = new ArrayList<>();

            maps.add(new BucketMap());
            maps.add(new BucketMap());

            for (Simulation simulation : simulations) {
                double[] winner = simulation.getWinnerPicks();
                double key = maps.get(0).tailMap(winner[0]).firstKey();

                maps.get(0).put(key, maps.get(0).get(key) + 1);

                double[] loser = simulation.getLoserPicks();

                if (winner[0] > max(loser)) {
                    maps.get(1).put(key, maps.get(1).get(key) + 1);
                }
            }

            log(new SimpleTableModel(new Object[][] { }, 3)
                .row("count:", simulations.size(), EMPTY));
            log();
            log(new MapsTableModel(maps,
                                   "first pick", "total wins",
                                   "on first pick"));
        } catch (BuildException exception) {
            throw exception;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new BuildException(throwable);
        }
    }

    private double max(double... array) { return array[greatest(array)]; }

    private int greatest(double... array) {
        int greatest = 0;

        for (int i = 1; i < array.length; i += 1) {
            if (array[i] > array[greatest]) {
                greatest = i;
            }
        }

        return greatest;
    }

    private static final Random RANDOM = new Random();

    @ToString
    private class Simulation {
        private final double[][] p;

        public Simulation() {
            p =
                new double[][] {
                    new double[] { RANDOM.nextDouble(), RANDOM.nextDouble() },
                    new double[] { RANDOM.nextDouble(), RANDOM.nextDouble() }
                };
        }

        public int getWinner() { return greatest(max(p[0]), max(p[1])); }

        public double[] getWinnerPicks() { return p[getWinner()]; }

        public int getLoser() { return (getWinner() == 0) ? 1 : 0; }

        public double[] getLoserPicks() { return p[getLoser()]; }
    }

    private class BucketMap extends TreeMap<Double,Integer> {
        private static final long serialVersionUID = -5756858279184775705L;

        public BucketMap() {
            super();

            for (int i = 0; i <= 100; i += 1) {
                put((double) i / 100, 0);
            }
        }
    }
}
