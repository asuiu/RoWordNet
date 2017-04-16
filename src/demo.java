import data.Literal;
import data.Relation;
import data.RoWordNet;
import data.Synset;
import io.IO;
import op.BFWalk;
import op.Operation;
import op.SimilarityMetrics;
import utils.Timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static java.util.Arrays.asList;

/**
 * This class's purpose is to illustrate various ways the API can be used by
 * offering some usage examples.
 *
 * @author Stefan Dumitrescu
 * @author Radu Petrut
 */
public class demo {

    public static void runDemos() throws Exception {

        IO.outln("Starting demos...\n");

        IO.outln("Loading RoWordNet ...");
        Timer timer = new Timer();
        // to read from XML :
        // RoWordNet rown = new RoWordNet(XMLRead.read("res\\rown.xml"));
        // to read from raw (serialized) object :
        RoWordNet rown = RoWordNet.deserializeFromFile("res\\RoWordNet.data");

        IO.outln("Done: " + timer.mark());

        IO.outln("\n\nDemo 1: Basic Operations\n------------------------------------------------------------------------\n");
        demo_basicOps(rown);

        IO.outln("\n\n\n\nDemo 2: WordNet Object Operations\n------------------------------------------------------------------------\n");
        demo_WordNetOps();

        IO.outln("\n\n\n\nDemo 3: WordNet Breadth-First Walk\n------------------------------------------------------------------------\n");
        demo_bfWalk(rown);

        IO.outln("\n\n\n\nDemo 4: Finding the LCS (lowest common subsumer)\n------------------------------------------------------------------------\n");
        demo_LCS(rown);

        IO.outln("\n\n\n\nDemo 5: Similarity Metrics\n------------------------------------------------------------------------\n");
        demo_SimilarityMetrics(rown, "res\\custom.corpus.ro");

        IO.outln("\n\n Demos have completed successfuly.");
    }

    /**
     * Method that illustrates how the basic data structures and their
     * associated operations work.
     *
     * @param RoWordNet_XMLFilePath the path where the dictionary is located on disk, under the
     *                              form of an XML file
     * @throws Exception
     */
    public static void demo_basicOps(RoWordNet rown) throws Exception {
        Timer timer = new Timer();

		/* Objects and components. */
        Literal literal;
        /* Print stats about the recently loaded object. */
        IO.outln("\n*** Displaying statistics ...");
        timer.start();
        IO.outln(rown.getStats());
        IO.outln("Done: " + timer.mark());

		/* Iterate through all synsets. */
        IO.outln("\n*** Iterating through all synsets ...");
        timer.start();
        int synNo = 0;
        for (String synsetID : rown.synsetsMap.keySet()) {
            Synset syn;
			/* method 1 */
            syn = rown.getSynsetById(synsetID);
			/* method 2 */
            syn = rown.synsetsMap.get(synsetID);
            synNo++;
        }
        IO.outln(synNo + " synsets found in the dictionary.");
        IO.outln("Done: " + timer.mark());
        // Get list of words & set of relations
        ArrayList<String> words = new ArrayList<>();
        HashSet<String> relations = new HashSet<>();
        for (Synset s : rown.synsetsMap.values()) {
            if (s.getLiterals() != null)
                for (Literal l : s.getLiterals()) {
                    words.add(l.getLiteral());
                }
            for (Relation r : s.getRelations()) {
                relations.add(r.getRelation());
            }
        }

        IO.outln("\n*** Searching for literals in RoWordNet ...");
        timer.start();
        literal = new Literal("demo_literal");
        if (rown.containsLiteral(literal))
            IO.outln("Literal '" + literal.getLiteral() + "' found in synset " + rown
                    .getSynsetFromLiteral(literal).getId() + ".");
        else
            IO.outln("Literal '" + literal.getLiteral() + "' not found.");

        literal = rown.synsets.get(13).getLiterals().get(0);
        if (rown.containsLiteral(literal))
            IO.outln("Literal '" + literal.getLiteral() + "' found in synset " + rown
                    .getSynsetFromLiteral(literal).getId() + ".");
        else
            IO.outln("Literal '" + literal.getLiteral() + "' not found.");
        IO.outln("Done: " + timer.mark());

        IO.outln("\n*** Extracting noun synsets ...");
        timer.start();
        HashMap<String, Synset> nounSynsets = rown
                .getHashedSynsetsByPos(Synset.Type.Noun);
        IO.outln(nounSynsets.keySet().size() + " noun synsets found.");
        IO.outln("Done: " + timer.mark());

        literal = new Literal("con");
        IO.outln("\n*** Fetching the synsets that contain literal '" + literal
                .getLiteral() + "' ...");
        timer.start();
        ArrayList<String> ids = rown.getIdsFromLiteral(literal);
        IO.outln(ids.size() + " synsets found.");
        IO.outln("Done: " + timer.mark());

        IO.outln("\n*** Extracting verb synsets ...");
        timer.start();
        ArrayList<Synset> verbSynsets = rown
                .getListedSynsetsByPos(Synset.Type.Verb);
        IO.outln(verbSynsets.size() + " verb synsets found.");
        IO.outln("Done: " + timer.mark());

        IO.outln("\n*** Generating id for new prefix using getNewID ...");
        timer.start();
        String newId = rown.getNewId("GetNew-", "-demo");
        System.out.println("\t New Id for new prefix: " + newId);
        IO.outln("Done: " + timer.mark());

        IO.outln("\n*** Generating id for existing prefix using getNewID ...");
        timer.start();
        newId = rown.getNewId("ENG30-", "-n");
        System.out.println("\t New Id for new prefix: " + newId);
        IO.outln("Done: " + timer.mark());

        IO.outln("\n*** Generating ids for new prefix using getIncrementalID ...");
        timer.start();
        for (int i = 0; i < 5; i++) {
            newId = rown.getNewIncrementalId("NEW12-", "-p");
            System.out
                    .println("\t Incremental Id " + i + " for new prefix: " + newId);
        }
        IO.outln("Done: " + timer.mark());

        IO.outln("\n*** Generating ids for existing prefix using getIncrementalID ...");
        timer.start();
        for (int i = 0; i < 5; i++) {
            newId = rown.getNewIncrementalId("ENG30-", "-n");
            System.out
                    .println("\t Incremental Id " + i + " for existing prefix: " + newId);
        }
        IO.outln("Done: " + timer.mark());

		/*
		 * Adding a new synset, setting only its ID, POS, Definition, Domain as
		 * well as 2 Literals and 2 Relations
		 */
        IO.outln("\n*** Adding a new synset with 2 Literals and 2 Relations ...");
        timer.start();

        Synset s = new Synset();

        s.setId(rown.getNewIncrementalId("ENG30-", "-n"));
        s.setPos(Synset.Type.Noun);
        s.setDefinition("New definition");
        s.setDomain("New domain");

		/*---Literals---*/

		/* Creating a Literal ArrayList (container) */
        ArrayList<Literal> literalArray = new ArrayList<>();

		/* Creating and adding the first Literal */
        Literal l = new Literal();
        l.setLiteral("Literal 1");
        l.setSense("S1");
        literalArray.add(l);

		/* Creating and adding the second Literal (direct method) */
        literalArray.add(new Literal("Literal 2", "S2"));

		/* Adding the Literal container to the Synset */
        s.setLiterals(literalArray);

		/*---Relations---*/

		/* Creating a Relation ArrayList (container) */
        ArrayList<Relation> relationArray = new ArrayList<>();

		/* Creating and adding the first new Relation */
        Relation r = new Relation();
        r.setRelation("hypernym");
        r.setSourceSynset(s.getId());
        r.setTargetSynset("NEWSYNSET-002-N");
        relationArray.add(r);

		/* Creating and adding the second Relation (direct method) */
        relationArray
                .add(new Relation(s.getId(), "NEWSYNSET-002-N", "hyponym"));

		/* Adding the Relation container to the Synset */
        s.setRelations(relationArray);

		/* Adding the synset to the RoWordNet object */
        rown.addSynset(s, true);

		/* Printing the synset's details */
        IO.outln(rown.getSynsetById(s.getId()).toString());
        IO.outln("Done: " + timer.mark());
    }

    /**
     * Method that demos the set operations : union, intersection, difference,
     * etc.
     *
     * @throws Exception
     */
    private static void demo_WordNetOps() throws Exception {

        RoWordNet r1, r2, result;
        Synset s;
        Timer timer = new Timer();
        String time = "";

        IO.outln("Creating two RoWN objects R1 and R2");
        timer.start();
        r1 = new RoWordNet();
        r2 = new RoWordNet();

        // adding synsets to O1
        s = new Synset();
        s.setId("S1");
        s.setLiterals(new ArrayList<Literal>(asList(new Literal("L1"))));
        r1.addSynset(s, false); // adding synset S1 to O2;

        s = new Synset();
        s.setId("S2");
        s.setLiterals(new ArrayList<Literal>(asList(new Literal("L2"))));
        r1.addSynset(s, false); // adding synset S2 to O1;

        // adding synsets to O2
        s = new Synset();
        s.setId("S1");
        s.setLiterals(new ArrayList<Literal>(asList(new Literal("L1"))));
        r2.addSynset(s, false); // adding synset S1 to O2;

        s = new Synset();
        s.setId("S2");
        s.setLiterals(new ArrayList<Literal>(asList(new Literal("L4"))));
        r2.addSynset(s, false); // adding synset S2 to O2, but with literal L4
        // not L2;

        s = new Synset();
        s.setId("S3");
        s.setLiterals(new ArrayList<Literal>(asList(new Literal("L3"))));
        r2.addSynset(s, false); // adding synset S3 to O2;
        time = timer.mark();

        // print current objects
        IO.outln("\n Object R1:");
        for (Synset syn : r1.synsets) {
            IO.outln(syn.toString());
        }

        IO.outln("\n Object R2:");
        for (Synset syn : r2.synsets) {
            IO.outln(syn.toString());
        }
        IO.outln("Done: " + time);

        // DIFFERENCE
        IO.outln("\nOperation DIFFERENCE(R1,R2) (different synsets in R1 and R2 having the same id):");
        timer.start();
        String[] diff = Operation.diff(r1, r2);
        time = timer.mark();

        for (String id : diff) {
            IO.outln("Different synset having the same id in both RoWN objects: " + id);
        }

        IO.outln("Done: " + time);

        // UNION
        IO.outln("\nOperation UNION(R1,R2):");
        timer.start();
        try {
            result = new RoWordNet(r1);// we copy-construct the result object
            // after r1 because the union and merge
            // methods work in-place directly on the
            // first parameter of the methods
            result = Operation.union(result, r2);
            IO.outln(" Union object:");
            for (Synset syn : result.synsets) {
                IO.outln(syn.toString());
            }
        } catch (Exception ex) {
            IO.outln("Union operation failed (as it should, as synset S2 is in both objects but has a different literal and are thus two different synsets having the same id that cannot reside in a single RoWN object). \nException message: " + ex
                    .getMessage());
        }
        IO.outln("Done: " + timer.mark());

        // MERGE
        IO.outln("\nOperation MERGE(R1,R2):");

        result = new RoWordNet(r1);
        timer.start();
        result = Operation.merge(result, r2);
        time = timer.mark();

        IO.outln(" Merged object (R2 overwritten on R1):");
        for (Synset syn : result.synsets) {
            IO.outln(syn.toString());
        }
        IO.outln("Done: " + time);

        // COMPLEMENT
        IO.outln("\nOperation COMPLEMENT(R2,R1) (compares only synset ids):");
        timer.start();
        String[] complement = Operation.complement(r2, r1);
        time = timer.mark();
        IO.outln(" Complement ids:");
        for (String id : complement) {
            IO.outln("Synset that is in R2 but not in R1: " + id);
        }
        IO.outln("Done: " + timer.mark());

        // INTERSECTION
        IO.outln("\nOperation INTERSECTION(R1,R2) (fully compares synsets):");
        timer.start();
        String[] intersection = Operation.intersection(r1, r2);
        time = timer.mark();
        IO.outln(" Intersection ids:");
        for (String id : intersection) {
            IO.outln("Equal synset in both R1 and R2: " + id);
        }
        IO.outln("Done: " + timer.mark());

        // IO.outln(((Synset) r1.getSynsetById("S1")).equals((Synset)
        // r2.getSynsetById("S1")));

    }

    /**
     * Demo of BFWalk on the RoWordNet semantic network
     *
     * @param rown input object
     * @throws Exception
     */
    private static void demo_bfWalk(RoWordNet rown) throws Exception {
        Synset source = rown.getSynsetsFromLiteral(new Literal("miere")).get(0);
        if (source == null)
            throw new Exception("Synset not found!");
        BFWalk bf = new BFWalk(rown, source.getId());
        IO.outln("Starting BFWalk on all relations with source synset:\n" + source
                .toString());

        int cnt = 0;
        while (bf.hasMoreSynsets()) {
            IO.outln("\n** STEP " + (++cnt));
            Synset step = rown.getSynsetById(bf.nextSynset());
            IO.outln(step.toString());
            if (cnt > 6) {
                IO.outln("\n Stopping BFWalk and returning.");
                break;
            }
        }
    }

    /**
     * Method that exemplifies finding the lowest common subsumer of two
     * synsets.
     *
     * @param RoWN the synset hierarchy
     * @throws Exception
     */
    public static void demo_LCS(RoWordNet RoWN) throws Exception {
        Timer timer = new Timer();
        String lcs;

        boolean allowAllRelations = true;

        Literal l1 = new Literal("viață");
        Literal l2 = new Literal("vietate");
        String s1 = RoWN.getSynsetsFromLiteral(l1).get(5).getId();
        String s2 = RoWN.getSynsetsFromLiteral(l2).get(0).getId();

        Literal l3 = new Literal("fenomen");
        Literal l4 = new Literal("proces");
        String s3 = RoWN.getSynsetsFromLiteral(l3).get(1).getId();
        String s4 = RoWN.getSynsetsFromLiteral(l4).get(1).getId();

        IO.outln("\n*** Searching for the LCS with all relations as the allowed relation ...");
        timer.start();
        IO.outln("Source synset: " + s1 + " " + ((Synset) RoWN
                .getSynsetById(s1)).toString());
        IO.outln("Target synset: " + s2 + " " + ((Synset) RoWN
                .getSynsetById(s2)).toString());
        lcs = SimilarityMetrics
                .lowestCommonSubsumer(RoWN, s1, s2, allowAllRelations);
        IO.outln("LCS found: " + lcs + " " + ((Synset) RoWN.getSynsetById(lcs))
                .toString());
        IO.outln();
        IO.outln("Done: " + timer.mark());

        allowAllRelations = false;
        IO.outln("\n*** Searching the LCS with only hypernymy as allowed edge ...");
        timer.start();
        IO.outln("Source synset: " + s3 + " " + ((Synset) RoWN
                .getSynsetById(s3)).toString());
        IO.outln("Target synset: " + s4 + " " + ((Synset) RoWN
                .getSynsetById(s4)).toString());
        lcs = SimilarityMetrics
                .lowestCommonSubsumer(RoWN, s3, s4, allowAllRelations);
        IO.outln("LCS found: " + lcs + " " + ((Synset) RoWN.getSynsetById(lcs))
                .toString());
        IO.outln();
        IO.outln("Done: " + timer.mark());
    }


    /**
     * Method that demonstrates the use of the functions in SimilarityMetrics.
     *
     * @param RoWN                the synset hierarchy
     * @param textCorpus_FilePath the path where the corpus used for computing the IC is located
     *                            on disk
     * @throws Exception
     */
    public static void demo_SimilarityMetrics(RoWordNet RoWN, String textCorpus_FilePath) throws Exception {
        boolean allowAllRelations = true;

        Literal l1 = new Literal("salvare");
        Literal l2 = new Literal("dotare");
        String id1 = RoWN.getSynsetsFromLiteral(l1).get(0).getId();
        String id2 = RoWN.getSynsetsFromLiteral(l2).get(0).getId();
        Synset s1, s2;

        s1 = RoWN.getSynsetById(id1);
        s2 = RoWN.getSynsetById(id2);

        IO.outln("Computed IC for synset_1: " + s1.getInformationContent());
        IO.outln("Computed IC for synset_2: " + s2.getInformationContent());
        IO.outln("Custom distance: " + SimilarityMetrics.distance(RoWN, s1
                .getId(), s2.getId(), true, null));
        IO.outln("Custom hypernymy distance: " + SimilarityMetrics
                .distance(RoWN, s1.getId(), s2.getId()));

        IO.outln("Sim Resnik: " + SimilarityMetrics.Resnik(RoWN, s1.getId(), s2
                .getId(), allowAllRelations));
        IO.outln("Sim Lin: " + SimilarityMetrics.Lin(RoWN, s1.getId(), s2
                .getId(), allowAllRelations));
        IO.outln("Sim JCN: " + SimilarityMetrics.JiangConrath(RoWN, s1.getId(), s2
                .getId(), allowAllRelations));
        IO.outln("Dist JCN: " + SimilarityMetrics
                .JiangConrath_distance(RoWN, s1.getId(), s2.getId(), allowAllRelations));
    }


}
