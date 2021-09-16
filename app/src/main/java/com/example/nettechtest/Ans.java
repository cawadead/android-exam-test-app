package com.example.nettechtest;

public class Ans {
    private String ans_text;
    private Integer correct;
    public Ans(String ans_text, Integer correct){
        this.ans_text = ans_text;
        this.correct = correct;
    }

    public String getAns_text() {
        return ans_text;
    }
    public Integer getCorrect() {
        return correct;
    }
    public void setAns_text(String ans_text) {
        this.ans_text = ans_text;
    }
    public void setCorrect(Integer correct) {
        this.correct = correct;
    }
}