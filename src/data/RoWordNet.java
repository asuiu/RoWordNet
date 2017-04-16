package data;

import io.IO;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import utils.Timer;

/**
 * Class that defines a RoWordNet object by implementing the methods that can be
 * applied to it.
 *
 * @author Stefan Dumitrescu
 * @author Radu Petrut
 */
public class RoWordNet implements Cloneable, Serializable {
	private static final long serialVersionUID = -7790398228874534235L;

	/**
	 * An ArrayList containing all the synsets that compose the dictionary.
	 */
	public ArrayList<Synset>					synsets;

	/**
	 * A HashMap containing (key, value) entries, where key is a synset's ID and
	 * value is the synset itself.
	 */
	public HashMap<String, Synset>				synsetsMap;

	/**
	 * A HashMap containing (key, value) entries, where key is a word belonging
	 * to the dictionary and value is an ArrayList with all of it's senses( ids
	 * of the synsets containing the word).
	 */
	public HashMap<String, ArrayList<String>>	words;

	/**
	 * The last assigned id for a synset.
	 */
	String										incrementalID;

	/**
	 * Default constructor, creates an empty object
	 */
	public RoWordNet() {
		this.synsets = new ArrayList<Synset>();
		this.synsetsMap = new HashMap<String, Synset>();
		this.words = new HashMap<String, ArrayList<String>>();
	}

	/**
	 * The class constructor.
	 * <p>
	 * Based on a list of synsets passed as argument it builds two HashMaps:
	 * <ul>
	 * <li>
	 * the first one's entries follow the structure (key, value), with the key
	 * being a synset's id and the value being the synset itself.</li>
	 * <li>
	 * the second one's entries follow the structure (key, value), with the key
	 * being a word's string representation and the value being the synsets
	 * associated with that word.</li>
	 * </ul>
	 * </p>
	 *
	 * @param synsets
	 *            the ArrayList of synsets
	 */
	public RoWordNet(ArrayList<Synset> synsets) {
		this.synsets = synsets;
		this.synsetsMap = new HashMap<String, Synset>();
		this.words = new HashMap<String, ArrayList<String>>();

		Timer timer = new Timer();
		IO.outln("Creating data structures...");
		for (Synset s : synsets) {
			this.synsetsMap.put(s.getId(), s);
			if (s.literals != null && !s.literals.isEmpty())
				for (Literal l : s.literals) {
					if (!this.words.containsKey(l.literal)) {
						ArrayList<String> al = new ArrayList<String>();
						al.add(s.getId());
						this.words.put(l.literal, al);
					} else {
						ArrayList<String> old_al = this.words.get(l.literal);
						old_al.add(s.getId());
						this.words.put(l.literal, old_al);
					}
				}
		}
		IO.out(" done. " + timer.mark());
	}

	/**
	 * Copy-constructor
	 *
	 * @param obj
	 */
	public RoWordNet(RoWordNet obj) {
		this.synsets = (ArrayList<Synset>) obj.synsets.clone();
		this.synsetsMap = (HashMap<String, Synset>) obj.synsetsMap.clone();
		this.words = (HashMap<String, ArrayList<String>>) obj.words.clone();
		this.incrementalID = obj.incrementalID;
	}

	/**
	 * Method that builds and returns a Hashmap of synsets with the same POS.
	 * <p>
	 * The HashMap entries follow the structure (key, value), with the key being
	 * a synset's id and the value being the synset itself.
	 * </p>
	 *
	 * @param pos
	 *            the Part Of Speech, representing the sorting criterion
	 * @return the synsets with the same pos as the one passed as argument
	 */
	public HashMap<String, Synset> getHashedSynsetsByPos(Synset.Type pos) {
		HashMap<String, Synset> posSynsets = new HashMap<String, Synset>();

		for (Synset s : synsets)
			if (s.getPos().equals(pos))
				if (!posSynsets.containsKey(s.getId()))
					posSynsets.put(s.getId(), s);

		return posSynsets;
	}

	/**
	 * Method that builds and returns an ArrayList of synsets with the same POS.
	 *
	 * @param pos
	 *            the Part Of Speech, representing the sorting criterion
	 * @return the synsets with the same pos as the one passed as argument
	 */
	public ArrayList<Synset> getListedSynsetsByPos(Synset.Type pos) {
		ArrayList<Synset> posSynsets = new ArrayList<Synset>();

		for (Synset s : synsets)
			if (s.getPos().equals(pos))
				if (!posSynsets.contains(s))
					posSynsets.add(s);

		return posSynsets;
	}

	/**
	 * Returns the first available id that starts with a given prefix and ends
	 * with a given suffix.
	 * <p>
	 * By available it means the id is not assigned to an existing synset.
	 * </p>
	 *
	 * @param prefix
	 *            the given prefix
	 * @param suffix
	 *            the given suffix
	 * @return the first available id that starts with the given prefix and ends
	 *         with the given suffix
	 */
	public String getNewId(String prefix, String suffix) {
		boolean matched = false;
		int max = 0, digitCount = 0;
		String temp;

		for (Synset s : synsets) {
			temp = s.getId();
			if (!temp.startsWith(prefix))
				continue;
			if (!suffix.equals(""))
				if (!temp.endsWith(suffix))
					continue;

			matched = true;

			temp = temp.replace(prefix, "");
			if (suffix != "")
				temp = temp.replace(suffix, "");
			if (Integer.parseInt(temp) > max) {
				max = Integer.parseInt(temp);
				digitCount = temp.length();
			}
		}
		if (!matched)
			return prefix + String.format("%0" + 8 + "d", 1) + suffix;
		return prefix + String.format("%0" + digitCount + "d", (max + 1)) + suffix;
	}

	/**
	 * Returns and stores in a class variable the first available id that starts
	 * with a given prefix and ends with a given suffix. If the previous stored
	 * id does not match the rule above, the method will iterate through the
	 * existing synsets and find the last assigned id that follows the rule. If
	 * the prefix-suffix rule is satisfied, the method will simply increment the
	 * stored id's digit section in order to obtain the first available id.
	 *
	 * @param prefix
	 *            the given prefix
	 * @param suffix
	 *            the given suffix
	 * @return the first available id that starts with a given prefix and ends
	 *         with a given suffix
	 * @see getNewId(String prefix, String suffix)
	 */
	public String getNewIncrementalId(String prefix, String suffix) {
		if (incrementalID == null || !incrementalID.startsWith(prefix) || (suffix != "" && !incrementalID
				.endsWith(suffix))) {
			incrementalID = this.getNewId(prefix, suffix);
		} else {
			String val = incrementalID.replace(prefix, "").replace(suffix, "");
			int digitCount = val.length();
			incrementalID = prefix + String
					.format("%0" + digitCount + "d", (Integer.parseInt(val) + 1)) + suffix;
			return incrementalID;
		}
		return incrementalID;
	}

	/**
	 * Adds a given synset to the synset pool, overwriting an existing synset if
	 * the overwrite flag is true.
	 *
	 * @param synset
	 *            Synset to be added
	 * @param overwrite
	 *            boolean specifying if the new synset should overwrite old
	 *            synset, if existing
	 * @return true if the synset was added, false if if it was not (only
	 *         happens when the overwrite flag is false and there is a
	 *         previously existing synset that will not be overwritten)
	 */
	public boolean addSynset(Synset synset, boolean overwrite) {
		if (this.synsetsMap.containsKey(synset.getId())) {
			if (overwrite == false)
				return false;
			this.synsets.remove(synsetsMap.get(synset.getId()));
		}
		this.synsets.add(synset);
		this.synsetsMap.put(synset.getId(), synset);
		return true;
	}

	/**
	 * Method adds an array of synsets, overwriting existing synsets if the
	 * overwrite flag is true.
	 *
	 * @param synsets
	 *            Synset array to be added
	 * @param overwrite
	 *            boolean specifying if the new synsets should overwrite old
	 *            synsets, if existing
	 */
	public void addSynsets(Synset[] synsets, boolean overwrite) {
		for (Synset synset : synsets)
			this.addSynset(synset, overwrite);
	}

	/**
	 * Method that prints on stdout various statisticals informations regarding
	 * the synsets that compose the dictionary. The informations are obtained by
	 * taking into consideration different criteria, such as the POS or the
	 * relation frequency.
	 *
	 * @return the statistical informations in String format
	 */
	public String getStats() {
		HashMap<String, Synset> nounSynsets = getHashedSynsetsByPos(Synset.Type.Noun);
		HashMap<String, Synset> verbSynsets = getHashedSynsetsByPos(Synset.Type.Verb);
		HashMap<String, Synset> adverbSynsets = getHashedSynsetsByPos(Synset.Type.Adverb);
		HashMap<String, Synset> adjectivesSynsets = getHashedSynsetsByPos(Synset.Type.Adjective);
		int lit, nl, totalLit = 0;
		HashSet<Literal> hsLit;


		String out = "Statistics:";
		out += "\n\t TOTAL Synsets:\t\t" + this.synsets.size();
		out += "\n\t Noun Synsets:\t\t" + nounSynsets.size();
		hsLit = new HashSet<Literal>();lit = nl = 0;
		for(String id : nounSynsets.keySet()) {
			Synset s = nounSynsets.get(id);
			if (s.literals != null)
				for (Literal l : s.literals) {
					hsLit.add(l);
					lit++;totalLit++;
				}
			if(s.isNon_lexicalized()) nl++;
		}
		out += "\n\t\t Noun Literals:\t\t" + lit;
		out += "\n\t\t Noun Unique Literals:\t\t" + hsLit.size();
		out += "\n\t\t Noun Non-lexicalized Synsets:\t\t" + nl;


		out += "\n\t Verb Synsets:\t\t" + verbSynsets.size();
		hsLit = new HashSet<Literal>();lit = nl = 0;
		for(String id : verbSynsets.keySet()) {
			Synset s = verbSynsets.get(id);
			if (s.literals != null)
				for (Literal l : s.literals) {
					hsLit.add(l);
					lit++;totalLit++;
				}
			if(s.non_lexicalized) nl++;
		}
		out += "\n\t\t Verb Literals:\t\t" + lit;
		out += "\n\t\t Verb Unique Literals:\t\t" + hsLit.size();
		out += "\n\t\t Verb Non-lexicalized Synsets:\t\t" + nl;


		out += "\n\t Adverb Synsets:\t" + adverbSynsets.size();
		hsLit = new HashSet<Literal>();lit = nl = 0;
		for(String id : adverbSynsets.keySet()) {
			Synset s = adverbSynsets.get(id);
			if (s.literals != null)
				for (Literal l : s.literals) {
					hsLit.add(l);
					lit++;totalLit++;
				}
			if(s.non_lexicalized) nl++;
		}
		out += "\n\t\t Adverb Literals:\t\t" + lit;
		out += "\n\t\t Adverb Unique Literals:\t\t" + hsLit.size();
		out += "\n\t\t Adverb Non-lexicalized Synsets:\t\t" + nl;


		out += "\n\t Adjective Synsets:\t" + adjectivesSynsets.size();
		hsLit = new HashSet<Literal>();lit = nl = 0;
		for(String id : adjectivesSynsets.keySet()) {
			Synset s = adjectivesSynsets.get(id);
			if (s.literals != null)
				for (Literal l : s.literals) {
					hsLit.add(l);
					lit++;totalLit++;
				}
			if(s.non_lexicalized) nl++;
		}
		out += "\n\t\t Adjective Literals:\t\t" + lit;
		out += "\n\t\t Adjective Unique Literals:\t\t" + hsLit.size();
		out += "\n\t\t Adjective Non-lexicalized Synsets:\t\t" + nl;





		HashSet<Literal> hl = new HashSet<Literal>();
		HashMap<String, Integer> relationFrequency = new HashMap<String, Integer>();
		int relationCount=0;

		for (Synset s : synsets) {
			if (s.literals != null)
				for (Literal l : s.literals)
					hl.add(l);
			if (s.relations == null)
				continue;
			for (Relation r : s.relations) {
				relationCount++;
				if (!relationFrequency.containsKey(r.relation))
					relationFrequency.put(r.relation, new Integer(1));
				else
					relationFrequency
							.put(r.relation, new Integer(relationFrequency
									.get(r.relation)) + 1);
			}
		}

		out += "\n\t Total Literals:\t" + totalLit;
		out += "\n\t Total Unique Literals:\t" + hl.size();
		out += "\n\n\t Number of relations:\t"+relationCount;
		out += "\n\t Relation Frequency table:";
		for (String rel : relationFrequency.keySet()) {
			out += "\n\t\t" + rel + ": " + relationFrequency.get(rel);
		}

		return out;
	}

	/**
	 * Returns the synset whose id corresponds to the one passed as argument, or
	 * null if no such synset is found.
	 *
	 * @param id
	 *            the id of the targeted synset
	 * @return the synset with the specified id
	 */
	public Synset getSynsetById(String id) {
		return synsetsMap.get(id);
	}

	/**
	 * Returns an array of Synset objects.
	 *
	 * @param ids
	 *            an ArrayList of String objects that represent the ids of the
	 *            targeted synsets
	 * @return the synsets pointed by the 'ids' argument
	 * @see getRelatedSynsetIds(String synsetId, String relation)
	 */
	public Synset[] getSynsetsFromIds(ArrayList<String> ids) {
		ArrayList<Synset> ret = new ArrayList<Synset>();
		for (String id : ids)
			ret.add(synsetsMap.get(id));
		return (Synset[]) ret.toArray(new Synset[0]);
	}

	/**
	 * Returns an array of Synset objects.
	 *
	 * @param ids
	 *            an array of String objects that represent the ids of the
	 *            targeted synsets
	 * @return the synsets pointed by the 'ids' argument
	 * @see getRelatedSynsetIds(String synsetId, String relation)
	 */
	public Synset[] getSynsetsFromIds(String[] ids) {
		ArrayList<Synset> ret = new ArrayList<Synset>();
		for (String id : ids)
			ret.add(synsetsMap.get(id));
		return (Synset[]) ret.toArray(new Synset[0]);
	}

	/**
	 * Returns an ArrayList of String objects that represent the ids of the
	 * synsets that relate with a reference synset in a manner specified by the
	 * 'relation' argument.
	 *
	 * @param synsetId
	 *            the id of the reference synset
	 * @param relation
	 *            specifies the relation that needs to occur between the
	 *            reference synset and another synset in order to add the
	 *            latter's id to the ArrayList of results
	 * @return the ids of the synsets that satisfy the given relation with the
	 *         reference synset
	 */
	public ArrayList<String> getRelatedSynsetIds(String synsetId, String relation) {
		ArrayList<String> ret = new ArrayList<String>();
		Synset s = synsetsMap.get(synsetId);
		for (Relation rel : s.getRelations()) {
			if ("*".equals(relation) || rel.relation.equals(relation))
				ret.add(rel.targetSynset);
		}
		return ret;
	}

	/**
	 * Returns the ids of the synsets that contain a given literal.
	 *
	 * @param literal
	 *            the searched literal
	 * @return the ids of the synsets that contain the searched literal
	 */
	public ArrayList<String> getIdsFromLiteral(Literal literal) {
		ArrayList<String> ret = new ArrayList<String>();
		for (Synset s : synsetsMap.values()) {
			if (s.literals != null)
				if (s.literals.contains(literal))
					ret.add(s.getId());
		}
		return ret;
	}

	/**
	 * Returns an ArrayList of synsets that contain a given literal.
	 *
	 * @param literal
	 *            the searched literal
	 * @return the synsets that contain the given literal
	 */
	public ArrayList<Synset> getSynsetsFromLiteral(Literal literal) {
		ArrayList<Synset> ret = new ArrayList<Synset>();
		for (Synset s : synsetsMap.values())
			if (s.literals != null && s.literals.contains(literal))
				ret.add(s);

		return ret;
	}

	/**
	 * Returns an ArrayList of synsets that contain a given literal and have a
	 * certain pos.
	 *
	 * @param literal
	 *            the searched literal
	 * @param pos
	 *            the required pos
	 * @return the synsets that contain the given literal and have the required
	 *         pos
	 */
	public ArrayList<Synset> getSynsetsFromLiteral(Literal literal, String pos) {
		ArrayList<Synset> ret = new ArrayList<Synset>();
		for (Synset s : synsetsMap.values())
			if (s.literals != null && s.literals.contains(literal) && s
					.getPos().equals(pos))
				ret.add(s);

		return ret;
	}

	/**
	 * Returns an ArrayList containing synsets that relate with a reference
	 * synset in a manner specified by the 'relation' argument.
	 *
	 * @param synsetId
	 *            the id of the reference synset
	 * @param relation
	 *            specifies the relation that needs to occur between the
	 *            reference synset and another synset in order to add the latter
	 *            to the ArrayList of results
	 * @return the synsets that satisfy the given relation with the reference
	 *         synset
	 */
	public ArrayList<Synset> getRelatedSynsets(String synsetId, String relation) {
		ArrayList<Synset> ret = new ArrayList<Synset>();
		Synset s = synsetsMap.get(synsetId);
		for (Relation rel : s.getRelations())
			if ("*".equals(relation) || rel.relation.equals(relation))
				ret.add(synsetsMap.get(rel.targetSynset));

		return ret;
	}

	/**
	 * Method that checks if a given literal is contained by at least a synset
	 * in the dictionary.
	 *
	 * @param literal
	 *            the searched literal
	 * @return true if the literal is found in any synset, false otherwise
	 */
	public boolean containsLiteral(Literal literal) {
		for (Synset s : synsetsMap.values())
			if (s.literals != null && s.literals.contains(literal))
				return true;
		return false;
	}

	/**
	 * Method used for finding the synset to which a literal(word+sense) belongs
	 * to. If the sense is null the first synset that contains the literal is
	 * returned.
	 *
	 * @param literal
	 * @return
	 */
	public Synset getSynsetFromLiteral(Literal literal) {
		for (Synset s : synsetsMap.values())
			if (s.literals != null && s.literals.contains(literal))
				for (Literal l : s.literals)
					if (l.sense.equals(literal.sense))
						return s;
		return null;
	}

	/**
	 * Writing (serializing) a RoWordNet object on disk.
	 *
	 * @param fileName
	 * @param lm
	 * @return
	 * @throws Exception
	 */
	public static boolean serializeToFile(String fileName, RoWordNet lm) throws Exception {
		IO.outln("Writing RoWN object to file (\"" + fileName + "\") ...");

		FileOutputStream fos = new FileOutputStream(fileName);
		try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(lm);
		}

		IO.outln("Done.");
		return true;
	}

	/**
	 * Reading (de-serializing) a RoWordNet object from disk.
	 *
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static RoWordNet deserializeFromFile(String fileName) throws Exception {
		IO.outln("Reading RoWN object from data file (\"" + fileName + "\") ...");

		FileInputStream fis = new FileInputStream(fileName);
		RoWordNet lm;
		try (ObjectInputStream ois = new ObjectInputStream(fis)) {
			lm = (RoWordNet) ois.readObject();
		}

		IO.outln("Done.");
		return lm;
	}
}
