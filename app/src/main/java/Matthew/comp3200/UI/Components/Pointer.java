package Matthew.comp3200.UI.Components;

public class Pointer {
    int xOrigin;
    int x;
    int yOrigin;
    int y;
    int xDif;
    int yDif;

    void reset(int newXVal, int newYVal){
        xOrigin = newXVal;
        x = newXVal;
        yOrigin = newYVal;
        y = newYVal;
    }

    void vectorDistance(){
        //1. check if pointer is still in view's bounds
        x = Math.max(0,x);
        y = Math.max(0,y);
        //2. calculate distance between origin cords and current cords
        xDif = Math.max(-127,Math.min(x - xOrigin,127));
        yDif = Math.max(-127,Math.min(y - yOrigin,127));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getxDif() {
        return xDif;
    }

    public int getyDif() {
        return yDif;
    }
}
