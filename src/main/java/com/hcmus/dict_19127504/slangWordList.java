package com.hcmus.dict_19127504;

import java.io.*;
import java.util.*;

public class slangWordList {

    private static slangWordList instanceSlangWord = null;
    private HashMap<String, ArrayList<String>> map;
    private HashMap<String, ArrayList<String>> historyMap;

    // constructor
    private slangWordList() throws FileNotFoundException {
        this.map = new HashMap<>();
        historyMap = new HashMap<>();
        this.start();
    }

    // singleton
    public static slangWordList getInstance() throws FileNotFoundException {
        if (instanceSlangWord == null)
            instanceSlangWord = new slangWordList();

        return instanceSlangWord;
    }

    public void start() throws FileNotFoundException {
        File file = new File("slang_hashmap.txt");
        File historyFile = new File("slang_history.txt");
        if (!file.exists()) {
            readData();
            saveHashMap();
        }
        else {
            loadHashMap();
        }
       if (historyFile.exists()) {
           loadHistory();
       }
    }
    // activity with slangWordList
    public HashMap<String, ArrayList<String>> getList() {
        return this.map;
    }

    public void setMeaningDuplicate(String word, String meaning) {
        ArrayList<String> meaningList = this.map.get(word);
        if (meaningList == null)
            meaningList = new ArrayList<>();
        meaningList.add(meaning);
        this.map.put(word, meaningList);
        saveHashMap();
    }

    public void setMeaningOverwrite(String word, String meaning) {
        ArrayList<String> meaningList = this.map.get(word);
        if (meaningList == null)
            meaningList = new ArrayList<>();
        meaningList.clear();
        meaningList.add(meaning);
        this.map.put(word, meaningList);
        saveHashMap();
    }

    public void readData() throws FileNotFoundException {
        File file = new File("slang_original.txt");
        if (!file.exists())
            System.out.println("File not found!");
        else {
            Scanner scanner = new Scanner(new FileInputStream(file));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] words = line.split("`");
                String[] words2;
                if (words.length == 2) {
                    words[0] = words[0].trim();
                    this.map.put(words[0], new ArrayList<>());
                    if (words[1].contains("|")) {
                        words2 = words[1].split("\\|");
                        for (String word : words2) {
                            this.map.get(words[0]).add(word.trim());
                        }
                    }
                    else {
                        this.map.get(words[0]).add(words[1].trim());
                    }
                }
            }
        }
    }

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
            oos.writeObject(this.map);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
            this.map = (HashMap<String, ArrayList<String>>) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> findSlangWord(String word) {
        ArrayList<String> list = this.map.get(word.toUpperCase(Locale.ROOT));
        if (list == null)
            return null;

        return list;
    }
    // history
    public HashMap<String, ArrayList<String>> getHistory() {
        return this.historyMap;
    }

    public void addHistory(String word, String time) {
        ArrayList<String> list = this.historyMap.get(word);
        if (list == null)
            list = new ArrayList<>();
        list.add(time);
        this.historyMap.put(word, list);
        saveHistory();
    }

    public void saveHistory() {
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
            // save hashMap to file
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.historyMap);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadHistory() {
        // load from file (slang_history.txt)
        File file = new File("slang_history.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            // load hashMap from file
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            this.historyMap = (HashMap<String, ArrayList<String>>) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, ArrayList<String>> findFromDefinition(String definition) {
        HashMap<String, ArrayList<String>> map = new HashMap<>();
        if (!definition.isEmpty()) {
            for (Map.Entry<String, ArrayList<String>> entry : this.map.entrySet())
                for (String word : entry.getValue())
                    if (word.toLowerCase(Locale.ROOT).contains(definition.toLowerCase(Locale.ROOT))) {
                        map.put(entry.getKey(), entry.getValue());
                    }
        }

        return map;
    }

    public void reset() throws FileNotFoundException {
        this.map.clear();
        readData();
        saveHashMap();
    }

    public boolean editSlangWord(String word, String meaning) {
        if (this.map.containsKey(word)) {
            this.map.get(word).clear();
            this.map.get(word).add(meaning);
            saveHashMap();
            return true;
        }
        return false;
    }

    public boolean deleteSlangWord(String word, String meaning) {
        if (this.map.containsKey(word)) {
            for (String s : this.map.get(word))
                if (s.equals(meaning)) {
                    this.map.get(word).remove(s);
                    if (this.map.get(word).size() == 0)
                        this.map.remove(word);
                    saveHashMap();
                    return true;
                }
        }
        return false;
    }

    public boolean checkMeaning(String word, String meaning) {
        if (this.map.containsKey(word))
            for (String s : this.map.get(word))
                if (s.toLowerCase(Locale.ROOT).equals(meaning.toLowerCase(Locale.ROOT)))
                    return true;
        return false;
    }
}
