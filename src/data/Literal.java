package data;

import java.io.Serializable;

/**
 * Literal data structure definition
 *
 * @author Stefan Dumitrescu
 * @author Radu Petrut
 */
public class Literal implements Serializable {
    private static final long serialVersionUID = 1438261926612978944L;
    String literal;
    String sense;

    /**
     * Class constructor. Sets the class variables(literal and sense) to null.
     */
    public Literal() {
        this.literal = this.sense = null;
    }

    /**
     * Class constructor. Sets the class variables(literal and sense) to given
     * values.
     *
     * @param literal the value that will be assigned to the literal class variable
     * @param sense   the value that will be assigned to the sense class variable
     */
    public Literal(String literal, String sense) {
        this.literal = literal;
        this.sense = sense;
    }

    /**
     * Class constructor. Sets the class variable literal to a given value. Null
     * will be assigned to the sense class variable.
     *
     * @param literal the value that will be assigned to the literal class variable
     */
    public Literal(String literal) {
        this.literal = literal;
        this.sense = null;
    }

    /**
     * @return the string describing the literal
     */
    public String getLiteral() {
        return literal;
    }

    /**
     * Sets the string describing the literal to a given value.
     *
     * @param literal the value to be assigned to the literal
     */
    public void setLiteral(String literal) {
        this.literal = literal;
    }

    /**
     * @return the string describing the literal's sense
     */
    public String getSense() {
        return sense;
    }

    /**
     * Sets the string describing the literal's sense to a given value.
     *
     * @param literal the value to be assigned to the literal's sense
     */
    public void setSense(String sense) {
        this.sense = sense;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((literal == null) ? 0 : literal.hashCode());
        result = prime * result + ((sense == null) ? 0 : sense.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Literal other = (Literal) obj;
        if (literal == null) {
            if (other.literal != null)
                return false;
        } else if (!literal.equals(other.literal))
            return false;

        if (sense == null)
            return true;
        if (other.sense == null)
            return true;
        if (!sense.equals(other.sense))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Literal [literal=" + literal + ", sense=" + sense + "]";
    }
}
