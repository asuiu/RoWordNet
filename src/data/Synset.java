package data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Synset data structure definition class
 * 
 * @author Stefan Dumitrescu
 * @author Radu Petrut
 */
public class Synset implements Serializable{
	private static final long serialVersionUID = -7431224983353851289L;

	public static enum Type {
		Noun, Verb, Adverb, Adjective
	}

	String id;
	ArrayList<String> pwn20;
	Type pos;
	boolean non_lexicalized;
	ArrayList<Literal> literals;
	String definition;
	ArrayList<String> usage;
	String stamp;
	ArrayList<Relation> relations;
	String domain;
	String sumo, sumotype;
	String sentiwn_p, sentiwn_n, sentiwn_o;
	String nl; 

	double informationContent;

	/**
	 * 
	 * @return the synset's unique id in string format
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the synset's id to a given value
	 * 
	 * @param id
	 *            the given value to which the synset's id will be set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * @return the Part Of Speech(noun, verb, adverb or adjective) that
	 *         corresponds to the way the literals belonging to the synset are
	 *         used in common speech, in accordance with the sense associated
	 *         with the synset
	 */
	public Type getPos() {
		return pos;
	}

	/**
	 * Sets the Part Of Speech for the current synset to a given one.
	 * 
	 * @param pos
	 *            the given value for the Part Of Speech
	 * @see getPos()
	 */
	public void setPos(Type pos) {
		this.pos = pos;
	}

	/**
	 * Returns the boolean class variable non-lexicalized(which tells us if the
	 * synset has any literals in it).
	 * 
	 * @return true if the synset has no literals, false otherwise
	 */
	public boolean isNon_lexicalized() {
		return non_lexicalized;
	}

	/**
	 * Sets the class variable non-lexicalized to a given value.
	 * 
	 * @param non_lexicalized
	 *            the value to which the class variable non-lexicalized will be
	 *            set
	 * @see isNon_lexicalized()
	 */
	public void setNon_lexicalized(boolean non_lexicalized) {
		this.non_lexicalized = non_lexicalized;
	}

	/**
	 * 
	 * @return the dictionary definition of the sense associated with the
	 *         current synset, along with a sentence example
	 */
	public String getDefinition() {
		return definition;
	}

	/**
	 * Sets the synset's sense definition to a given value
	 * 
	 * @param definition
	 *            the given value for the synset's sense definition
	 */
	public void setDefinition(String definition) {
		this.definition = definition;
	}

	/**
	 * 
	 * @return the signature(in string format)of the person who added the
	 *         current synset to the dictionary
	 */
	public String getStamp() {
		return stamp;
	}

	/**
	 * Sets the synset's signature to a given value
	 * 
	 * @param relations
	 *            the value to which the synset's signature will be set
	 * @see getStamp()
	 */
	public void setStamp(String stamp) {
		this.stamp = stamp;
	}

	/**
	 * 
	 * @return an ArrayList containing the literals associated with the synset
	 */
	public ArrayList<Literal> getLiterals() {
		return literals;
	}

	/**
	 * Sets the list of literals belonging to the synset to a given value.
	 * 
	 * @param literals
	 *            the value to which the list of literals will be set
	 */
	public void setLiterals(ArrayList<Literal> literals) {
		this.literals = literals;
	}

	/**
	 * 
	 * @return an ArrayList containing the all the relations in which the
	 *         current synset can be involved with other synsets
	 */
	public ArrayList<Relation> getRelations() {
		return relations;
	}

	/**
	 * Sets the synset's list of relations to a given value
	 * 
	 * @param relations
	 *            the value that will be assigned to the the synset's list of
	 *            relations
	 */
	public void setRelations(ArrayList<Relation> relations) {
		this.relations = relations;
	}

	/**
	 * 
	 * @return the string representation of the domain to which the current
	 *         synset belongs to
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Sets the synset's domain to a given value.
	 * 
	 * @param domain
	 *            the value that will be assigned to the synset's domain
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getSumo() {
		return sumo;
	}

	public void setSumo(String sumo) {
		this.sumo = sumo;
	}

	public String getSumotype() {
		return sumotype;
	}

	public void setSumotype(String sumotype) {
		this.sumotype = sumotype;
	}

	public String getSentiwn_p() {
		return sentiwn_p;
	}

	public void setSentiwn_p(String sentiwn_p) {
		this.sentiwn_p = sentiwn_p;
	}

	public String getSentiwn_n() {
		return sentiwn_n;
	}

	public void setSentiwn_n(String sentiwn_n) {
		this.sentiwn_n = sentiwn_n;
	}

	public String getSentiwn_o() {
		return sentiwn_o;
	}

	public void setSentiwn_o(String sentiwn_o) {
		this.sentiwn_o = sentiwn_o;
	}

	public String getNl() {
		return nl;
	}

	public void setNl(String nl) {
		this.nl = nl;
	}

	public ArrayList<String> getPwn20() {
		return pwn20;
	}

	public void setPwn20(ArrayList<String> pwn20) {
		this.pwn20 = pwn20;
	}

	public ArrayList<String> getUsage() {
		return usage;
	}

	public void setUsage(ArrayList<String> usage) {
		this.usage = usage;
	}

	/**
	 * @return the informationContent
	 */
	public double getInformationContent() {
		return informationContent;
	}

	/**
	 * @param informationContent
	 *            the informationContent to set
	 */
	public void setInformationContent(double informationContent) {
		this.informationContent = informationContent;
	}

	static public Type stringToType(String pos) {
		switch (pos) {
		case "n":
			return Synset.Type.Noun;
		case "v":
			return Synset.Type.Verb;
		case "r":
			return Synset.Type.Adverb;
		case "a":
			return Synset.Type.Adjective;
		default:
			return null;
		}
	}

	static public String typeToString(Type type) {
		switch (type) {
		case Noun:
			return "n";
		case Verb:
			return "v";
		case Adverb:
			return "r";
		case Adjective:
			return "a";
		default:
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((definition == null) ? 0 : definition
				.hashCode());
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((literals == null) ? 0 : literals.hashCode());
		result = prime * result + ((nl == null) ? 0 : nl.hashCode());
		result = prime * result + (non_lexicalized ? 1231 : 1237);
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
		result = prime * result + ((relations == null) ? 0 : relations
				.hashCode());
		result = prime * result + ((sentiwn_n == null) ? 0 : sentiwn_n
				.hashCode());
		result = prime * result + ((sentiwn_o == null) ? 0 : sentiwn_o
				.hashCode());
		result = prime * result + ((sentiwn_p == null) ? 0 : sentiwn_p
				.hashCode());
		result = prime * result + ((sumo == null) ? 0 : sumo.hashCode());
		result = prime * result + ((sumotype == null) ? 0 : sumotype.hashCode());
		result = prime * result + ((usage == null) ? 0 : usage.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Synset other = (Synset) obj;
		if (definition == null) {
			if (other.definition != null) {
				return false;
			}
		} else if (!definition.equals(other.definition)) {
			return false;
		}
		if (domain == null) {
			if (other.domain != null) {
				return false;
			}
		} else if (!domain.equals(other.domain)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (literals == null) {
			if (other.literals != null) {
				return false;
			}
		} else if (!literals.equals(other.literals)) {
			return false;
		}
		if (nl == null) {
			if (other.nl != null) {
				return false;
			}
		} else if (!nl.equals(other.nl)) {
			return false;
		}
		if (non_lexicalized != other.non_lexicalized) {
			return false;
		}
		if (pos != other.pos) {
			return false;
		}
		if (relations == null) {
			if (other.relations != null) {
				return false;
			}
		} else if (!relations.equals(other.relations)) {
			return false;
		}
		if (sentiwn_n == null) {
			if (other.sentiwn_n != null) {
				return false;
			}
		} else if (!sentiwn_n.equals(other.sentiwn_n)) {
			return false;
		}
		if (sentiwn_o == null) {
			if (other.sentiwn_o != null) {
				return false;
			}
		} else if (!sentiwn_o.equals(other.sentiwn_o)) {
			return false;
		}
		if (sentiwn_p == null) {
			if (other.sentiwn_p != null) {
				return false;
			}
		} else if (!sentiwn_p.equals(other.sentiwn_p)) {
			return false;
		}
		if (sumo == null) {
			if (other.sumo != null) {
				return false;
			}
		} else if (!sumo.equals(other.sumo)) {
			return false;
		}
		if (sumotype == null) {
			if (other.sumotype != null) {
				return false;
			}
		} else if (!sumotype.equals(other.sumotype)) {
			return false;
		}
		if (usage == null) {
			if (other.usage != null) {
				return false;
			}
		} else if (!usage.equals(other.usage)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		String out = "Synset: id=" + id;
		if (pos != null)
			out += ", pos=" + pos;
		if (nl != null)
			out += ", nl=" + non_lexicalized;
		if (definition != null)
			out += ", definition=" + definition;
		if (stamp != null)
			out += ", stamp=" + stamp;
		if (domain != null)
			out += ", domain=" + domain;
		if (this.literals != null)
			for (Literal l : this.literals)
				out += "\n\t" + l.toString();
		if (this.relations != null)
			for (Relation r : this.relations)
				out += "\n\t" + r.toString();
		return out;
	}
}
