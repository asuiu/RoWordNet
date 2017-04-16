package op;

import data.RoWordNet;

/**
 * <p>
 * This class's main purpose is to implement well-known distance measuring
 * algorithms(Lin, Resnik, Jiang-Conrath) in order to compare our custom way 
 * of computing the distance between two synsets in the hierarchy(by counting 
 * the number of edges between them) with the results of these reference 
 * algorithms.
 * </p>
 * <p>
 * The first step in accomplishing this was computing the IC(information content)
 * for each synset in the hierarchy. In order to do this we implemented a 
 * WSD(Word Sense Disambiguation) algorithm. We chose an adapted version of 
 * Michael Lesk's algorithm, but also implemented the original algorithm, for 
 * comparison reasons.
 * </p>
 * <p>
 * The second resource needed was finding the lowest common subsumer between 
 * two synsets. We created a method that does that using the getPath() function
 * in the Operation class.
 * </p>
 * <p>
 * Once the two important resources stated above were computed, implementing
 * the measurement algorithms was quite simple.
 * </p>
 * 
 * @author Stefan Dumitrescu
 * @author Radu Petrut
 * @see Operation 
 * @see BFWalk
 */
public class SimilarityMetrics {	

	/**
	 * Simple distance measure. 
	 * Counts the number of edges between two synsets in the hierarchy.
	 * The relations that represent valid edges are extracted by interpreting
	 * the values of allowAllRelations and filteredRelations parameters.
	 * 
	 * @param RoWN the synset hierarchy
	 * @param sourceSynsetID root synset's ID
	 * @param targetSynsetID target synset's ID
	 * @param allowAllRelations	flag that indicates if all the possible relations
	 *			between synsets are to be considered when parsing the tree
	 * @param filteredRelations 
	 * <ul>Array of relations with the following meaning:
	 *	<li>if allowAllRelations param is true, the relations contained 
	 *		in the array will be ignored when parsing the tree
	 *	</li>	
	 *	<li>if allowAllRelations param is false, the relations contained 
	 *		in the array will be the only ones considered when parsing the tree
	 *	</li>
	 * </ul>
	 * @return
	 * @throws Exception  if no relation is left to analyze after applying the
	 * restrictions
	 */
	public static double distance(RoWordNet RoWN, String sourceSynsetID, String targetSynsetID, boolean allowAllRelations, String[] filteredRelations) throws Exception{
		return Operation.getPath(RoWN, sourceSynsetID, targetSynsetID, allowAllRelations, filteredRelations).length-1;
	}
	
	/**
	 * Simple distance measure. 
	 * Counts the number of edges between two synsets in the hierarchy, 
	 * considering only the hypernym relation as a valid edge.
	 * 
	 * @param RoWN	the synset hierarchy
	 * @param sourceSynsetID root synset's ID
	 * @param targetSynsetID target synset's ID
	 * @return the number of edges between the root and the target node
	 * @throws Exception if the starting node isn't linked to any other node 
	 * through the hypernym relation
	 */
	public static double distance(RoWordNet RoWN, String sourceSynsetID, String targetSynsetID) throws Exception{
		String[] filteredRelations = {"hypernym", "instance_hypernym"};
		return distance(RoWN, sourceSynsetID, targetSynsetID, false, filteredRelations);
	}
	
	/**
	 * Resnik's semantic similarity measure.
	 * 
	 * @param rown
	 * @param synsetId1
	 * @param synsetId2
	 * @return IC(lcs(s1,s2))
	 */
	public static double Resnik(RoWordNet rown, String synsetId1, String synsetId2, boolean allowAllRelations) throws Exception {		
		String lcsId = lowestCommonSubsumer(rown, synsetId1, synsetId2, allowAllRelations);
		return rown.getSynsetById(lcsId).getInformationContent();
	}
	
	/**
	 * Lin's semantic similarity measure.
	 * 
	 * @param rown
	 * @param synsetId1
	 * @param synsetId2
	 * @return 2*Resnik(s1,s2)/[IC(s1)+IC(s2)]
	 */
	public static double Lin(RoWordNet rown, String synsetId1, String synsetId2, boolean allowAllRelations) throws Exception {
		return 2*Resnik(rown, synsetId1, synsetId2, allowAllRelations)/(rown.getSynsetById(synsetId1).getInformationContent()+rown.getSynsetById(synsetId2).getInformationContent());		
	}
	
	/**
	 * Jiang and Conrath's semantic similarity measure.
	 * 
	 * @param rown
	 * @param synsetId1
	 * @param synsetId2
	 * @return 1/[IC(s1) + IC(s2) - 2*Resnik(s1,s2)];
	 */
	public static double JiangConrath(RoWordNet rown, String synsetId1, String synsetId2, boolean allowAllRelations) throws Exception{
		return 1/JiangConrath_distance(rown, synsetId1, synsetId2, allowAllRelations);		
	}
	
	/**
	 * Jiang and Conrath's semantic distance measure.
	 * 
	 * @param rown
	 * @param synsetId1
	 * @param synsetId2
	 * @return IC(s1) + IC(s2) - 2*Resnik(s1,s2)
	 */
	public static double JiangConrath_distance(RoWordNet rown, String synsetId1, String synsetId2, boolean allowAllRelations) throws Exception {
		return rown.getSynsetById(synsetId1).getInformationContent() + rown.getSynsetById(synsetId2).getInformationContent() - 2*Resnik(rown, synsetId1, synsetId2, allowAllRelations);		
	}
	
	/**
	 * Method that finds the lowest common subsumer of two synsets in the 
	 * hierarchy, considering as valid edges only the ones reflecting the
	 * hypernymy relation.
	 * <p>
	 * Each synset's connected component is extracted and stored, the lcs being 
	 * the first common synset between the two components.
	 * </p>
	 * 
	 * @param RoWN the synset hierarchy
	 * @param synsetId1	first synset's ID
	 * @param synsetId2	second synset's ID
	 * @param allowAllRelations flag that specifies whether all relations are
	 * considered to be valid edges on only the hypernymy one
	 * @return the lowest common subsumer's ID
	 * @throws Exception if no relation is left to analyze after applying the
	 * hypernymy restriction, if the flag indicates so
	 */
	public static String lowestCommonSubsumer(RoWordNet RoWN, String synsetId1, 
			String synsetId2, boolean allowAllRelations) throws Exception{
		String[] filteredRelations;
		String[] s1_desc, s2_desc;
		
		if(allowAllRelations){
			filteredRelations = null;
			s1_desc = Operation.getPath(RoWN, synsetId1, synsetId2, true, filteredRelations);
			s2_desc = Operation.getPath(RoWN, synsetId2, synsetId1, true, filteredRelations);
		}
		else{
			filteredRelations = new String[2];
			filteredRelations[0] = "hypernym";
			filteredRelations[1] = "instance_hypernym";
			s1_desc = Operation.getPath(RoWN, synsetId1, synsetId2, false, filteredRelations);
			s2_desc = Operation.getPath(RoWN, synsetId2, synsetId1, false, filteredRelations);
		}
		
		for(String s1:s1_desc)
			for(String s2:s2_desc)
				if(s1.equals(s2))
					return s1;
		return null;
	}
}
