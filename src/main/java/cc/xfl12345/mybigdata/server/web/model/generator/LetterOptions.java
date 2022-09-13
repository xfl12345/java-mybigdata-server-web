package cc.xfl12345.mybigdata.server.web.model.generator;

public class LetterOptions extends CharacterOptions {
    private boolean allUpperCase = false;

    private boolean allLowerCase = false;

    public boolean isAllUpperCase() {
        return allUpperCase;
    }

    public void setAllUpperCase(boolean allUpperCase) {
        this.allUpperCase = allUpperCase;
        if (allUpperCase && allLowerCase) {
            allLowerCase = false;
        }
    }

    public boolean isAllLowerCase() {
        return allLowerCase;
    }

    public void setAllLowerCase(boolean allLowerCase) {
        this.allLowerCase = allLowerCase;
        if (allUpperCase && allLowerCase) {
            allUpperCase = false;
        }
    }
}
