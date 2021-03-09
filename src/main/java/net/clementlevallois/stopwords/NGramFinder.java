/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.clementlevallois.stopwords;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.clementlevallois.utils.Multiset;

/**
 *
 * @author C. Levallois adapted from
 * http://stackoverflow.com/questions/3656762/n-gram-generation-from-a-sentence
 */
public class NGramFinder {

    private Multiset<String> freqSetN = new Multiset();
    private String[] words;
    private Set<String> setUniqueNGramsPerLine;
    private Multiset<String> setAllNGramsPerLine;
    private Multiset<String> multisetToReturn;
    private List<String> listStrings;

    public static void main(String[] args) {
        new NGramFinder("  but    although the law is ").runIt(4, false);

    }

    public NGramFinder(List<String> listStrings) {
        this.listStrings = listStrings;
    }

    public NGramFinder(String string) {
        this.listStrings = new ArrayList();
        if (string != null && !string.isEmpty()) {
            this.listStrings.add(string);
        }
    }

    public Multiset<String> runIt(int maxgram, boolean binary) {
//        Clock extractingNGramsPerLine = new Clock("extracting ngrams");
        multisetToReturn = new Multiset();

        for (String string : listStrings) {

            setAllNGramsPerLine = new Multiset();
            setAllNGramsPerLine.addAllFromMultiset(run(string, maxgram));

            //takes care of the binary counting.
            if (binary) {
                setUniqueNGramsPerLine = new HashSet();
                setUniqueNGramsPerLine.addAll(setAllNGramsPerLine.getElementSet());
                multisetToReturn.addAllFromListOrSet(setUniqueNGramsPerLine);
            } else {
                multisetToReturn.addAllFromMultiset(setAllNGramsPerLine);
            }

        }
//        extractingNGramsPerLine.addText("number of unique terms after nGram detection: " + multisetToReturn.elementSet().size());
//        extractingNGramsPerLine.closeAndPrintClock();
        return multisetToReturn;

    }

    private Multiset<String> ngrams(int n, String str) {

        Multiset<String> setToReturn = new Multiset();
        if (str == null) {
            return setToReturn;
        }
        words = str.split("\\s+");
        String concat;
        if (n == 1) {
            setToReturn.addAllFromListOrSet(Arrays.asList(words));
        } else {
            for (int i = 0; i < words.length - n + 1; i++) {
                concat = concat(words, i, i + n);
                if (!concat.isEmpty()) {
                    setToReturn.addOne(concat);
                }
            }
        }

        return setToReturn;

    }

    public static Multiset<String> ngramsFinderJustAGivenLength(int n, String str) {
        String[] words;
        Multiset<String> setToReturn = new Multiset();
        words = str.split(" ");
        for (int i = 0; i < words.length - n + 1; i++) {
            setToReturn.addOne(concatStatic(words, i, i + n));
        }

        return setToReturn;

    }

    private static String concatStatic(String[] words, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++) {
            sb.append(i > start ? " " : "").append(words[i].trim());
        }
        return sb.toString().trim();
    }

    private String concat(String[] words, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++) {
            sb.append(i > start ? " " : "").append(words[i].trim());
        }
        return sb.toString().trim();
    }

    private Multiset<String> run(String toBeParsed, int nGram) {
        freqSetN = new Multiset();

        for (int n = 1; n <= nGram; n++) {
            freqSetN.addAllFromMultiset(ngrams(n, toBeParsed));
        }
        //System.out.println(freqList.get(i));
        return freqSetN;
    }
}
