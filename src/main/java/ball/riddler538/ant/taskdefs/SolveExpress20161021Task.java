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
import ball.game.scrabble.Bag;
import ball.game.scrabble.Tile;
import ball.game.scrabble.WordList;
import ball.swing.table.MapTableModel;
import ball.util.ant.taskdefs.AntTask;
import ball.util.ant.taskdefs.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.tools.ant.BuildException;

/**
 * {@link.uri http://ant.apache.org/ Ant} {@link org.apache.tools.ant.Task}
 * to solve
 * {@link.uri http://fivethirtyeight.com/features/this-challenge-will-boggle-your-mind/ First, for Riddler Express, a Scrabble problem:}
 * <p>
 * What is the longest word you can build in a game of Scrabble one letter
 * at a time? That is, starting with a valid two-letter word, how long a
 * word can you build by playing one letter at a time on either side to form
 * a valid three-letter word, then a valid four-letter word, and so on? (For
 * example, HE could become THE, then THEM, then THEME, then THEMES, for a
 * six-letter result.)
 * </p>
 * {@ant.task}
 * <p>
 * The generated solutions:
 * </p>
<pre>
CLASSISMS [AS, AS+S, L+ASS, LASS+I, LASSI+S, C+LASSIS, CLASSIS+M, CLASSISM+S]
CLASSISTS [AS, AS+S, L+ASS, LASS+I, LASSI+S, C+LASSIS, CLASSIS+T, CLASSIST+S]
RELAPSERS [LA, LA+P, LAP+S, LAPS+E, E+LAPSE, R+ELAPSE, RELAPSE+R, RELAPSER+S]
GLASSIEST [AS, AS+S, L+ASS, LASS+I, LASSI+E, LASSIE+S, G+LASSIES, GLASSIES+T]
SCRAPINGS [PI, PI+N, PIN+G, A+PING, R+APING, C+RAPING, S+CRAPING, SCRAPING+S]
SHEATHERS [AT, E+AT, EAT+H, H+EATH, S+HEATH, SHEATH+E, SHEATHE+R, SHEATHER+S]
UPRAISERS [IS, A+IS, R+AIS, RAIS+E, P+RAISE, PRAISE+R, PRAISER+S, U+PRAISERS]
</pre>
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
@AntTask("solve-express-2016-10-21")
@NoArgsConstructor @ToString
public class SolveExpress20161021Task extends AbstractTask {
    @NotNull @Getter @Setter
    private String list = null;
    private WordList wordlist = null;

    @Override
    public void execute() throws BuildException {
        super.execute();

        try {
            wordlist = (WordList) getClassForName(getList()).newInstance();

            SolutionMap previous = new SolutionMap();
            SolutionMap next = new SolutionMap();

            wordlist.stream()
                .filter(t -> t.length() == 2)
                .forEach(t -> next.add(new Solution(t)));

            while (! next.isEmpty()) {
                previous.clear();
                previous.putAll(next);

                next.clear();

                for (Solution solution : previous.values()) {
                    next.addAll(solution.solutions());
                }
            }

            log(new MapTableModel(previous));
        } catch (BuildException exception) {
            throw exception;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new BuildException(throwable);
        }
    }

    private class Solution implements Cloneable {
        private Bag bag = new Bag();
        private ArrayList<CharSequence> list = new ArrayList<>();
        private StringBuilder builder = new StringBuilder();

        public Solution(CharSequence sequence) { append(sequence); }

        public Collection<Solution> solutions() throws Exception {
            SolutionMap map = new SolutionMap();

            for (Tile tile : new ArrayList<Tile>(bag)) {
                map.add(clone().prepend(tile.toString()));
                map.add(clone().append(tile.toString()));
            }

            map.keySet().retainAll(wordlist);

            return map.values();
        }

        private Solution prepend(CharSequence sequence) {
            if (! list.isEmpty()) {
                list.add(sequence + "+" + builder);

                for (int i = 0, n = sequence.length(); i < n; i += 1) {
                    char letter = sequence.charAt(i);
                    Tile tile = bag.draw(letter);

                    builder.insert(0, tile.getLetter());
                }
            } else {
                append(sequence);
            }

            return this;
        }

        private Solution append(CharSequence sequence) {
            if (! list.isEmpty()) {
                list.add(builder + "+" + sequence);
            } else {
                list.add(sequence);
            }

            for (int i = 0, n = sequence.length(); i < n; i += 1) {
                char letter = sequence.charAt(i);
                Tile tile = bag.draw(letter);

                builder.append(tile.getLetter());
            }

            return this;
        }

        @Override
        public Solution clone() throws CloneNotSupportedException {
            Solution clone = (Solution) super.clone();

            clone.bag = this.bag.clone();
            clone.list = new ArrayList<>(this.list);
            clone.builder = new StringBuilder(this.builder);

            return clone;
        }

        @Override
        public String toString() { return list.toString(); }
    }

    private class SolutionMap extends HashMap<CharSequence,Solution> {
        private static final long serialVersionUID = -5105125984399633023L;

        public SolutionMap() { super(); }

        public SolutionMap(SolutionMap map) { super(map); }

        public void add(Solution solution) {
            put(solution.builder.toString(), solution);
        }

        public void addAll(Iterable<Solution> iterable) {
            for (Solution solution : iterable) {
                add(solution);
            }
        }
    }
}
