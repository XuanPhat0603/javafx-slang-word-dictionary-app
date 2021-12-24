package com.hcmus.dict_19127504;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class slangWordList {
    private static slangWordList instanceSlangWord = null;
    private HashMap<String, ArrayList<String>> slangWord;

    private slangWordList() throws FileNotFoundException {
        this.slangWord = new HashMap<>();
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
                    this.slangWord.put(words[0], new ArrayList<>());
                    if (words[1].contains("|")) {
                        words2 = words[1].split("\\|");
                        for (String word : words2) {
                            this.slangWord.get(words[0]).add(word.trim());
                        }
                    }
                    else {
                        this.slangWord.get(words[0]).add(words[1]);
                    }
                }
            }
        }
    }

    public void findSlangWord(String word) {
        ArrayList<String> list = this.slangWord.get(word);
        if (list != null) {
            System.out.println("Slang word: " + list.toString());
        } else {
            System.out.println("Not found!");
        }
    }

    public HashMap<String, ArrayList<String>> getList() {
        return this.slangWord;
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
            oos.writeObject(this.slangWord);
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
            long startTime = System.currentTimeMillis();
            // load hashMap from file
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            this.slangWord = (HashMap<String, ArrayList<String>>) ois.readObject();
            ois.close();
            fis.close();
            long endTime = System.currentTimeMillis();
            System.out.println("Execution time: " + (endTime - startTime) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() throws FileNotFoundException {
        File file = new File("slang_hashmap.txt");
        if (!file.exists()) {
            readData();
        } else
            loadHashMap();
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
}
