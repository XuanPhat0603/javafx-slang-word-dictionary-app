package com.hcmus.dict_19127504;

import java.io.Serializable;

public class history implements Serializable {
    private String word;
    private String time;

    public history(String word, String time) {
        this.word = word;
        this.time = time;
    }

    public String getWord() {
        return word;
    }

    public String getTime() {
        return time;
    }
}
