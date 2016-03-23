package org.thomas.annea.flatland;

import org.thomas.annea.gui.flatland.CellGui;

public class Cell extends AbstractFlatlandObject implements Cloneable {

    // Various headings
    public static final int EMPTY = 0;
    public static final int FOOD = 1;
    public static final int POISON = 2;

    private int x;
    private int y;
    private int state;

    /**
     * Constructor
     * @param xCoor Coordinate for x
     * @param yCoor Coordinate for y
     */

    public Cell(int xCoor, int yCoor) {
        this(xCoor, yCoor, EMPTY);
    }

    /**
     * Constructor
     * @param xCoor Coordinate for x
     * @param yCoor Coordinate for y
     * @param s State
     */

    public Cell(int xCoor, int yCoor, int s) {
        super(new CellGui());

        // Set the gui source
        gui.setSource(this);

        // Set various information
        x = xCoor;
        y = yCoor;
        state = s;
    }

    /**
     * Setter for the state variable
     * @param s The state
     */

    public void setState(int s) {
        state = s;
    }

    /**
     * Various getters
     * @return Value(s)
     */

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getState() {
        return state;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
