package com.hcmus.dict_19127504;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class slangWordList {
    private static slangWordList instanceSlangWord = null;
    private HashMap<String, ArrayList<String>> slangWord;

    private slangWordList() throws FileNotFoundException {
        this.slangWord = new HashMap<>();
        readData();
    }

    // instance method
    public static slangWordList getInstance() throws FileNotFoundException {
        if (instanceSlangWord == null) {
            instanceSlangWord = new slangWordList();
        }
        return instanceSlangWord;
    }
    public void readData() throws FileNotFoundException {
        File file = new File("slang.txt");
        if (!file.exists()) {
            System.out.println("File not found!");
        } else {
            Scanner scanner = new Scanner(new FileInputStream(file));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] words = line.split("`");
                String[] words2;
                if (words.length == 2) {
                    this.slangWord.put(words[0], new ArrayList<>());
                    if (words[1].contains("|")) {
                        words2 = words[1].split("\\|");
                        for (String word : words2) {
                            this.slangWord.get(words[0]).add(word);
                        }
                    }
                    else {
                        this.slangWord.get(words[0]).add(words[1]);
                    }
                }
            }
        }
    }

//    public void findSlangWord(String word) {
//        ArrayList<String> list = this.slangWord.get(word);
//        if (list != null) {
//            System.out.println("Slang word: " + list.toString());
//        } else {
//            System.out.println("Not found!");
//        }
//    }
    public void printSlangWord() {
        for (String key : this.slangWord.keySet()) {
            System.out.println(key + ": " + this.slangWord.get(key));
        }
    }


}
