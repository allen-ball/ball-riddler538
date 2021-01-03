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
import ball.util.ant.taskdefs.AntTask;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.tools.ant.BuildException;

/**
 * {@link.uri http://ant.apache.org/ Ant} {@link org.apache.tools.ant.Task}
 * to solve
 * {@link.uri
 * http://fivethirtyeight.com/features/how-long-will-your-smartphone-distract-you-from-family-dinner/
 * How Long Will Your Smartphone Distract You From Family Dinner?}
 * <p>
 * You’ve just finished unwrapping your holiday presents. You and your
 * sister got brand-new smartphones, opening them at the same moment. You
 * immediately both start doing important tasks on the Internet, and each
 * task you do takes one to five minutes. (All tasks take exactly one, two,
 * three, four or five minutes, with an equal probability of each). After
 * each task, you have a brief moment of clarity. During these, you remember
 * that you and your sister are supposed to join the rest of the family for
 * dinner and that you promised each other you’d arrive together. You ask if
 * your sister is ready to eat, but if she is still in the middle of a task,
 * she asks for time to finish it. In that case, you now have time to kill,
 * so you start a new task (again, it will take one, two, three, four or
 * five minutes, exactly, with an equal probability of each). If she asks
 * you if it’s time for dinner while you’re still busy, you ask for time to
 * finish up and she starts a new task and so on. From the moment you first
 * open your gifts, how long on average does it take for both of you to be
 * between tasks at the same time so you can finally eat? (You can assume
 * the “moments of clarity” are so brief as to take no measurable time at
 * all.)
 * </p>
 * Solution uses the Monte Carlo method.
 *
 * {@ant.task}
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
@AntTask("solve-riddle-2015-12-22")
@NoArgsConstructor @ToString
public class SolveRiddle20151222Task extends AbstractSimulationTask {
    @Override
    public void execute() throws BuildException {
        super.execute();

        try {
            ArrayList<Try> tries = new ArrayList<>(getCount());

            for (int i = 0, n = getCount(); i < n; i += 1) {
                tries.add(new Try());
            }

            int min = tries.get(0).duration();
            int max = tries.get(0).duration();
            int sum = 0;

            for (Try tryN : tries) {
                int duration = tryN.duration();

                min = Math.min(min, duration);
                max = Math.max(max, duration);
                sum += duration;
            }

            log("count: " + tries.size());
            log("min: " + min);
            log("max: " + max);
            log("average: " + (((float) sum) / tries.size()));
        } catch (BuildException exception) {
            throw exception;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new BuildException(throwable);
        }
    }

    private static final Random RANDOM = new Random();
    private static final List<Integer> DURATIONS =
        Arrays.asList(1, 2, 3, 4 ,5);

    @ToString
    private class Try {
        private List<Integer> you = new ArrayList<>();
        private List<Integer> sister = new ArrayList<>();

        public Try() {
            for (;;) {
                if (sum(you) < sum(sister)) {
                    you.add(DURATIONS.get(RANDOM.nextInt(DURATIONS.size())));
                } else {
                    sister.add(DURATIONS.get(RANDOM.nextInt(DURATIONS.size())));
                }

                if (sum(you) == sum(sister)) {
                    break;
                }
            }
        }

        public int duration() { return sum(you); }

        private int sum(List<Integer> list) {
            int total = 0;

            for (Integer element : list) {
                total += element;
            }

            return total;
        }
    }
}
