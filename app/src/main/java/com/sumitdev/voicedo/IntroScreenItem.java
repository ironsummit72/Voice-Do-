package com.sumitdev.voicedo;

public class IntroScreenItem {
    String Title, Description;
    int screenimage;

    public IntroScreenItem() {
    }
    public IntroScreenItem(String title, String description, int screenimage) {
        Title = title;
        Description = description;
        this.screenimage = screenimage;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getScreenimage() {
        return screenimage;
    }

    public void setScreenimage(int screenimage) {
        this.screenimage = screenimage;
    }
}
