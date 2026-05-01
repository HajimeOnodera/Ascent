package fun.ascent.skyblock.utility;

public class Progress {

    public int curProgress;
    public int reqProgress;

    public Progress(int reqProgress){
        this(0,reqProgress);
    }

    public Progress(int curProgress, int reqProgress){
        this.curProgress = curProgress;
        this.reqProgress = reqProgress;
    }

    public boolean add(int amount) {
        this.curProgress += amount;
        if(curProgress >= reqProgress){
            this.curProgress = 0;
            return true;
        }
        return false;
    }
}
