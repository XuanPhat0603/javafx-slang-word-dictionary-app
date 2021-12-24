package com.hcmus.dict_19127504;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class slangWordList {
    private static slangWordList instanceSlangWord = null;
    private HashMap<String, ArrayList<String>> forwardMap;
    private HashMap<String, ArrayList<String>> backwardMap;

    private slangWordList() throws FileNotFoundException {
        this.forwardMap = new HashMap<>();
        this.backwardMap = new HashMap<>();
        this.start();
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
                    words[0] = words[0].trim();
                    this.forwardMap.put(words[0], new ArrayList<>());
                    if (words[1].contains("|")) {
                        words2 = words[1].split("\\|");
                        for (String word : words2) {
                            this.forwardMap.get(words[0]).add(word.trim());
                        }
                    }
                    else {
                        this.forwardMap.get(words[0]).add(words[1].trim());
                    }
                }
            }
        }
    }

    public ArrayList<String> findSlangWord(String word) {
        ArrayList<String> list = this.forwardMap.get(word);
        if (list == null) {
            return null;
        }
        return list;
    }

    public void findDefine(String word) {
        // get arrayList of slangWord
        ArrayList<String> list = this.forwardMap.get(word);
        if (list == null) {
            System.out.println("Not found!");
        } else {
            for (String word2 : list) {
                System.out.println(word2);
            }
        }
    }

    public HashMap<String, ArrayList<String>> getList() {
        return this.forwardMap;
    }

    // save hashMap to file (slang_hashmap.txt)
    public void saveHashMap() {
        File file = new File("slang_hashmap.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            // save hashMap to file
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.forwardMap);
            oos.writeObject(this.backwardMap);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // load hashMap from file (slang_hashmap.txt)
    public void loadHashMap() {
        File file = new File("slang_hashmap.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            // load hashMap from file
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            this.forwardMap = (HashMap<String, ArrayList<String>>) ois.readObject();
            this.backwardMap = (HashMap<String, ArrayList<String>>) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() throws FileNotFoundException {
        File file = new File("slang_hashmap.txt");
        if (!file.exists()) {
            readData();
            for (String key : this.forwardMap.keySet()) {
                ArrayList<String> list = this.forwardMap.get(key);
                for (String word : list) {
                    if (this.backwardMap.get(word) == null) {

                        this.backwardMap.put(word, new ArrayList<>());

                    }
                    this.backwardMap.get(word).add(key);
                }
            }
            saveHashMap();
        }
        else {
            System.out.println("HashMap already exists!");
            loadHashMap();
            print();
        }
    }

    public void saveHistory(String word) {
        // save to file (slang_history.txt)
        File file = new File("slang_history.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(word + "\n");
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void print() {
//        for (String key : this.forwardMap.keySet()) {
//            System.out.println(key + ": " + this.forwardMap.get(key));
//        }

        // print all slang words
        for (String key : this.backwardMap.keySet()) {
            System.out.println(key + ": " + this.backwardMap.get(key));
        }

    }

    public HashMap<String, ArrayList<String>> findDefinition(String definition) {
        // find slangword contain definition from backwardMap

        HashMap<String, ArrayList<String>> map = new HashMap<>();
        for (String key : this.backwardMap.keySet()) {
            if (key.toLowerCase(Locale.ROOT).contains(definition.toLowerCase(Locale.ROOT))) {
                map.put(key, this.backwardMap.get(key));
                //System.out.println(key + ": " + this.backwardMap.get(key));
            }
        }

        return map;
    }
}
