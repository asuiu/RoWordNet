package op;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import data.Literal;
import data.RoWordNet;
import data.Synset;

/**
 * This class consists of static methods which represent implementations of
 * various operations that can be applied to a dictionary, represented as a
 * RoWordNet object.
 * <p>
 * The dictionary is seen as a tree, with the nodes being the synsets and the
 * edges being the relations between them.
 * </p>
 * 
 * @author Stefan Dumitrescu
 * @author Radu Petrut
 */
public class Operation {

	/**
	 * Method that returns the list of synsets that:
	 * <ul>
	 * <li>exist in both RoWordNet objects a and b (having the same ID)</li>
	 * <li>have different content(the equals method returns false)</li>
	 * </ul>
	 * 
	 * @param a
	 *            RoWordNet object a
	 * @param b
	 *            RoWordNet object b
	 * @return Array of synset IDs contained in a and b that have different
	 *         content
	 */
	public static String[] diff(RoWordNet a, RoWordNet b) {
		ArrayList<String> dif = new ArrayList<>();

		Iterator<Entry<String, Synset>> it = a.synsetsMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Synset> entry = (Map.Entry<String, Synset>) it
					.next();

			if (b.synsetsMap.containsKey(entry.getKey()))
				if (!b.getSynsetById(entry.getKey()).equals(entry.getValue()))
					dif.add(entry.getKey());
		}
		return (String[]) dif.toArray(new String[0]);
	}

	/**
	 * Method that returns the complement of b in a, where a and b are RoWordNet
	 * objects passed as arguments.
	 * <p>
	 * By 'the complement of b in a' we understand the IDs of the synsets that
	 * are in a but not in b.
	 * </p>
	 * <p>
	 * Note that the comparison is made only after the ID. If the content of two
	 * synsets(one from a and one from b) is the same but their IDs differ, then
	 * the ID from a will be added to the complement.
	 * </p>
	 * 
	 * @param a
	 *            RoWordNet object a
	 * @param b
	 *            RoWordNet object b
	 * @return the complement of b in a, as described in the previous paragraphs
	 */
	public static String[] complement(RoWordNet a, RoWordNet b) {
		ArrayList<String> comp = new ArrayList<>();

		Iterator<Entry<String, Synset>> it = a.synsetsMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Synset> entry = (Map.Entry<String, Synset>) it
					.next();
			if (!b.synsetsMap.containsKey(entry.getKey()))
				comp.add(entry.getKey());
		}

		return (String[]) comp.toArray(new String[0]);
	}

	/**
	 * Method that returns the intersection of a and b, where a and b are
	 * RoWordNet objects passed as arguments.
	 * <p>
	 * By 'the intersection of a and b' we understand the IDs of the synsets
	 * that are in both a and b and have the same content(the equals function
	 * returns true).
	 * </p>
	 * 
	 * @param a
	 *            RoWordNet object a
	 * @param b
	 *            RoWordNet object b
	 * @return the intersection of b in a, as described in the previous
	 *         paragraphs
	 */
	public static String[] intersection(RoWordNet a, RoWordNet b) {
		ArrayList<String> inter = new ArrayList<>();

		Iterator<Entry<String, Synset>> it = a.synsetsMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Synset> entry = (Map.Entry<String, Synset>) it
					.next();

			if (b.synsetsMap.containsKey(entry.getKey()))
				if (b.getSynsetById(entry.getKey()).equals(entry.getValue()))
					inter.add(entry.getKey());
		}
		return (String[]) inter.toArray(new String[0]);
	}

	/**
	 * Method that adds synsets from the addition RoWordNet object into the base
	 * RoWordNet object if the following condition is satisfied:
	 * <ul>
	 * <li>
	 * the base RoWordNet object does not contain a synset with it's ID the same
	 * as the target one from the addition</li>
	 * </ul>
	 * 
	 * 
	 * @param base
	 *            Target RoWordNet object into which all synsets from the
	 *            addition object which respect the condition above are added
	 * @param addition
	 *            RoWordNet object that contains synsets that will be added into
	 *            the base RoWordNet object if they satisfy the condition
	 * @return The resulting union between the two RoWordNet objects(the base
	 *         RoWordNet object)
	 * @throws Exception
	 *             if two synsets(one from the base and the other from the
	 *             addition) are found with the same IDs, but different content
	 */
	public static RoWordNet union(RoWordNet base, RoWordNet addition) throws Exception {
		Iterator<Entry<String, Synset>> it = addition.synsetsMap.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<String, Synset> entry = (Map.Entry<String, Synset>) it
					.next();

			if (!base.synsetsMap.containsKey(entry.getKey()))
				base.addSynset(entry.getValue(), true);
			else if (!base.getSynsetById(entry.getKey())
					.equals(entry.getValue()))
				throw new Exception("The RoWordNet objects contain synsets" + "with the same id, but different content.");
		}
		return base;
	}

	/**
	 * Method merges the addition RoWordNet object into the base RoWordNet
	 * object, overwriting synsets if already existing in the base object
	 * 
	 * @param base
	 *            Target RoWordNet object into which all synsets from the
	 *            addition object are copied into
	 * @param addition
	 *            RoWordNet object that contains synsets that will be copied
	 *            into the base RoWordNet object
	 * @return The resulting merge between the two RoWordNet objects(the base
	 *         RoWordNet object)
	 */
	public static RoWordNet merge(RoWordNet base, RoWordNet addition) {
		Iterator<Entry<String, Synset>> it = addition.synsetsMap.entrySet()
				.iterator();
		while (it.hasNext())
			base.addSynset(((Map.Entry<String, Synset>) it.next()).getValue(), true);

		return base;
	}

	/**
	 * Method that returns the path between two synsets using the search
	 * mechanism made available by the BFWalk class.
	 * <p>
	 * The path consists in the IDs of all the synsets visited in order to get
	 * from the source synset to the target one. It is represented as an array
	 * of String objects(each element is a synset ID).
	 * </p>
	 * <p>
	 * Note that the found path isn't the shortest one, as the search
	 * implemented in the BFWalk class is just a modified BFS with no optimality
	 * criterion included.
	 * </p>
	 * 
	 * @param RoWN
	 *            the dictionary to which the synsets belong
	 * @param sourceSynsetID
	 *            the ID of the synset from where the search begins
	 * @param targetSynsetID
	 *            the searched synset's ID
	 * @param allowAllRelations
	 *            flag that indicates if all the possible relations between
	 *            synsets are to be considered when parsing the tree
	 * @param filteredRelations
	 *            array of relations with the following meaning:
	 *            <ul>
	 *            <li>
	 *            if allowAllRelations param is true, the relations contained in
	 *            the array will be ignored when parsing the tree;</li>
	 *            <li>
	 *            if allowAllRelations param is false, the relations contained
	 *            in the array will be the only ones considered when parsing the
	 *            tree;</li>
	 *            </ul>
	 * @return the path from target to source, as an array of String objects
	 *         (synset IDs)
	 * @throws Exception
	 *             if no relation is left to analyze after applying the
	 *             restrictions
	 * @see BFWalk
	 */
	public static String[] getPath(RoWordNet RoWN, String sourceSynsetID, String targetSynsetID, boolean allowAllRelations, String[] filteredRelations) throws Exception {

		String s;
		String[] retS;
		ArrayList<String> retL = new ArrayList<String>();

		BFWalk bfw = new BFWalk(RoWN, sourceSynsetID, allowAllRelations, filteredRelations);
		while (bfw.hasMoreSynsets()) {
			s = bfw.nextSynset();
			retL.add(s);
			if (s.equals(targetSynsetID))
				break;
		}
		retS = new String[retL.size()];
		retL.toArray(retS);

		return retS;
	}

	/**
	 * Method that returns the path between two literals using the search
	 * mechanism made available by the BFWalk class.
	 * <p>
	 * The path consists in the IDs of all the synsets visited in order to get
	 * from the synset that contains the source literal to the synset that
	 * contains the target one. It is represented as an array of String objects
	 * (each element is a synset ID).
	 * </p>
	 * <p>
	 * Note that the found path isn't the shortest one, as the search
	 * implemented in the BFWalk class is just a modified BFS with no optimality
	 * criterion included.
	 * </p>
	 * 
	 * @param RoWN
	 *            the dictionary to which the synsets belong
	 * @param sourceLiteral
	 *            the literal from where the search begins
	 * @param targetLiteral
	 *            the searched literal
	 * @param allowAllRelations
	 *            flag that indicates if all the possible relations between
	 *            synsets are to be considered when parsing the tree
	 * @param filteredRelations
	 *            array of relations with the following meaning:
	 *            <ul>
	 *            <li>
	 *            if allowAllRelations parameter is true, the relations
	 *            contained in the array will be ignored when parsing the tree;</li>
	 *            <li>
	 *            if allowAllRelations parameter is false, the relations
	 *            contained in the array will be the only ones considered when
	 *            parsing the tree;</li>
	 *            </ul>
	 * 
	 * @return the path from target to source, as an array of String objects
	 *         (synset IDs)
	 * @throws Exception
	 *             if no relation is left to analyze after applying the
	 *             restrictions
	 * @see BFWalk
	 */
	public static String[] getPath(RoWordNet RoWN, Literal sourceLiteral, Literal targetLiteral, boolean allowAllRelations, String[] filteredRelations) throws Exception {

		String sourceSynsetID = RoWN.getSynsetFromLiteral(sourceLiteral)
				.getId();
		String targetSynsetID = RoWN.getSynsetFromLiteral(targetLiteral)
				.getId();

		return getPath(RoWN, sourceSynsetID, targetSynsetID, allowAllRelations, filteredRelations);
	}
}
