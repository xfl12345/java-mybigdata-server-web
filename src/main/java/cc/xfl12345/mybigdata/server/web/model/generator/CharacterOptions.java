package cc.xfl12345.mybigdata.server.web.model.generator;

public class CharacterOptions {
    private boolean containMyself = true;

    private boolean allMyself = false;

    private int limitCount = -1;

    public boolean isContainMyself() {
        return containMyself;
    }

    public void setContainMyself(boolean containMyself) {
        this.containMyself = containMyself;
        if (!containMyself) {
            allMyself = false;
            limitCount = 0;
        }
    }

    public boolean isAllMyself() {
        return allMyself;
    }

    public void setAllMyself(boolean allMyself) {
        this.allMyself = allMyself;
        if (allMyself) {
            containMyself = true;
            limitCount = -1;
        }
    }

    public int getLimitCount() {
        return limitCount;
    }

    public void setLimitCount(int limitCount) {
        this.limitCount = limitCount;
    }
}
