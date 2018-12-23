package com.example.islam.project;

public class AzkarParam {
    private int arabicArrayID, languageArrayID, pronunciationArrayID, referenceArrayID;

    public AzkarParam(int arabicArrayID, int languageArrayID, int pronunciationArrayID, int referenceArrayID) {
        this.arabicArrayID = arabicArrayID;
        this.languageArrayID = languageArrayID;
        this.pronunciationArrayID = pronunciationArrayID;
        this.referenceArrayID = referenceArrayID;
    }

    public int getArabicArrayID() {
        return arabicArrayID;
    }

    public int getLanguageArrayID() {
        return languageArrayID;
    }

    public int getPronunciationArrayID() {
        return pronunciationArrayID;
    }

    public int getReferenceArrayID() {
        return referenceArrayID;
    }
}
