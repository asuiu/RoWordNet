package data;

import java.io.Serializable;

/**
 * Relation data structure definition
 * 
 * @author Stefan Dumitrescu
 * @author Radu Petrut
 */
public class Relation implements Serializable{
	private static final long serialVersionUID = 3815805516315457306L;

	String sourceSynset, targetSynset;
	String relation;
	String sourceLiteral, targetLiteral;

	/**
	 * Class constructor. Sets all the class variables to null.
	 */
	public Relation() {
		this.sourceSynset = this.targetSynset = this.relation = null;
		this.sourceLiteral = this.targetLiteral = null;
	}

	/**
	 * Class constructor. Sets the source synset's id, the target synset's id
	 * and the relation between the source and target synsets to given values.
	 * 
	 * @param sourceSynsetId
	 *            the given value for the source synset's id
	 * @param targetSynsetId
	 *            the given value for the target synset's id
	 * @param relation
	 *            the given value for the string describing the relation between
	 *            synsets
	 */
	public Relation(String sourceSynsetId, String targetSynsetId, String relation) {
		this.sourceSynset = sourceSynsetId;
		this.targetSynset = targetSynsetId;
		this.relation = relation;
		this.sourceLiteral = this.targetLiteral = null;
	}

	/**
	 * Class constructor. Sets all the class variables to given values.
	 * 
	 * @param sourceSynsetId
	 *            the given value for the source synset's id
	 * @param targetSynsetId
	 *            the given value for the target synset's id
	 * @param relation
	 *            the given value for the string describing the relation between
	 *            synsets
	 * @param sourceLiteral
	 *            the given value for the source literal's id
	 * @param targetLiteral
	 *            the given value for the source literal's id
	 */
	public Relation(String sourceSynsetId, String targetSynsetId, String relation, String sourceLiteral, String targetLiteral) {
		this.sourceSynset = sourceSynsetId;
		this.targetSynset = targetSynsetId;
		this.relation = relation;
		this.sourceLiteral = sourceLiteral;
		this.targetLiteral = targetLiteral;
	}

	/**
	 * 
	 * @return the id of the relation's source synset
	 */
	public String getSourceSynset() {
		return sourceSynset;
	}

	/**
	 * Sets the relation's source synset to a given value.
	 * 
	 * @param source
	 *            the given value for the relation's source synset
	 */
	public void setSourceSynset(String source) {
		this.sourceSynset = source;
	}

	/**
	 * 
	 * @return the id of the relation's target synset
	 */
	public String getTargetSynset() {
		return targetSynset;
	}

	/**
	 * Sets the relation's target synset to a given value.
	 * 
	 * @param source
	 *            the given value for the relation's target synset
	 */
	public void setTargetSynset(String target) {
		this.targetSynset = target;
	}

	/**
	 * 
	 * @return the string describing the relation
	 */
	public String getRelation() {
		return relation;
	}

	/**
	 * Sets the relation to a given value.
	 * 
	 * @param source
	 *            the given value for the relation
	 */
	public void setRelation(String type) {
		this.relation = type;
	}

	/**
	 * 
	 * @return the relation's source literal, if one exists; if not, null will
	 *         be returned
	 */
	public String getSourceLiteral() {
		return sourceLiteral;
	}

	/**
	 * Sets the relation's source literal to a given value.
	 * 
	 * @param source
	 *            the given value for the relation's source literal
	 */
	public void setSourceLiteral(String sourceLiteral) {
		this.sourceLiteral = sourceLiteral;
	}

	/**
	 * 
	 * @return the relation's target literal, if one exists; if not, null will
	 *         be returned
	 */
	public String getTargetLiteral() {
		return targetLiteral;
	}

	/**
	 * Sets the relation's target literal to a given value.
	 * 
	 * @param source
	 *            the given value for the relation's target literal
	 */
	public void setTargetLiteral(String targetLiteral) {
		this.targetLiteral = targetLiteral;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((relation == null) ? 0 : relation.hashCode());
		result = prime * result + ((sourceLiteral == null) ? 0 : sourceLiteral
				.hashCode());
		result = prime * result + ((sourceSynset == null) ? 0 : sourceSynset
				.hashCode());
		result = prime * result + ((targetLiteral == null) ? 0 : targetLiteral
				.hashCode());
		result = prime * result + ((targetSynset == null) ? 0 : targetSynset
				.hashCode());
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
		Relation other = (Relation) obj;
		if (relation == null) {
			if (other.relation != null)
				return false;
		} else if (!relation.equals(other.relation))
			return false;
		if (sourceLiteral == null) {
			if (other.sourceLiteral != null)
				return false;
		} else if (!sourceLiteral.equals(other.sourceLiteral))
			return false;
		if (sourceSynset == null) {
			if (other.sourceSynset != null)
				return false;
		} else if (!sourceSynset.equals(other.sourceSynset))
			return false;
		if (targetLiteral == null) {
			if (other.targetLiteral != null)
				return false;
		} else if (!targetLiteral.equals(other.targetLiteral))
			return false;
		if (targetSynset == null) {
			if (other.targetSynset != null)
				return false;
		} else if (!targetSynset.equals(other.targetSynset))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Relation [" + sourceSynset + " " + relation + " " + targetSynset + "]";
	}
}
