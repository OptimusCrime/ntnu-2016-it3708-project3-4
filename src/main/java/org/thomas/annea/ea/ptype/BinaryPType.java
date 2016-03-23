package org.thomas.annea.ea.ptype;

import org.thomas.annea.ea.gtype.AbstractGType;
import org.thomas.annea.tools.settings.AbstractSettings;

public class BinaryPType extends AbstractPType {

    /**
     * Constructor
     * @param s Instance of settings
     * @param p Instance of the G-type
     */

    public BinaryPType(AbstractSettings s, AbstractGType p) {
        super(s, p);
    }

    /**
     * To String
     * @return Stringification of the instance
     */

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        double[] representation = gtype.getAsArray();

        for (int i = 0; i < representation.length; i++) {
            if ((int) representation[i] == 1) {
                out.append("1");
            }
            else {
                out.append("0");
            }

            if ((i + 1) < representation.length) {
                out.append(", ");
            }
        }

        return "[" + out.toString()  + "]";
    }
}
