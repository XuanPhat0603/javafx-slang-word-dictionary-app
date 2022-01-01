package com.hcmus.dict_19127504;

import java.io.*;
import java.util.*;

public class slangWordList {

    // save slangWordList to hashmap
    private static slangWordList instanceSlangWord = null;
    private HashMap<String, ArrayList<String>> map;
    private ArrayList<history> historyList;

    // constructor
    private slangWordList() throws FileNotFoundException {
        this.map = new HashMap<>();
        historyList = new ArrayList<>();
        this.start();
    }

    // singleton slangWordList object
    public static slangWordList getInstance() throws FileNotFoundException {
        if (instanceSlangWord == null)
            instanceSlangWord = new slangWordList();

        return instanceSlangWord;
    }

    // check file: history + data structure is exist
    public void start() throws FileNotFoundException {
        File file = new File("slang_hashmap.dat");
        File historyFile = new File("slang_history.dat");
        if (!file.exists()) {
            readData();
        }
        else {
            loadHashMap();
        }
       if (historyFile.exists()) {
           loadHistory();
       }
    }

    // reset data
    public void reset() throws FileNotFoundException {
        this.map.clear();
        readData();
        saveHashMap();
    }

    //---------------------- activity with slangWordList------------------

    // get hashmap
    public HashMap<String, ArrayList<String>> getList() {
        return this.map;
    }

    // add new slangWord duplicate
    public void setMeaningDuplicate(String word, String meaning) {
        ArrayList<String> meaningList = this.map.get(word);
        if (meaningList == null)
            meaningList = new ArrayList<>();
        meaningList.add(meaning);
        this.map.put(word, meaningList);
        saveHashMap();
    }

    // add new slangWord overwrite
    public void setMeaningOverwrite(String word, String meaning) {
        ArrayList<String> meaningList = this.map.get(word);
        if (meaningList == null)
            meaningList = new ArrayList<>();
        meaningList.clear();
        meaningList.add(meaning);
        this.map.put(word, meaningList);
        saveHashMap();
    }

    // read data from file
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
            saveHashMap();
        }
    }

    // save hashmap to file
    public void saveHashMap() {
        File file = new File("slang_hashmap.dat");
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

    // load hashmap from file while structure is exist
    public void loadHashMap() {
        File file = new File("slang_hashmap.dat");
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

    // find slangWord use searchWord
    public ArrayList<String> findSlangWord(String word) {
        ArrayList<String> meaningList = new ArrayList<>();
        if (this.map.containsKey(word)) {
            meaningList = this.map.get(word);
        }
        return meaningList;
    }

    // find slangword use definition
    public HashMap<String, ArrayList<String>> findFromDefinition(String definition) {
        HashMap<String, ArrayList<String>> map = new HashMap<>();
        if (!definition.isEmpty()) {
            for (Map.Entry<String, ArrayList<String>> entry : this.map.entrySet()) {
                for (String word : entry.getValue()) {
                    ArrayList<String> meaningList = new ArrayList<>();
                    if (word.toLowerCase(Locale.ROOT).startsWith(definition.toLowerCase(Locale.ROOT))) {
                        meaningList.add(word);
                    }
                    if (!meaningList.isEmpty()) {
                        map.put(entry.getKey(), meaningList);
                    }
                }
            }
        }

        return map;
    }

    // edit slangword
    public boolean editSlangWord(String word, String oldMeaning, String newMeaning) {
        if (this.map.containsKey(word)) {
            ArrayList<String> list = this.map.get(word);
            if (list.contains(oldMeaning)) {
                list.remove(oldMeaning);
                list.add(newMeaning);
                this.map.put(word, list);
                saveHashMap();
                return true;
            }
            return false;
        }
        return false;
    }

    // delete slangword
    public boolean deleteSlangWord(String word, String meaning) {
        if (this.map.containsKey(word)) {
            for (String s : this.map.get(word)) {
                if (s.equals(meaning)) {
                    this.map.get(word).remove(s);
                    if (this.map.get(word).size() == 0) {
                        this.map.remove(word);
                    }
                    saveHashMap();
                    return true;
                }
            }
        }
        return false;
    }

    // check slangword is exist
    public boolean checkWord(String word) {
        return this.map.containsKey(word);
    }

    // check meaning is exist
    public boolean checkMeaning(String word, String meaning) {
        if (this.map.containsKey(word))
            for (String s : this.map.get(word))
                if (s.toLowerCase(Locale.ROOT).equals(meaning.toLowerCase(Locale.ROOT)))
                    return true;
        return false;
    }

    //-------------------------History-------------------------------

    // get history
    public ArrayList<history> getHistory() {
        return this.historyList;
    }

    // add history
    public void addHistory(String word, String time) {
        this.historyList.add(new history(word, time));
        saveHistory();
    }

    // save history to file
    public void saveHistory() {
        // save to file (slang_history.txt)
        File file = new File("slang_history.dat");
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
            oos.writeObject(this.historyList);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // load history from file
    public void loadHistory() {
        // load from file (slang_history.txt)
        File file = new File("slang_history.dat");
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
            this.historyList = (ArrayList<history>) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
