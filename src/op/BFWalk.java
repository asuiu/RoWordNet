package op;

import java.util.HashSet;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;

import data.RoWordNet;
import data.Literal;
import data.Relation;

/**
 * This class is an essential tool for parsing a dictionary, represented as a
 * RoWordNet object.
 * <p>
 * The dictionary is seen as a tree, with the nodes being the synsets and the
 * edges being the relations between them.
 * </p>
 * <p>
 * The parsing mechanism is fairly simple: we start from a root node and then
 * BFS through the tree, applying at each step the necessary restrictions(if
 * any) to the analyzed node's relation set.
 * </p>
 * <p>
 * The example below shows a common way this class can be used, by searching for
 * a target synset starting from a root:
 * 
 * <pre>
 * BFWalk bfw = new BFWalk(RoWN, sourceSynsetID, allowAllRelations, filteredRelations);
 * while (bfw.hasMoreSynsets()) {
 * 	s = bfw.nextSynset();
 * 	retL.add(s);
 * 	if (s.equals(targetSynsetID))
 * 		break;
 * }
 * </pre>
 * 
 * <p>
 * The boolean allowAllRelations and String[] filteredRelations function as:
 * Case 1. allowAllRelations = true, then any strings contained in
 * filteredRelations will be relations that are not considered as valid when
 * performing BFWalk. Default filteredRelations = null, meaning all relations
 * are allowed.
 * 
 * Case 2. allowAllRelations = false, then the only allowed relations that
 * BFWalk is considered are those specified as strings in filteredRelations. If
 * filteredRelations = null, then no relation is allowed, and thus the BFWalk
 * will start and stop immediately at the source synset.
 * </p>
 * 
 * @author Stefan Dumitrescu
 * @author Radu Petrut
 */
public class BFWalk {

	/**
	 * The dictionary, seen as a synset tree, with the nodes being the synsets
	 * and the edges being the relations between them.
	 */
	RoWordNet RoWN;

	/**
	 * The id of the synset from where the search begins. Note that this class
	 * variable and the rootLiteral class variable exclude one-another: only one
	 * of them is non-null in a BFWalk object(either we start the search from a
	 * literal or from a synset).
	 */
	String rootSynsetID;

	/**
	 * The is of the literal from where the search begins. Note that this class
	 * variable and the rootSynsetID class variable exclude one-another: only
	 * one of them is non-null in a BFWalk object(either we start the search
	 * from a literal or from a synset).
	 */
	Literal rootLiteral;

	/**
	 * Flag that indicates if all the possible relations between synsets are to
	 * be considered when parsing the tree.
	 */
	boolean allowAllRelations;

	/**
	 * Array of relations with the following meaning:
	 * <ul>
	 * <li>
	 * if allowAllRelations variable is true, the relations contained in the
	 * array will be ignored when parsing the tree;</li>
	 * <li>
	 * if allowAllRelations variable is false, the relations contained in the
	 * array will be the only ones considered when parsing the tree;</li>
	 * </ul>
	 */
	String filteredRelations[];

	/**
	 * Queue used by the BFS algorithm to deposit and extract nodes starting
	 * from the root and filtering the relations correspondingly at each step.
	 */
	Queue<String> bfsQueue;

	/**
	 * Structure used to store the nodes added to the bfQueue throughout the
	 * BFS, used for avoiding infinite loops.
	 */
	HashSet<String> analyzed;

	/**
	 * Class constructor.
	 * <p>
	 * The allowAllRelations flag is initialized to 'true' and the bfsQueue is
	 * implemented and initialized as a LinkedList. All other class variables
	 * are initialized to null(the HashSet is initialized to an empty one).
	 * </p>
	 */
	public BFWalk() {
		this.RoWN = null;
		this.rootSynsetID = null;
		this.rootLiteral = null;
		this.allowAllRelations = true;
		this.filteredRelations = null;

		bfsQueue = new LinkedList<String>();
		analyzed = new HashSet<String>();
	}

	/**
	 * Class constructor.
	 * <p>
	 * It builds a BFWalk object with the root being a synset.
	 * </p>
	 * <p>
	 * The bfsQueue is implemented and initialized as a LinkedList. The first
	 * node(the root) is added to the queue. The HashSet is initialized and the
	 * root is added to it.
	 * </p>
	 * <p>
	 * All other class variables are initialized in accordance with the
	 * constructor parameters.
	 * </p>
	 * 
	 * @param RoWN
	 *            the value to which the RoWN class variable is set
	 * @param rootSynsetID
	 *            the value to which the rootSynsetID class variable is
	 *            set(rootLiteral will be set to null)
	 * @param allowAllRelations
	 *            the value to which the allowAllRelations class variable is set
	 * @param filteredRelations
	 *            the value to which the filteredRelations class variable is set
	 */
	public BFWalk(RoWordNet RoWN, String rootSynsetID, boolean allowAllRelations, String filteredRelations[]) {
		this.RoWN = RoWN;
		this.rootSynsetID = rootSynsetID;
		this.rootLiteral = null;
		this.allowAllRelations = allowAllRelations;
		this.filteredRelations = filteredRelations;

		bfsQueue = new LinkedList<String>();
		bfsQueue.add(rootSynsetID);
		analyzed = new HashSet<String>();
		analyzed.add(rootSynsetID);
	}

	/**
	 * Class constructor.
	 * <p>
	 * It builds a BFWalk object with the root being a synset.
	 * </p>
	 * <p>
	 * The bfsQueue is implemented and initialized as a LinkedList. The first
	 * node(the root) is added to the queue. The HashSet is initialized and the
	 * root is added to it.
	 * </p>
	 * <p>
	 * The allowAllRelations flag is set to true and the filteredRelations array
	 * to null.
	 * </p>
	 * <p>
	 * All other class variables are initialized in accordance with the
	 * constructor parameters.
	 * </p>
	 * 
	 * @param RoWN
	 *            the value to which the RoWN class variable is set
	 * @param rootSynsetID
	 *            the value to which the rootSynsetID class variable is
	 *            set(rootLiteral will be set to null)
	 */
	public BFWalk(RoWordNet RoWN, String rootSynsetID) {
		this.RoWN = RoWN;
		this.rootSynsetID = rootSynsetID;
		this.rootLiteral = null;
		this.allowAllRelations = true;
		this.filteredRelations = null;

		bfsQueue = new LinkedList<String>();
		bfsQueue.add(rootSynsetID);
		analyzed = new HashSet<String>();
		analyzed.add(rootSynsetID);
	}

	/**
	 * Class constructor.
	 * <p>
	 * It builds a BFWalk object with the root being a literal.
	 * </p>
	 * <p>
	 * The bfsQueue is implemented and initialized as a LinkedList. The first
	 * node(the root) is added to the queue. The HashSet is initialized and the
	 * root is added to it.
	 * </p>
	 * <p>
	 * All other class variables are initialized in accordance with the
	 * constructor parameters.
	 * </p>
	 * 
	 * @param RoWN
	 *            the value to which the RoWN class variable is set
	 * @param rootLiteral
	 *            the value to which the rootLiteral class variable is
	 *            set(rootSynsetID will be set to null)
	 * @param allowAllRelations
	 *            the value to which the allowAllRelations class variable is set
	 * @param filteredRelations
	 *            the value to which the filteredRelations class variable is set
	 */
	public BFWalk(RoWordNet RoWN, Literal rootLiteral, boolean allowAllRelations, String filteredRelations[]) {
		this.RoWN = RoWN;
		this.rootSynsetID = null;
		this.rootLiteral = rootLiteral;
		this.allowAllRelations = allowAllRelations;
		this.filteredRelations = filteredRelations;

		String rootSynID = RoWN.getSynsetFromLiteral(rootLiteral).getId();

		bfsQueue = new LinkedList<String>();
		bfsQueue.add(rootSynID);
		analyzed = new HashSet<String>();
		analyzed.add(rootSynID);
	}

	/**
	 * Class constructor.
	 * <p>
	 * It builds a BFWalk object with the root being a literal.
	 * </p>
	 * <p>
	 * The bfsQueue is implemented and initialized as a LinkedList. The first
	 * node(the root) is added to the queue. The HashSet is initialized and the
	 * root is added to it.
	 * </p>
	 * <p>
	 * The allowAllRelations flag is set to true and the filteredRelations array
	 * to null.
	 * </p>
	 * <p>
	 * All other class variables are initialized in accordance with the
	 * constructor parameters.
	 * </p>
	 * 
	 * @param RoWN
	 *            the value to which the RoWN class variable is set
	 * @param rootLiteral
	 *            the value to which the rootLiteral class variable is
	 *            set(rootSynsetID will be set to null)
	 */
	public BFWalk(RoWordNet RoWN, Literal rootLiteral) {
		this.RoWN = RoWN;
		this.rootSynsetID = null;
		this.rootLiteral = rootLiteral;
		this.allowAllRelations = true;
		this.filteredRelations = null;

		String rootSynID = RoWN.getSynsetFromLiteral(rootLiteral).getId();

		bfsQueue = new LinkedList<String>();
		bfsQueue.add(rootSynID);
		analyzed = new HashSet<String>();
		analyzed.add(rootSynID);
	}

	/**
	 * Boolean method that returns true if a relation passed as argument(in
	 * String format) is contained in the restriction array(the
	 * filteredRelations class variable) and false otherwise. Useful for the
	 * nextSynset() method.
	 * 
	 * @param rel
	 *            the searched relation
	 * @return true if the relation is contained in the filteredRelations array
	 *         and false otherwise
	 */
	boolean isFiltered(String rel) {
		if (filteredRelations != null && filteredRelations.length != 0)
			for (String s : filteredRelations)
				if (rel.equals(s))
					return true;
		return false;
	}

	/**
	 * Method that returns the id of the next synset in the bfsQueue. At each
	 * step, after extracting the next synset from the bfsQueue the IDs of the
	 * synsets related to it are added to the bfsQueue after filtering the
	 * extracted synset's relations in correspondence to the values of the
	 * allowAllRelations and filteredRelations class variables.
	 * 
	 * @return the id of the next synset in the bfsQueue
	 * @throws Exception
	 *             if no relation is left to analyze after applying the
	 *             restrictions
	 */
	public String nextSynset() throws Exception {
		String nextSynsetID = bfsQueue.remove();

		if (!allowAllRelations)
			if (filteredRelations != null && filteredRelations.length != 0)
				for (String s : filteredRelations) {
					ArrayList<String> ends = RoWN
							.getRelatedSynsetIds(nextSynsetID, s);
					for (String end : ends)
						if (!analyzed.contains(end)) {
							bfsQueue.add(end);
							analyzed.add(end);
						}
				}
			else
				throw new Exception("No relation to analyse after applying restrictions! ");
		else if (isFiltered("*"))
			throw new Exception("No relation to analyse after applying restrictions! ");
		else
			for (Relation r : RoWN.getSynsetById(nextSynsetID).getRelations())
				if (!isFiltered(r.getRelation())) {
					ArrayList<String> ends = RoWN
							.getRelatedSynsetIds(nextSynsetID, r.getRelation());
					for (String end : ends)
						if (!analyzed.contains(end)) {
							bfsQueue.add(end);
							analyzed.add(end);
						}
				}
		return nextSynsetID;
	}

	/**
	 * Boolean method that analyzes whether there are any more synsets in the
	 * bfsQueue.
	 * 
	 * @return false if the bfsQueue is empty, true otherwise
	 */
	public boolean hasMoreSynsets() {
		if (!bfsQueue.isEmpty())
			return true;
		return false;
	}
}
