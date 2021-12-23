package com.hcmus.dict_19127504;

import java.util.ArrayList;

public class slangWord {
    private String word;
    private String meaning;


    public slangWord(String word, String meaning){
        this.word = word;
        this.meaning = meaning;
    }

    public String getWord(){
        return word;
    }

//    public ArrayList<String> getMeaning(){
//        return meaning;
//    }

    public void setWord(String word){
        this.word = word;
    }

//    public void setMeaning(ArrayList<String> meaning){
//        this.meaning = meaning;
//    }

    public String toString(){
        String result = "";
        result += "Word: " + word + "\n";
        result += "Meaning: " + meaning.toString() + "\n";
        return result;
    }

//    public void addMeaning(String meaning){
//        this.meaning.add(meaning);
//    }

//    public void removeMeaning(String meaning){
//        this.meaning.remove(meaning);
//    }

}
