package com.sumitdev.voicedo;

public class Content {
    String todo, path, booleancheck;
    int uid;


    public Content(String todo, String path, String booleancheck, int uid) {
        this.todo = todo;
        this.path = path;
        this.booleancheck = booleancheck;
        this.uid = uid;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBooleancheck() {
        return booleancheck;
    }

    public void setBooleancheck(String booleancheck) {
        this.booleancheck = booleancheck;
    }
}
