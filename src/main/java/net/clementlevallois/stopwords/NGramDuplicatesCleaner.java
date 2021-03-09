/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.clementlevallois.stopwords;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.clementlevallois.utils.Multiset;

/**
 *
 * @author C. Levallois
 */
public class NGramDuplicatesCleaner {

    Set<String> stopWords;
    Multiset<String> multisetWords;

    Iterator<Map.Entry<String, Integer>> itFreqList;
    Map.Entry<String, Integer> entry;
    Set<String> wordsToBeRemoved;
    String currWord;
    Set<String> setCurrentSubNGrams;
    Iterator<String> setCurrentSubNGramsIterator;
    String string;
    String[] termsInBigram;

    public NGramDuplicatesCleaner(String lang) throws Exception {
        stopWords = Stopwords.getStopWords(lang).get("long");
    }

    public NGramDuplicatesCleaner(Set<String> stopwordsParameter) {
        stopWords = stopwordsParameter;

    }

    public Multiset<String> removeDuplicates(Multiset<String> setNGrams, int maxGrams, boolean removeSingleTerms) {

        // this factor determines to what extent, if "European Union" is frequently used, then "Union" alone should not be removed from relevant ngrams
        // the higher the factor, the harder it is for "Union" to remain.
        // rule of thumb: on a small text, this factor should be higher.
        float factorRemovingIrrelevantUnigrams;
        if (setNGrams.getElementSet().size() < 500) {
            factorRemovingIrrelevantUnigrams = 2f;
        } else {
            factorRemovingIrrelevantUnigrams = 1.5f;
        }

        multisetWords = new Multiset();
        wordsToBeRemoved = new HashSet();
        itFreqList = setNGrams.getEntrySet().iterator();

        //we start by removing all terms that appear just once in the corpus.
        // why? because we assume these terms are of null interest
        while (itFreqList.hasNext()) {
            entry = itFreqList.next();
            if (entry.getValue() == 1 && removeSingleTerms) {
                itFreqList.remove();
            }
        }
        System.out.println("number of ngrams after purge of unique terms: " + setNGrams.getSize());

        // then we remove the terms which appear less frequently individually than in combined expressions 
        for (int i = maxGrams - 1; i > 0; i--) {
            itFreqList = setNGrams.getEntrySet().iterator();
            while (itFreqList.hasNext()) {
                entry = itFreqList.next();
                currWord = entry.getKey().trim();
                long countIn = currWord.chars().filter(ch -> ch == ' ').count();
                if (countIn == i) {
                    //special condition for i = 1 : two terms separated by a blank space
                    // since this is a very simple case that does not need a heavy duty n-gram detection approach
                    if (i == 1) {
                        termsInBigram = currWord.split(" ");
                        String term1 = termsInBigram[0].trim();
                        String term2 = termsInBigram[1].trim();

                        if (stopWords.contains(term1) & stopWords.contains(term2)) {
                            wordsToBeRemoved.add(term1);
                            wordsToBeRemoved.add(term2);
                            wordsToBeRemoved.add(currWord);
                        }
                        if (!stopWords.contains(term1) & !stopWords.contains(term2)) {
                            if (setNGrams.getCount(term1) < entry.getValue() * factorRemovingIrrelevantUnigrams) {
                                wordsToBeRemoved.add(term1.trim());
                            }
                            if (setNGrams.getCount(term2) < entry.getValue() * factorRemovingIrrelevantUnigrams) {
                                wordsToBeRemoved.add(term2.trim());
                            }
                        }

                    } else {
                        setCurrentSubNGrams = NGramFinder.ngramsFinderJustAGivenLength(i, currWord).getElementSet();
                        setCurrentSubNGramsIterator = setCurrentSubNGrams.iterator();
                        while (setCurrentSubNGramsIterator.hasNext()) {
                            string = setCurrentSubNGramsIterator.next().trim();
                            if (setNGrams.getElementSet().contains(string)) {
                                if (setNGrams.getCount(string) < entry.getValue() * factorRemovingIrrelevantUnigrams) {
                                    wordsToBeRemoved.add(string);
                                }
                                // example: "European Union" is used 2 times in the text,
                                // the term "union" is used 3 times: twice in "European Union", once separately
                                // we count: how many times is "union" used outside of "European Union"?
                                // (hence the substraction)
                                // if this freq is lower than the freq of "European Union" x 1.5, then remove "Union".

                            }
                        }
                    }
                }
            }
        }

        itFreqList = setNGrams.getEntrySet().iterator();
        while (itFreqList.hasNext()) {
            boolean toRemain;
            entry = itFreqList.next();
            currWord = entry.getKey();

            toRemain = wordsToBeRemoved.add(currWord);

            if (toRemain & !stopWords.contains(currWord)) {
                multisetWords.addSeveral(entry.getKey().trim(), entry.getValue());
            }

        }

        return multisetWords;

    }
}
