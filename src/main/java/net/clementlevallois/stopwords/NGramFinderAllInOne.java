package net.clementlevallois.stopwords;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.clementlevallois.utils;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
///**
// *
// * @author LEVALLOIS
// */
//public class NGramFinderAllInOne {
//
//    NGramDuplicatesCleaner cleaner;
//    NGramFinder ngramFinder;
//    Lemmatizer lemmatizer;
//    Multiset<String> ngrams;
//    boolean lemmatize;
//    StopWordsRemover stopwordsRemover;
//    Multiset<String> toBeRemoved;
//    int maxNGrams;
//    int maxOccurrencesPerDoc;
//    String lang;
//    boolean local;
//
//    public NGramFinderAllInOne(boolean lemmatize, int maxNGrams, int maxOccurrencesPerDoc, String lang) {
//        cleaner = new NGramDuplicatesCleaner(lang);
//        this.lemmatize = lemmatize;
//        stopwordsRemover = new StopWordsRemover(2, lang);
//        this.maxNGrams = maxNGrams;
//        this.maxOccurrencesPerDoc = maxOccurrencesPerDoc;
//        this.local = local;
//        this.lang = lang;
//
//    }
//
//    public Multiset<String> run(String terms) {
//
//        if (lemmatize) {
//            lemmatizer = new Lemmatizer(lang);
//            terms = lemmatizer.sentenceLemmatizer(terms);
//        }
//
//        ngramFinder = new NGramFinder(terms);
//
//        ngrams = ngramFinder.runIt(maxNGrams, false);
//
//        ngrams = cleaner.removeDuplicatesAndAllSingleTerms(ngrams, maxNGrams);
//        toBeRemoved = HashMultiset.create();
//
//        Iterator<String> it = ngrams.elementSet().iterator();
//        while (it.hasNext()) {
//            String next = it.next();
//            if (stopwordsRemover.shouldItBeRemoved(next)) {
//                toBeRemoved.add(next, ngrams.count(next));
//            }
//        }
//        ngrams.removeAll(toBeRemoved);
//
//        //we limit ngrams count to a maximum of 3 per the text
//        //the reason is that counting multiple ngrams beyond 3 occurrences tends to give too much prominence to terms that appear many times, but just in one text.
//        Multiset<String> ngramsTemp = HashMultiset.create(ngrams);
//        ngrams = HashMultiset.create();
//        ngramsTemp.entrySet().stream().forEach((entry) -> {
//            if (entry.getCount() > 3) {
//                ngrams.add(entry.getElement(), maxOccurrencesPerDoc);
//            } else {
//                ngrams.add(entry.getElement(), entry.getCount());
//            }
//        });
//
//        return ngrams;
//    }
//
//}
