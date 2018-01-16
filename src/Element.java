/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reversifx;

/**
 *
 * @author flug
 */
public class Element {

    private int x;
    private int y;
    private int counter;

    public Element(int x, int y) {
        this.x = x;
        this.y = y;
        this.counter = 0;
    }
    
    public Element(){
        this.x = -1;
        this.y = -1;
        this.counter = 0;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getCounter() {
        return this.counter;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void updateCounter(int value) {
        this.counter += value;
    }

}
