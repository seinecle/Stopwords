/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.clementlevallois.stopwords;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import net.clementlevallois.stopwords.resources.PlaceHolder;
import net.clementlevallois.utils.ResourceLoader;

/*
 Copyright 2008-2013 Clement Levallois
 Authors : Clement Levallois <clementlevallois@gmail.com>
 Website : http://www.clementlevallois.net


 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

 Copyright 2013 Clement Levallois. All rights reserved.

 The contents of this file are subject to the terms of either the GNU
 General Public License Version 3 only ("GPL") or the Common
 Development and Distribution License("CDDL") (collectively, the
 "License"). You may not use this file except in compliance with the
 License. You can obtain a copy of the License at
 http://gephi.org/about/legal/license-notice/
 or /cddl-1.0.txt and /gpl-3.0.txt. See the License for the
 specific language governing permissions and limitations under the
 License.  When distributing the software, include this License Header
 Notice in each file and include the License files at
 /cddl-1.0.txt and /gpl-3.0.txt. If applicable, add the following below the
 License Header, with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"

 If you wish your version of this file to be governed by only the CDDL
 or only the GPL Version 3, indicate your decision by adding
 "[Contributor] elects to include this software in this distribution
 under the [CDDL or GPL Version 3] license." If you do not indicate a
 single choice of license, a recipient has the option to distribute
 your version of this file under either the CDDL, the GPL Version 3 or
 to extend the choice of license to its licensees as provided above.
 However, if you add GPL Version 3 code and therefore, elected the GPL
 Version 3 license, then the option applies only if the new code is
 made subject to such option by the copyright holder.

 Contributor(s): Clement Levallois

 */
public class Stopwords {

    private static final String[] twitterStopWords = {"rt", "w/"};
    private static final String[] commonStopWords = {"and", "for", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "20", "25", "30", "40", "50", "100", "1000"};

    private static Map<String, Map<String, Set<String>>> cache = new HashMap();
    private static Map<String, Set<String>> cacheTwitter = new HashMap();

    public static void main(String args[]) throws URISyntaxException, IOException, Exception {
        Stopwords app = new Stopwords();
        InputStream fileFromResourceAsStream = app.getInputStreamFromResource("twitter/en.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fileFromResourceAsStream));
        String readLine = br.readLine();
        System.out.println("line: " + readLine);
        Map<String, Set<String>> stopWords = Stopwords.getStopWords("en");
        System.out.println(stopWords.toString());
//        ResourceLoader.("twitter/en.txt");
//        List<String> readAllLines = Files.readAllLines(Paths.get(url.toURI()));
//        System.out.println("text: "+readAllLines.get(0));

    }

    public static Map<String, Set<String>> getStopWords(String lang) throws Exception {

        if (cache.containsKey(lang)) {
            return cache.get(lang);
        }
        Set<String> stopWords = new HashSet();
        Set<String> shortStopWords = new HashSet();
        Map<String, Set<String>> pair = null;
        InputStream inputStream;
        URL resource;

        resource = PlaceHolder.class.getResource(lang + ".txt");
        if (resource != null) {
            inputStream = PlaceHolder.class.getResourceAsStream(lang + ".txt");
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                Stream<String> lines = br.lines();
                lines.forEach(l -> stopWords.add(l));
            } catch (Exception e) {
                System.out.println("exception is: " + e.toString());
                e.printStackTrace();
            }
        }
        resource = PlaceHolder.class.getResource(lang + "_short.txt");
        if (resource != null) {
            inputStream = PlaceHolder.class.getResourceAsStream(lang + "_short.txt");
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                Stream<String> lines = br.lines();
                lines.forEach(l -> shortStopWords.add(l));
            } catch (Exception e) {
                System.out.println("exception is: " + e.toString());
                e.printStackTrace();
            }
        }

        pair = new HashMap();
        pair.put("short", shortStopWords);
        pair.put("long", stopWords);
        cache.put(lang, pair);

        return pair;
    }

    public static Set<String> getTwitterStopwords(boolean longList) throws FileNotFoundException, IOException, Exception {
        Set<String> words = new HashSet();

        InputStream inputStream;
        URL resource;

        resource = PlaceHolder.class.getResource("twitter_long.txt");
        if (resource != null) {
            inputStream = PlaceHolder.class.getResourceAsStream("twitter_long.txt");
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                Stream<String> lines = br.lines();
                lines.forEach(l -> words.add(l));
            } catch (Exception e) {
                System.out.println("exception is: " + e.toString());
                e.printStackTrace();
            }
        }
        resource = PlaceHolder.class.getResource("twitter_short.txt");
        if (resource != null) {
            inputStream = PlaceHolder.class.getResourceAsStream("twitter_short.txt");
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                Stream<String> lines = br.lines();
                lines.forEach(l -> words.add(l));
            } catch (Exception e) {
                System.out.println("exception is: " + e.toString());
                e.printStackTrace();
            }
        }
        return words;
    }

    private InputStream getInputStreamFromResource(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }
}
