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
import ball.swing.table.ArrayListTableModel;
import ball.swing.table.SimpleTableModel;
import ball.util.ant.taskdefs.AntTask;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.tools.ant.BuildException;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * {@link.uri http://ant.apache.org/ Ant} {@link org.apache.tools.ant.Task}
 * to solve a
 * {@link.uri https://fivethirtyeight.com/features/can-you-solve-the-vexing-vexillology/ Riddler Classic}:
 *
 * The New York Times recently launched some new word puzzles, one of which
 * is Spelling Bee. In this game, seven letters are arranged in a honeycomb
 * lattice, with one letter in the center. Here’s the lattice from December
 * 24, 2019:
 *
 * Spelling Bee screenshot, with the required letter G, and the additional
 * letters L, A, P, X, M and E.
 * The goal is to identify as many words that meet the following criteria:
 *
 * The word must be at least four letters long.
 * The word must include the central letter.
 * The word cannot include any letter beyond the seven given letters.
 * Note that letters can be repeated. For example, the words GAME and
 * AMALGAM are both acceptable words. Four-letter words are worth 1 point
 * each, while five-letter words are worth 5 points, six-letter words are
 * worth 6 points, seven-letter words are worth 7 points, etc. Words that
 * use all of the seven letters in the honeycomb are known as “pangrams” and
 * earn 7 bonus points (in addition to the points for the length of the
 * word). So in the above example, MEGAPLEX is worth 15 points.
 *
 * Which seven-letter honeycomb results in the highest possible game score?
 * To be a valid choice of seven letters, no letter can be repeated, it must
 * not contain the letter S (that would be too easy) and there must be at
 * least one pangram.
 *
 * For consistency, please use this word list to check your game score.
 *
 * {@ant.task}
 * <p>
 * The solution:
 * </p>
<pre>
List:           172820
Scoring Words:  44585
Honeycomb Sets: 7986
Solutions:      55902

      537           3898
------------------------
  T   REAGGREGATING 20
A   E REINTEGRATING 20
  R   ENTERTAINING  19
N   G INTENERATING  19
  I   REGENERATING  19
      REINITIATING  19
      AGGREGATING   18
      GRATINEEING   18
      INTEGRATING   18
      ITINERATING   18
      REATTAINING   18
      REINTEGRATE   18
      REITERATING   18
      RETARGETING   18
      ENTRAINING    17
      ENTREATING    17
      GARNIERITE    17
      GENERATING    17
      GREATENING    17
      INGRATIATE    17
      INTERREGNA    17
      INTREATING    17
      REGRANTING    17
      RETRAINING    17
      RETREATING    17
      ARGENTINE     16
      ARGENTITE     16
      GARTERING     16
      INTEGRATE     16
      INTERGANG     16
      ITERATING     16
      NATTERING     16
      RATTENING     16
      REGRATING     16
      RETAGGING     16
      RETAINING     16
      RETEARING     16
      TANGERINE     16
      TARGETING     16
      TATTERING     16
      AERATING      15
      GNATTIER      15
      GRATINEE      15
      INTERAGE      15
      TREATING      15
      GRANITE       14
      GRATINE       14
      INGRATE       14
      TANGIER       14
      TEARING       14
      REENGINEERING 13
      INGRATIATING  12
      ENGINEERING   11
      ENTERTAINER   11
      REAGGREGATE   11
      REARRANGING   11
      REINTERRING   11
      TRINITARIAN   11
      ARRAIGNING    10
      ENGRAINING    10
      GANGRENING    10
      INGRAINING    10
      INTENERATE    10
      IRRIGATING    10
      IRRITATING    10
      REENGAGING    10
      REENGINEER    10
      REENTERING    10
      REGENERATE    10
      REGREENING    10
      REGREETING    10
      REGRETTING    10
      REIGNITING    10
      REINITIATE    10
      RETREATANT    10
      TRIGGERING    10
      AGGREGATE     9
      ARRANGING     9
      ARREARAGE     9
      ENTERTAIN     9
      ENTRAINER     9
      GARNERING     9
      GETTERING     9
      GINGERING     9
      GREENGAGE     9
      GREGARINE     9
      INTERNING     9
      INTERRING     9
      INTRIGANT     9
      ITINERANT     9
      ITINERATE     9
      NARRATING     9
      NITRATING     9
      REARRANGE     9
      REEARNING     9
      REENTRANT     9
      REGAINING     9
      REGEARING     9
      REGRETTER     9
      REITERATE     9
      RENIGGING     9
      RERIGGING     9
      RETINTING     9
      RETREATER     9
      TEETERING     9
      TENTERING     9
      TITRATING     9
      TITTERING     9
      AGRARIAN      8
      AGREEING      8
      AIGRETTE      8
      ANEARING      8
      ANGERING      8
      ANTEATER      8
      ANTIARIN      8
      ARGININE      8
      ARRANGER      8
      ATTAINER      8
      ATTIRING      8
      ENGINEER      8
      ENRAGING      8
      ENTERING      8
      GANGRENE      8
      GARAGING      8
      GENERATE      8
      GNARRING      8
      GRAINIER      8
      GRAINING      8
      GRANTING      8
      GREEGREE      8
      GREENIER      8
      GREENING      8
      GREETING      8
      GRINNING      8
      GRITTIER      8
      GRITTING      8
      INERRANT      8
      INERTIAE      8
      INTERNEE      8
      INTERTIE      8
      IRRIGATE      8
      IRRITANT      8
      IRRITATE      8
      NARRATER      8
      RATTENER      8
      REATTAIN      8
      REENGAGE      8
      REGAINER      8
      REIGNING      8
      REIGNITE      8
      RENEGING      8
      RENITENT      8
      RETAINER      8
      RETARGET      8
      RETIARII      8
      RETINENE      8
      RETINITE      8
      RETIRANT      8
      RETIRING      8
      TARTRATE      8
      TEENAGER      8
      TERRARIA      8
      TITTERER      8
      TRAINING      8
      TRIAGING      8
      TRIENNIA      8
      TRIGGING      8
      AGINNER       7
      AIRTING       7
      ANERGIA       7
      ANGARIA       7
      ANGRIER       7
      ANTIAIR       7
      ARENITE       7
      ARIETTA       7
      ARIETTE       7
      ARRAIGN       7
      ARRANGE       7
      ATTRITE       7
      EAGERER       7
      EARNING       7
      EARRING       7
      ENGAGER       7
      ENGRAIN       7
      ENTERER       7
      ENTRAIN       7
      ENTRANT       7
      ENTREAT       7
      ETAGERE       7
      GARRING       7
      GEARING       7
      GINNIER       7
      GIRNING       7
      GIRTING       7
      GITTERN       7
      GRAINER       7
      GRANGER       7
      GRANITA       7
      GRANNIE       7
      GRANTEE       7
      GRANTER       7
      GRATING       7
      GREATEN       7
      GREATER       7
      GREEING       7
      GREENER       7
      GREENIE       7
      GREETER       7
      GRINNER       7
      IGNITER       7
      INERTIA       7
      INGRAIN       7
      INTEGER       7
      INTERNE       7
      INTRANT       7
      INTREAT       7
      ITERANT       7
      ITERATE       7
      NAGGIER       7
      NARRATE       7
      NATTIER       7
      NEARING       7
      NEGATER       7
      NETTIER       7
      NITERIE       7
      NITRATE       7
      NITRITE       7
      NITTIER       7
      RAGGING       7
      RAINIER       7
      RAINING       7
      RANGIER       7
      RANGING       7
      RANTING       7
      RATATAT       7
      RATTEEN       7
      RATTIER       7
      RATTING       7
      REAGENT       7
      REARING       7
      REENTER       7
      REGATTA       7
      REGINAE       7
      REGNANT       7
      REGRANT       7
      REGRATE       7
      REGREEN       7
      REGREET       7
      REINING       7
      REINTER       7
      RENEGER       7
      RENTIER       7
      RENTING       7
      RETINAE       7
      RETIREE       7
      RETIRER       7
      RETRAIN       7
      RETREAT       7
      RETTING       7
      RIGGING       7
      RINGENT       7
      RINGGIT       7
      RINGING       7
      RINNING       7
      TANAGER       7
      TANTARA       7
      TARRIER       7
      TARRING       7
      TARTANA       7
      TARTING       7
      TATTIER       7
      TEARIER       7
      TEENIER       7
      TENTIER       7
      TERGITE       7
      TERNATE       7
      TERRAIN       7
      TERRANE       7
      TERREEN       7
      TERRENE       7
      TERRIER       7
      TERRINE       7
      TERTIAN       7
      TIERING       7
      TINNIER       7
      TITRANT       7
      TITRATE       7
      TRAINEE       7
      TRAINER       7
      TREATER       7
      TREEING       7
      TRIGGER       7
      TRINING       7
      AERATE        6
      AERIER        6
      AIGRET        6
      AIRIER        6
      AIRING        6
      ANTIAR        6
      ARGENT        6
      ARRANT        6
      ARREAR        6
      ARTIER        6
      ATTIRE        6
      EARING        6
      EARNER        6
      EERIER        6
      ENGIRT        6
      ENRAGE        6
      ENTERA        6
      ENTIRE        6
      ENTREE        6
      ERGATE        6
      ERRANT        6
      ERRATA        6
      ERRING        6
      ETERNE        6
      GAGGER        6
      GAINER        6
      GAITER        6
      GANGER        6
      GARAGE        6
      GARGET        6
      GARNER        6
      GARNET        6
      GARRET        6
      GARTER        6
      GENERA        6
      GERENT        6
      GETTER        6
      GINGER        6
      GINNER        6
      GRANGE        6
      GRATER        6
      GRATIN        6
      GREIGE        6
      GRIGRI        6
      INANER        6
      INTERN        6
      IRATER        6
      NAGGER        6
      NARINE        6
      NATTER        6
      NEARER        6
      NEATER        6
      NETTER        6
      NIGGER        6
      RAGGEE        6
      RAGING        6
      RAGTAG        6
      RANGER        6
      RANTER        6
      RARING        6
      RATINE        6
      RATING        6
      RATITE        6
      RATTAN        6
      RATTEN        6
      RATTER        6
      REAGIN        6
      REARER        6
      REEARN        6
      REGAIN        6
      REGEAR        6
      REGENT        6
      REGGAE        6
      REGINA        6
      REGRET        6
      RENEGE        6
      RENNET        6
      RENNIN        6
      RENTER        6
      RETAIN        6
      RETEAR        6
      RETENE        6
      RETINA        6
      RETINE        6
      RETINT        6
      RETIRE        6
      RIGGER        6
      RINGER        6
      RITTER        6
      TAGGER        6
      TAGRAG        6
      TANNER        6
      TANTRA        6
      TARGET        6
      TARING        6
      TARTAN        6
      TARTAR        6
      TARTER        6
      TATTER        6
      TEARER        6
      TEENER        6
      TEETER        6
      TENNER        6
      TENTER        6
      TERETE        6
      TERRAE        6
      TERRET        6
      TERRIT        6
      TETTER        6
      TINIER        6
      TINNER        6
      TINTER        6
      TIRING        6
      TITTER        6
      TRIAGE        6
      TRIENE        6
      TRITER        6
      AERIE         5
      AGGER         5
      AGREE         5
      AGRIA         5
      AIRER         5
      ANEAR         5
      ANGER         5
      ANTRA         5
      ANTRE         5
      AREAE         5
      ARENA         5
      ARETE         5
      ATRIA         5
      ATTAR         5
      EAGER         5
      EAGRE         5
      EATER         5
      EERIE         5
      EGGAR         5
      EGGER         5
      EGRET         5
      ENTER         5
      GAGER         5
      GARNI         5
      GENRE         5
      GNARR         5
      GRAIN         5
      GRANA         5
      GRANT         5
      GRATE         5
      GREAT         5
      GREEN         5
      GREET         5
      INERT         5
      INNER         5
      INTER         5
      IRATE         5
      IRING         5
      NAIRA         5
      NITER         5
      NITRE         5
      RAGEE         5
      RANEE         5
      RANGE         5
      RARER         5
      RATAN         5
      RATER         5
      REATA         5
      REGNA         5
      REIGN         5
      RENIG         5
      RENIN         5
      RENTE         5
      RERAN         5
      RERIG         5
      RETAG         5
      RETIA         5
      RETIE         5
      RIANT         5
      RIATA         5
      TARGE         5
      TARRE         5
      TATAR         5
      TATER         5
      TERAI         5
      TERGA         5
      TERNE         5
      TERRA         5
      TETRA         5
      TIARA         5
      TIGER         5
      TITER         5
      TITRE         5
      TRAGI         5
      TRAIN         5
      TRAIT         5
      TREAT         5
      TREEN         5
      TRIER         5
      TRINE         5
      TRITE         5
      AGAR          1
      AGER          1
      AIRN          1
      AIRT          1
      AREA          1
      ARIA          1
      EARN          1
      EGER          1
      ERNE          1
      GEAR          1
      GIRN          1
      GIRT          1
      GNAR          1
      GRAN          1
      GRAT          1
      GREE          1
      GRIG          1
      GRIN          1
      GRIT          1
      NEAR          1
      RAGA          1
      RAGE          1
      RAGI          1
      RAIA          1
      RAIN          1
      RANG          1
      RANI          1
      RANT          1
      RARE          1
      RATE          1
      REAR          1
      REIN          1
      RENT          1
      RETE          1
      RING          1
      RITE          1
      TARE          1
      TARN          1
      TART          1
      TEAR          1
      TERN          1
      TIER          1
      TIRE          1
      TREE          1
      TRET          1
      TRIG          1
</pre>
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
@AntTask("solve-riddle-2020-01-03")
@NoArgsConstructor @ToString
public class SolveRiddle20200103Task extends AbstractTask {
    private static final Character S = Character.valueOf('S');

    @Override
    public void execute() throws BuildException {
        super.execute();

        try {
            URI uri = getClass().getResource("enable1.txt").toURI();
            List<String> list =
                Files.lines(Paths.get(uri))
                .map(t -> t.split("#", 2)[0].trim())
                .filter(t -> isNotBlank(t))
                .map(t -> t.toUpperCase())
                .collect(toList());
            Set<String> words =
                list.stream()
                .filter(t -> t.indexOf(S) == -1)
                .filter(t -> score(t) > 0)
                .filter(t -> toCharacterSet(t).size() <= 7)
                .collect(toSet());
            Set<Set<Character>> sets =
                words.stream()
                .map(t -> toCharacterSet(t))
                .filter(t -> t.size() == 7)
                .collect(toSet());
            Map<Set<Character>,Set<String>> map =
                new TreeMap<>(Comparator.comparing(Object::toString));

            for (Set<Character> key : sets) {
                Set<String> value =
                    words.stream()
                    .filter(t -> key.containsAll(toCharacterSet(t)))
                    .collect(toSet());

                map.put(key, value);
            }

            Map<List<Character>,Set<String>> solutions =
                new TreeMap<>(Comparator.comparing(Object::toString));
            Map<List<Character>,Integer> scores =
                new TreeMap<>(Comparator.comparing(Object::toString));

            for (Map.Entry<Set<Character>,Set<String>> entry :
                     map.entrySet()) {
                for (int i = 0, n = entry.getKey().size(); i < n; i += 1) {
                    List<Character> key =
                        entry.getKey().stream().collect(toList());

                    key.add(0, key.remove(i));

                    Set<String> value =
                        entry.getValue()
                        .stream()
                        .filter(t -> t.indexOf(key.get(0)) >= 0)
                        .collect(toSet());

                    solutions.put(key, value);
                    scores.put(key, score(value));
                }
            }

            log(new SimpleTableModel(new Object[][] { }, 2)
                .row("List:", list.size())
                .row("Scoring Words:", words.size())
                .row("Honeycomb Sets:", sets.size())
                .row("Solutions:", solutions.size()));

            int maxScore =
                scores.values()
                .stream()
                .mapToInt(Integer::intValue)
                .max().getAsInt();

            for (List<Character> key : scores.keySet()) {
                if (scores.get(key).intValue() == maxScore) {
                    log();

                    List<String> solution =
                        solutions.get(key).stream().collect(toList());

                    solution.sort(Comparator
                                  .<String>comparingInt(t -> score(t))
                                  .reversed()
                                  .thenComparing(Comparator.naturalOrder()));

                    log(new SolutionTableModel(key, solution));
                }
            }
        } catch (BuildException exception) {
            throw exception;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new BuildException(throwable);
        }
    }

    private int score(String string) {
        int score = 0;

        switch (string.length()) {
        case 0:
        case 1:
        case 2:
        case 3:
            score = 0;
            break;

        case 4:
            score = 1;
            break;

        default:
            score = string.length();

            if (string.length() >= 7) {
                if (toCharacterSet(string).size() == 7) {
                    score += 7;
                }
            }
            break;
        }

        return score;
    }

    private int score(Collection<String> collection) {
        return collection.stream().mapToInt(t -> score(t)).sum();
    }

    private List<Character> toCharacterList(String string) {
        return (string.chars()
                .mapToObj(c -> Character.valueOf((char) c))
                .collect(toList()));
    }

    private Set<Character> toCharacterSet(String string) {
        return toCharacterList(string).stream().collect(toSet());
    }

    private class SolutionTableModel extends ArrayListTableModel<String> {
        private static final long serialVersionUID = -5945141277843892156L;

        private final List<Character> honeycomb;

        public SolutionTableModel(List<Character> honeycomb,
                                  List<String> solution) {
            super(solution,
                  new String[] {
                      EMPTY, EMPTY, EMPTY,
                      String.valueOf(solution.size()),
                      String.valueOf(score(solution))
                  });

            this.honeycomb = honeycomb;
        }

        @Override
        public Object getValueAt(int y, int x) {
            Object value = EMPTY;

            if (y == 0 && x == 1) {
                value = honeycomb.get(2);
            } else if (y == 1 && x == 0) {
                value = honeycomb.get(1);
            } else if (y == 1 && x == 2) {
                value = honeycomb.get(3);
            } else if (y == 2 && x == 1) {
                value = honeycomb.get(0);
            } else if (y == 3 && x == 0) {
                value = honeycomb.get(6);
            } else if (y == 3 && x == 2) {
                value = honeycomb.get(4);
            } else if (y == 4 && x == 1) {
                value = honeycomb.get(5);
            } else {
                value = super.getValueAt(y, x);
            }

            return value;
        }

        @Override
        protected Object getValueAt(String row, int x) {
            Object value = EMPTY;

            switch (x) {
            case 3:     value = row;            break;
            case 4:     value = score(row);     break;
            }

            return value;
        }
    }
}
