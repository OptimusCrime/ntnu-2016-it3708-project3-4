package org.annea.tools.settings;

public class BeerSettings extends AbstractSettings {

    private int mode = 1;

    public void setMode(int value) {
        mode = value;
    }

    public int getMode() {
        return mode;
    }
}
