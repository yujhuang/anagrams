package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private static ArrayList<String> wordList = new ArrayList<String>();
    private static HashSet<String> wordSet = new HashSet<String>();
    private static HashMap<String,ArrayList<String>> lettersToWord = new HashMap<String,ArrayList<String>>();
    private static HashMap<Integer,ArrayList<String>> sizeToWords = new HashMap<Integer,ArrayList<String>>();
    private static int wordLength = DEFAULT_WORD_LENGTH;
    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            Integer length = word.length();
            if(sizeToWords.containsKey(length)){
                sizeToWords.get(length).add(word);
            }else {
                ArrayList<String> newword = new ArrayList<String>();
                newword.add(word);
                sizeToWords.put(length,newword);
            }

            String sorted = sortLetters(word);
            if(lettersToWord.containsKey(sorted)){
                lettersToWord.get(sorted).add(word);
            }else {
                ArrayList<String> newword = new ArrayList<String>();
                newword.add(word);
                lettersToWord.put(sorted,newword);
            }

        }
    }

    public String sortLetters(String target) {
        char[] temp = target.toCharArray();
        Arrays.sort(temp);
        return String.valueOf(temp);
    }

    public boolean isGoodWord(String word, String base) {
        if(wordSet.contains(word) && !(word.contains(base))) {
            return true;
        }
        return false;

    }

    public ArrayList<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String sorted = sortLetters(targetWord);
        for(String current: wordList) {
            if(current.length() == sorted.length() && sorted.equals(sortLetters(current))) {
                if(!current.equals(targetWord)) {
                    result.add(current);
                    Log.d("Adding: ",current);
                }
            }
        }
        for(String more: getAnagramsWithOneMoreLetter(targetWord)){
            result.add(more);
            Log.d("Adding: ",more);
        }
        return result;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(char alphabet = 'a'; alphabet <= 'z';alphabet++) {
            String temp = word + alphabet;
            String sorted = sortLetters(temp);
            if(lettersToWord.containsKey(sorted)){
                for(String element: lettersToWord.get(sorted)) {
                    if(!result.contains(element))
                         result.add(element);
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        int randomNumber;
        String starterWord;

        do {
            randomNumber = random.nextInt(sizeToWords.get(wordLength).size());
            starterWord = sizeToWords.get(wordLength).get(randomNumber);
        } while (getAnagramsWithOneMoreLetter(starterWord).size() < MIN_NUM_ANAGRAMS);

        if (wordLength < MAX_WORD_LENGTH) {
            wordLength++;
        }

        return starterWord;



    }
}
