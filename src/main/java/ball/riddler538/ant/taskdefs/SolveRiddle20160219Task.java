package ball.riddler538.ant.taskdefs;
/*-
 * ##########################################################################
 * Solutions for the 538 Riddler
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2015 - 2020 Allen D. Ball
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.tools.ant.BuildException;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * {@link.uri http://ant.apache.org/ Ant} {@link org.apache.tools.ant.Task}
 * to solve
 * {@link.uri
 * http://fivethirtyeight.com/features/will-someone-be-sitting-in-your-seat-on-the-plane/
 * Will Someone Be Sitting In Your Seat On The Plane?}
 * <p>
 * There's an airplane with 100 seats, and there are 100 ticketed passengers
 * each with an assigned seat.  They line up to board in some random order.
 * However, the first person to board is the worst person alive, and just
 * sits in a random seat, without even looking at his boarding pass.  Each
 * subsequent passenger sits in his or her own assigned seat if it's empty,
 * but sits in a random open seat if the assigned seat is occupied.  What is
 * the probability that you, the hundredth passenger to board, finds your
 * seat unoccupied?
 * </p>
 * Solution uses the Monte Carlo method.
 *
 * {@ant.task}
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
@AntTask("solve-riddle-2016-02-19")
@NoArgsConstructor @ToString
public class SolveRiddle20160219Task extends AbstractSimulationTask {
    @Getter @Setter
    private int passengers = -1;
    @Getter @Setter
    private int seats = 100;

    @Override
    public void execute() throws BuildException {
        super.execute();

        if (getPassengers() < 0) {
            setPassengers(getSeats());
        }

        try {
            List<Integer> passengers = asList(0, getPassengers());
            List<Integer> seats = asList(0, getSeats());
            ArrayList<Simulation> simulations = new ArrayList<>(getCount());

            for (int i = 0, n = getCount(); i < n; i += 1) {
                simulations.add(new Simulation(passengers, seats));
            }

            int passengerN = passengers.get(passengers.size() - 1);
            int seatN = passengerN;
            int successes = 0;

            for (Simulation simulation : simulations) {
                if (simulation.get(seatN) == passengerN) {
                    successes += 1;
                }
            }

            log(new SimpleTableModel(new Object[][] { }, 3)
                .row("seats:", getSeats(), EMPTY)
                .row("passengers:", getPassengers(), EMPTY)
                .row("count:", simulations.size(), EMPTY)
                .row("successes:",
                     successes, asPercent(successes, simulations.size()) + "%"));

            String[] headers =
                new String[] { "passenger", "seat#" + seatN + "count", "%", "cum%" };
            ArrayList<Map<Integer,Number>> maps = new ArrayList<>();

            maps.add(new TreeMap<Integer,Number>());
            maps.add(new TreeMap<Integer,Number>());
            maps.add(new TreeMap<Integer,Number>());

            for (int passenger : passengers) {
                maps.get(0).put(passenger, 0);
            }

            for (Simulation simulation : simulations) {
                int passenger = simulation.get(seatN);

                maps.get(0).put(passenger,
                                maps.get(0).get(passenger).intValue() + 1);
            }

            float cumulative = (float) 0;

            for (int passenger : passengers) {
                float probability =
                    asPercent(maps.get(0).get(passenger), simulations.size());

                maps.get(1).put(passenger, probability);

                cumulative += probability;

                maps.get(2).put(passenger, cumulative);
            }

            log();
            log(new MapsTableModel(maps,
                                   "passenger", "seat#" + seatN + " count",
                                   "%", "cum%"));
        } catch (BuildException exception) {
            throw exception;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new BuildException(throwable);
        }
    }

    private List<Integer> asList(int start, int end) {
        Integer[] array = new Integer[end - start];

        for (int i = 0; i < array.length; i += 1) {
            array[i] = start + i;
        }

        return Collections.unmodifiableList(Arrays.asList(array));
    }

    private class Simulation extends TreeMap<Integer,Integer> {
        private static final long serialVersionUID = -4047361911757741221L;

        public Simulation(List<Integer> passengers, List<Integer> seats) {
            passengers = new ArrayList<>(passengers);
            seats = new ArrayList<>(seats);
            /*
             * First passenger chooses any seat.
             */
            Collections.sort(passengers);
            Collections.shuffle(seats);
            put(seats.remove(0), passengers.remove(0));
            /*
             * Remaining passenger takes their assigned seat unless someone
             * has already taken their's.
             */
            for (int p : passengers) {
                int s = p;

                if (! seats.remove((Object) s)) {
                    s = seats.remove(0);
                }

                put(s, p);
            }
        }
    }
}
