/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.clementlevallois.stopwords;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author LEVALLOIS
 */
public class Lemmatizer {

    private String[] noLemmaEN = new String[]{"access", "accumbens", "addresses", "afterwards", "always", "approaches", "analyses", "biases", "businesses", "classes", "crises", "discusses", "economics", "ethics", "focuses", "fries", "goes", "humanities", "hypotheses", "inches", "lies", "losses", "physics", "politics", "premises", "processes", "red", "ries", "series", "sometimes", "species", "spring", "status", "themselves", "neural processes", "witnesses"};
    private String[] noLemmaFR = new String[]{"cours", "sens", "puis", "temps", "parcours", "près", "auprès", "outils", "secours", "sommes", "travers", "pays", "concours", "êtes", "divers", "éthos", "alors", "corps", "ouvrirons", "univers", "sans"};
    private String[] noLemma = new String[]{"analytics", "accumbens", "aws", "bayes", "business", "charles", "ects", "cnrs", "cowles", "deep learning", "developer", "ethos", "faas", "forbes", "iaas", "james", "keynes", "koopmans", "nhs", "paas", "programming", "reactjs", "saas", "vuejs", "united states"};

    // el/elle e/é i/î ie/ique al/ale
    private Set<String> noLemmaSet;
    private String lang;

    public Lemmatizer(String lang) throws Exception {
        switch (lang) {
            case "en":
                noLemmaSet = new HashSet(Arrays.asList(noLemmaEN));
                break;
            case "fr":
                noLemmaSet = new HashSet(Arrays.asList(noLemmaFR));
                break;
            default:
                noLemmaSet = new HashSet();
                break;
        }
        noLemmaSet.addAll(Stopwords.getStopWords(lang).get("long"));
        noLemmaSet.addAll(Arrays.asList(noLemma));
        this.lang = lang;
    }

    public String lemmatize(String term) {

        String currEntry = term.toLowerCase();
        String[] terms = currEntry.split(" ");
        String lastTerm = terms[terms.length - 1];
        if (noLemmaSet.contains(lastTerm)) {
            return currEntry.trim();
        }

        if ((currEntry.endsWith("s") | currEntry.endsWith("s'"))
                && !currEntry.endsWith("us")
                && !currEntry.endsWith("as")
                && !currEntry.endsWith("ss")
                && !currEntry.endsWith("ies")
                && !noLemmaSet.contains(currEntry)
                && !currEntry.endsWith("is")) {
            if (currEntry.endsWith("s")) {
                currEntry = currEntry.substring(0, currEntry.length() - 1);
            }
            if (currEntry.endsWith("s'")) {
                currEntry = currEntry.substring(0, currEntry.length() - 2);
            }

        } else if (currEntry.endsWith("'")) {
            currEntry = currEntry.substring(0, currEntry.length() - 1);
        }

        if (lang.equals("en")) {
            if (currEntry.endsWith("ies")) {
                currEntry = currEntry.substring(0, currEntry.length() - 3) + "y";
            } else if (currEntry.endsWith("'s")) {
                currEntry = currEntry.substring(0, currEntry.length() - 2);
            } else if (currEntry.endsWith("ed")) {
                if (currEntry.endsWith("lked") | currEntry.endsWith("ssed") | currEntry.endsWith("ned")) {
                    currEntry = currEntry.substring(0, currEntry.length() - 2);
                } else if (currEntry.endsWith("ied")) {
                    currEntry = currEntry.substring(0, currEntry.length() - 3) + "y";
                } else {
                    // purchased -> purchase
                    currEntry = currEntry.substring(0, currEntry.length() - 1);
                }
            } else if (currEntry.endsWith("'s")) {
                currEntry = currEntry.substring(0, currEntry.length() - 2);
            } else if (currEntry.endsWith("ing")) {
                currEntry = currEntry.substring(0, currEntry.length() - 3);
            } else if (currEntry.endsWith("ier")) {
                currEntry = currEntry.substring(0, currEntry.length() - 3) + "y";
            }
        }
        if (lang.equals("fr")) {
            if (currEntry.endsWith("ère")) {
                currEntry = currEntry.substring(0, currEntry.length() - 3) + "er";
            }
        }
        return currEntry.trim();

    }

    public String sentenceLemmatizer(String sentence) {

        String[] terms = sentence.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String term : terms) {
            sb.append(lemmatize(term));
            sb.append(" ");
        }
        return sb.toString().trim();
    }

}
