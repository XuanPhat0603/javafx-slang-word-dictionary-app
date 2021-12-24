package com.hcmus.dict_19127504;

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

    public String getMeaning() {
        return meaning;
    }

    public String toString(){
        String result = "";
        result += "Word: " + word + "\n";
        result += "Meaning: " + meaning.toString() + "\n";
        return result;
    }
}
