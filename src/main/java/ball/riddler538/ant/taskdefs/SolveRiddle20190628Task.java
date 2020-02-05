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
import ball.util.ant.taskdefs.AntTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.tools.ant.BuildException;

import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.repeat;

/**
 * {@link.uri http://ant.apache.org/ Ant} {@link org.apache.tools.ant.Task}
 * to solve a
 * {@link.uri https://fivethirtyeight.com/features/whats-your-best-scrabble-string/ Riddler Classic}:
 * <p>
 * From Benjamin Danard, the Superstring Scrabble Challenge:
 * </p>
 * <p>
 * The game of Scrabble has 100 tiles — 98 of these tiles contain a letter
 * and a score, and two of them are wildcards worth zero points. At home on
 * a lazy summer day with a bag of these tiles, you decide to play the
 * Superstring Scrabble Challenge. Using only the 100 tiles, you lay them
 * out into one long 100-letter string of your choosing. You look through
 * the string. For each word you find, you earn points equal to its
 * score. Once you find a word, you don’t get any points for finding it
 * again. The same tile may be used in multiple, overlapping words. So
 * ‘“theater” includes “the,” “heat,” “heater,” “eat,” “eater,” “ate,” etc.
 * </p>
 * <p>
 * The super challenge: What order of tiles gives you the biggest score?
 * (The blank tiles are locked into the letter they represent once you’ve
 * picked it.)
 * </p>
 * <p>
 * The winner, and inaugural Wordsmith Extraordinaire of Riddler Nation,
 * will be the solver whose string generates the most points. You should use
 * {@link.uri https://norvig.com/ngrams/enable1.txt this word list} to
 * determine whether a word is valid.
 * </p>
 * <p>
 * For reference, this is the distribution of letter tiles in the bag, by
 * their point value:
 * </p>
<pre>
0: ?×2
1: E×12 A×9 I×9 O×8 N×6 R×6 T×6 L×4 S×4 U×4
2: D×4 G×3
3: B×2 C×2 M×2 P×2
4: F×2 H×2 V×2 W×2 Y×2
5: K
8: J X
10: Q Z
</pre>
 * {@ant.task}
 * <p>
 * The submitted solution:
 * </p>
<pre>
CARBOXYMETHYLCELLULOSEHAND_RAFTSMANS_IPWIREDRAWERDINITROBENZENEPETTIFOGGINGJUDOKAEQUATEVIVAAIOESOOIU 912
                          C         H
----------------------------------------------------------------------------------------------------
CARBOXYMETHYLCELLULOSE                                                                               46
CARBO                                                                                                9
CARB                                                                                                 8
CAR                                                                                                  5
 ARB                                                                                                 5
 AR                                                                                                  2
   BOXY                                                                                              16
   BOX                                                                                               12
   BO                                                                                                4
    OXY                                                                                              13
    OX                                                                                               9
       METHYLCELLULOSE                                                                               25
       METHYL                                                                                        14
       METH                                                                                          9
       MET                                                                                           5
       ME                                                                                            4
        ETHYL                                                                                        11
        ETH                                                                                          6
        ET                                                                                           2
         THY                                                                                         9
             CELLULOSE                                                                               11
             CELL                                                                                    6
             CEL                                                                                     5
              ELL                                                                                    3
              EL                                                                                     2
                  LOSE                                                                               4
                  LO                                                                                 2
                   OSE                                                                               3
                   OS                                                                                2
                     EH                                                                              5
                      HANDCRAFTSMANSHIP                                                              26
                      HANDCRAFTSMAN                                                                  21
                      HANDCRAFTS                                                                     16
                      HANDCRAFT                                                                      15
                      HAND                                                                           8
                      HA                                                                             5
                       AND                                                                           4
                       AN                                                                            2
                          CRAFTSMANSHIP                                                              18
                          CRAFTSMAN                                                                  13
                          CRAFTS                                                                     8
                          CRAFT                                                                      7
                           RAFTSMAN                                                                  13
                           RAFTS                                                                     8
                           RAFT                                                                      7
                            AFT                                                                      6
                                MANS                                                                 6
                                MAN                                                                  5
                                MA                                                                   4
                                   SHIP                                                              5
                                   SH                                                                1
                                    HIP                                                              4
                                    HI                                                               1
                                       WIREDRAWER                                                    17
                                       WIREDRAW                                                      15
                                       WIRED                                                         9
                                       WIRE                                                          7
                                        IRED                                                         5
                                        IRE                                                          3
                                         REDRAWER                                                    12
                                         REDRAW                                                      10
                                         RED                                                         4
                                         RE                                                          2
                                          ED                                                         3
                                           DRAWER                                                    10
                                           DRAW                                                      8
                                            RAWER                                                    8
                                            RAW                                                      6
                                             AWE                                                     6
                                             AW                                                      5
                                              WE                                                     5
                                               ER                                                    2
                                                 DINITROBENZENE                                      26
                                                 DINITRO                                             8
                                                 DIN                                                 4
                                                  IN                                                 2
                                                   NITROBENZENE                                      23
                                                   NITRO                                             5
                                                   NIT                                               3
                                                    IT                                               2
                                                      ROBE                                           6
                                                      ROB                                            5
                                                       OBE                                           5
                                                        BENZENE                                      18
                                                        BEN                                          5
                                                        BE                                           4
                                                         EN                                          2
                                                             NE                                      2
                                                               PETTIFOGGING                          20
                                                               PETTIFOG                              14
                                                               PETTI                                 7
                                                               PET                                   5
                                                               PE                                    4
                                                                  TI                                 2
                                                                   IF                                5
                                                                    FOGGING                          13
                                                                    FOG                              7
                                                                       GIN                           4
                                                                           JUDOKA                    18
                                                                           JUDO                      12
                                                                            UDO                      4
                                                                             DO                      3
                                                                              OKA                    7
                                                                               KA                    6
                                                                                AE                   2
                                                                                 EQUATE              15
                                                                                  QUATE              14
                                                                                  QUA                12
                                                                                    ATE              3
                                                                                    AT               2
                                                                                       VIVA          10
                                                                                           AI        2
                                                                                             OE      2
                                                                                               SO    2
</pre>
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
@AntTask("solve-riddle-2019-06-28")
@NoArgsConstructor @ToString
public class SolveRiddle20190628Task extends AbstractTask {
    private static final Path TMPDIR =
        Paths.get(System.getProperty("java.io.tmpdir"));

    private static final Comparator<CharSequence> COMPARATOR =
        comparing(CharSequence::toString, CASE_INSENSITIVE_ORDER);

    private final Wordlist wordlist = new Wordlist();
    private final WordMap wordmap = new WordMap(wordlist.keySet());
    private final StartsWithMap starts = new StartsWithMap(wordmap.keySet());
    private final EndsWithMap ends = new EndsWithMap(wordmap.keySet());
    private final Bag bag = new Bag();
    private final List<Tile> solution = new LinkedList<>();
    private final StringBuilder sequence = new StringBuilder();
    private final LinkedList<TreeMap<CharSequence,Integer>> score =
        new LinkedList<>();

    @Override
    public void execute() throws BuildException {
        super.execute();

        try {
            while (! bag.isEmpty()) {
                Optional<CharSequence> next = next();
                CharSequence subsequence =
                    next.orElseGet(() -> Tile.toString(bag));
                List<Tile> tiles = draw(subsequence, bag);

                play(subsequence, tiles);
            }

            int total =
                score
                .stream()
                .flatMap(t -> t.values().stream())
                .mapToInt(t -> t.intValue())
                .sum();

            log(Tile.toString(solution) + SPACE + total);

            StringBuilder subheader = new StringBuilder();

            for (int i = 0, n = solution.size(); i < n; i += 1) {
                if (solution.get(i).getLetter() != sequence.charAt(i)) {
                    subheader.append(sequence.charAt(i));
                } else {
                    subheader.append(SPACE);
                }
            }

            log(subheader.toString());
            log(repeat("-", solution.size()));

            for (int i = 0, n = score.size(); i < n; i += 1) {
                LinkedList<CharSequence> keys =
                    new LinkedList<>(score.get(i).keySet());

                keys.sort(comparingInt(CharSequence::length).reversed());

                for (CharSequence key : keys) {
                    StringBuilder line = new StringBuilder();

                    line.append(repeat(SPACE, i));
                    line.append(key);
                    line.append(repeat(SPACE,
                                       solution.size() - (i + key.length())));
                    line.append(SPACE)
                        .append(String.valueOf(score.get(i).get(key)));

                    log(line.toString());
                }
            }

            if (! bag.isEmpty()) {
                log("Remaining: " + Tile.toString(bag));
            }
        } catch (BuildException exception) {
            throw exception;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new BuildException(throwable);
        }
    }

    private Optional<CharSequence> next() {
        TreeMap<CharSequence,CharSequence> map = new TreeMap<>(COMPARATOR);

        for (int i = 1, j = sequence.length(); i <= j; i += 1) {
            String prefix = sequence.subSequence(i, j).toString();

            starts.subMap(prefix + Character.MIN_VALUE,
                          prefix + Character.MAX_VALUE)
                .keySet()
                .stream()
                .max(comparingInt(t -> potential(t)))
                .ifPresent(t -> map.put(prefix, t));
        }

        starts.keySet()
            .stream()
            .max(comparingInt(t -> potential(t)))
            .ifPresent(t -> map.put(EMPTY, t));

        Optional<CharSequence> next =
            map.entrySet()
            .stream()
            .max(comparingInt(t -> potential(t.getValue())))
            .map(t -> t.getValue().toString().substring(t.getKey().length()));

        return next;
    }
/*
    private Optional<CharSequence> next() {
        Optional<CharSequence> next =
            wordmap.keySet()
            .stream()
            .max(comparingInt(CharSequence::length));

        return next;
    }
*/
    private int potential(CharSequence sequence) {
        int potential = 0;

        Collection<CharSequence> words = wordmap.get(sequence);

        if (words != null) {
            potential =
                words.stream()
                .mapToInt(t -> wordlist.getOrDefault(t, 0))
                .sum();
        }

        return potential;
    }

    private void play(CharSequence subsequence, List<Tile> tiles) {
        if (tiles.size() == subsequence.length()) {
            solution.addAll(tiles);
            sequence.append(subsequence);

            while (score.size() < solution.size()) {
                score.add(new TreeMap<>(COMPARATOR));
            }

            for (int j = 1; j <= sequence.length(); j += 1) {
                for (int i = 0; i < j; i += 1) {
                    String substring =
                        sequence.subSequence(i, j).toString();

                    if (wordmap.keySet().remove(substring)) {
                        score.get(i)
                            .put(substring,
                                 solution.subList(i, j)
                                 .stream()
                                 .mapToInt(Tile::getPoints)
                                 .sum());
                    }
                }
            }

            wordmap.keySet().removeIf(t -> (! isPlayable(t, bag)));

            wordmap.values().forEach(t -> t.retainAll(wordmap.keySet()));
            wordmap.values().removeIf(Collection::isEmpty);

            starts.values().forEach(t -> t.retainAll(wordmap.keySet()));
            starts.values().removeIf(Collection::isEmpty);

            ends.values().forEach(t -> t.retainAll(wordmap.keySet()));
            ends.values().removeIf(Collection::isEmpty);
        } else {
            throw new IllegalStateException();
        }
    }

    private boolean isPlayable(CharSequence in, List<Tile> list) {
        List<Tile> out = draw(in, list);
        boolean isPlayable = (in.length() == out.size());

        list.addAll(out);

        return isPlayable;
    }

    private List<Tile> draw(CharSequence in, List<Tile> list) {
        LinkedList<Tile> out = new LinkedList<>();

        for (int character : in.codePoints().boxed().collect(toList())) {
            Optional<Tile> tile =
                list.stream()
                .filter(t -> t.getLetter() == character)
                .findFirst();

            if (! tile.isPresent()) {
                tile =
                    list.stream()
                    .filter(t -> t.getLetter() == '_')
                    .findFirst();
            }

            if (tile.isPresent()) {
                out.add(tile.get());
                list.remove(tile.get());
            } else {
                break;
            }
        }

        return out;
    }

    private class Wordlist extends TreeMap<CharSequence,Integer> {
        private static final long serialVersionUID = 8504887435537639563L;

        protected final Path path = TMPDIR.resolve(getClass().getSimpleName());

        public Wordlist() {
            super(COMPARATOR);

            try {
                if (Files.exists(path)) {
                    for (String line : Files.readAllLines(path)) {
                        String[] entry = line.split("=", 2);

                        computeIfAbsent(entry[0],
                                        k -> Integer.parseInt(entry[1]));
                    }
                } else {
                    try (BufferedReader reader = new ResourceReader()) {
                        Bag bag = new Bag();

                        reader.lines()
                            .map(t -> t.split("#", 2)[0])
                            .map(t -> t.split(SPACE, 2)[0])
                            .filter(t -> isNotBlank(t))
                            .map(t -> t.trim().toUpperCase())
                            .forEach(t -> put(t, null));

                        keySet().removeIf(t -> (! isPlayable(t, bag)));
                    }

                    keySet().stream()
                        .forEach(t -> computeIfAbsent(t,
                                                      k -> draw(t, new Bag())
                                                           .stream()
                                                           .mapToInt(Tile::getPoints)
                                                           .sum()));

                    Stream<String> stream =
                        entrySet().stream().map(String::valueOf);

                    Files.write(path, (Iterable<String>) stream::iterator);
                }
            } catch (Exception exception) {
                throw new ExceptionInInitializerError(exception);
            }
        }

        private class ResourceReader extends BufferedReader {
            public ResourceReader() {
                super(new InputStreamReader(SolveRiddle20190628Task.class.getResourceAsStream("enable1.txt")));
            }

            @Override
            public String toString() { return super.toString(); }
        }
    }

    private static abstract class XREF extends TreeMap<CharSequence,SortedSet<CharSequence>> {
        private static final long serialVersionUID = 3416903069379036321L;

        protected final Path path = TMPDIR.resolve(getClass().getSimpleName());

        protected XREF(Set<CharSequence> wordset) {
            super(COMPARATOR);

            try {
                if (Files.exists(path)) {
                    for (String line : Files.readAllLines(path)) {
                        String[] entry = line.split("=", 2);

                        computeIfAbsent(entry[0],
                                        k -> new TreeSet<>(COMPARATOR))
                            .addAll(Arrays.asList(entry[1].split(SPACE)));
                    }
                } else {
                    compute(wordset);

                    Stream<String> stream =
                        entrySet().stream().map(t -> format(t));

                    Files.write(path, (Iterable<String>) stream::iterator);
                }
            } catch (Exception exception) {
                throw new ExceptionInInitializerError(exception);
            }
        }

        protected abstract void compute(Set<CharSequence> wordset);

        private String format(Map.Entry<CharSequence,SortedSet<CharSequence>> entry) {
            return (String.valueOf(entry.getKey()) + "="
                    + entry.getValue().stream().collect(joining(SPACE)));
        }
    }

    private class WordMap extends XREF {
        private static final long serialVersionUID = -1880844619250978768L;

        public WordMap(Set<CharSequence> wordset) { super(wordset); }

        @Override
        protected void compute(Set<CharSequence> wordset) {
            for (CharSequence key : wordset) {
                key = key.toString();

                for (int j = 1; j <= key.length(); j += 1) {
                    for (int i = 0; i < j; i += 1) {
                        String value = key.toString().substring(i, j);

                        if (wordset.contains(value)) {
                            computeIfAbsent(key,
                                            k -> new TreeSet<>(COMPARATOR))
                                .add(value);
                        }
                    }
                }
            }
        }
    }

    private class StartsWithMap extends XREF {
        private static final long serialVersionUID = -2944553722487812425L;

        public StartsWithMap(Set<CharSequence> wordset) { super(wordset); }

        @Override
        protected void compute(Set<CharSequence> wordset) {
            for (CharSequence word : wordset) {
                for (int i = 0, j = word.length() - 1; j > 0; j -= 1) {
                    String key = word.subSequence(i, j).toString();

                    computeIfAbsent(key, k -> new TreeSet<>(COMPARATOR))
                        .add(word);
                }
            }
        }
    }

    private class EndsWithMap extends XREF {
        private static final long serialVersionUID = -5078727815938269143L;

        public EndsWithMap(Set<CharSequence> wordset) { super(wordset); }

        @Override
        protected void compute(Set<CharSequence> wordset) {
            for (CharSequence word : wordset) {
                for (int i = 1, j = word.length(); i < j; i += 1) {
                    String key = word.subSequence(i, j).toString();

                    computeIfAbsent(key, k -> new TreeSet<>(COMPARATOR))
                        .add(word);
                }
            }
        }
    }
}
