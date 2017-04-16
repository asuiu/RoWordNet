package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import data.Literal;
import data.Relation;
import data.Synset;

/**
 * Class used to read a RoWordNet XML file into a RoWordNet object
 * 
 * @author Stefan Dumitrescu
 * @author Radu Petrut
 */
public class XMLRead extends DefaultHandler {

	InputSource is;
	String temp;
	ArrayList<Synset> synsets;
	Synset tempSynset;
	boolean inRelation, inSumo;
	ArrayList<Literal> literals;
	Literal literal;
	ArrayList<Relation> relations;
	Relation relation;
	ArrayList<String> usages, pwn20;

	/**
	 * Class constructor.
	 * 
	 * @param filePath the path where the XML file is located on disk
	 * @throws IOException if IO.openFile() throws IOException
	 */
	public XMLRead(String filePath) throws IOException{
		BufferedReader f3 = IO.openFile(filePath);

		this.is = new InputSource(f3);
		this.is.setEncoding("UTF-8");

		this.synsets = new ArrayList<Synset>();
	}

	@Override
	public void startDocument() throws SAXException {

	}
	
	@Override
	public void characters(char[] buffer, int start, int length) {
		temp += new String(buffer, start, length);

	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// System.out.println("E: "+qName);
		if (qName.equalsIgnoreCase("SENSE")) {
			literal.setLiteral(temp);// System.out.println(literal.getLiteral());
			temp = "";
			return;
		}

		if (qName.equalsIgnoreCase("TYPE")) { // id obiect este gata de citire
			if (inRelation) {
				relation.setTargetSynset(temp);
				relation.setSourceSynset(tempSynset.getId());
			}
			if (inSumo) {
				tempSynset.setSumo(temp);
			}
			temp = "";
			return;
		}

		temp = "";

		if (qName.equalsIgnoreCase("SYNSET")) {
			tempSynset = new Synset();
			literal = new Literal();
			literals = new ArrayList<Literal>(3);
			relations = new ArrayList<Relation>(10);
			usages = new ArrayList<String>(3);
			pwn20 = new ArrayList<String>(3);
			return;
		}
		if (qName.equalsIgnoreCase("SYNONYM")) {
			return;
		}
		if (qName.equalsIgnoreCase("LITERAL")) {
			literal = new Literal();
			return;
		}
		if (qName.equalsIgnoreCase("ILR")) {
			relation = new Relation();
			inRelation = true;
			return;
		}
		if (qName.equalsIgnoreCase("SUMO")) {
			inSumo = true;
			return;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if (qName.equalsIgnoreCase("SYNSET")) {
			tempSynset.setRelations(relations);
			tempSynset.setUsage(usages);
			tempSynset.setPwn20(pwn20);
			synsets.add(tempSynset);
			return;
		}
		if (qName.equalsIgnoreCase("ID")) {
			tempSynset.setId(temp);
			return;
		}
		if (qName.equalsIgnoreCase("PWN20")) {
			pwn20.add(temp);
			return;
		}
		if (qName.equalsIgnoreCase("POS")) {
			Synset.Type type = Synset.stringToType(temp);
			if (type == null)
				throw new SAXException("Unidentified POS: " + qName);
			tempSynset.setPos(type);
			return;
		}
		if (qName.equalsIgnoreCase("DEF")) {
			tempSynset.setDefinition(temp);
			return;
		}
		if (qName.equalsIgnoreCase("STAMP")) {
			tempSynset.setStamp(temp);
			return;
		}
		if (qName.equalsIgnoreCase("DOMAIN")) {
			tempSynset.setDomain(temp);
			return;
		}
		if (qName.equalsIgnoreCase("LITERAL")) {
			literals.add(literal);
			literal = null;
			return;
		}
		if (qName.equalsIgnoreCase("SENSE")) {
			literal.setSense(temp);
			return;
		}
		if (qName.equalsIgnoreCase("SYNONYM")) {
			tempSynset.setLiterals(literals);
			return;
		}
		if (qName.equalsIgnoreCase("TYPE")) {
			if (inRelation) {
				relation.setRelation(temp);
				return;
			}
			if (inSumo) {
				tempSynset.setSumotype(temp);
				return;
			}
		}
		if (qName.equalsIgnoreCase("ILR")) {
			relations.add(relation);
			relation = null;
			inRelation = false;
			return;
		}
		if (qName.equalsIgnoreCase("SUMO")) {
			inSumo = false;
			return;
		}
		if (qName.equalsIgnoreCase("P")) {
			tempSynset.setSentiwn_p(temp);
			return;
		}
		if (qName.equalsIgnoreCase("N")) {
			tempSynset.setSentiwn_n(temp);
			return;
		}
		if (qName.equalsIgnoreCase("O")) {
			tempSynset.setSentiwn_o(temp);
			return;
		}
		if (qName.equalsIgnoreCase("NL")) {
			tempSynset.setNl(temp);
			tempSynset.setNon_lexicalized(true);
			return;
		}
		if (qName.equalsIgnoreCase("SRCL")) {
			relation.setSourceLiteral(temp);
			return;
		}
		if (qName.equalsIgnoreCase("TRGL")) {
			relation.setTargetLiteral(temp);
			return;
		}
		if (qName.equalsIgnoreCase("USAGE")) {
			usages.add(temp);
			return;
		}
	}
	
	/**
	 * Method that extracts and saves the synsets from an XML file.
	 * 
	 * @param filePath the XML file's location on disk
	 * @return an ArrayList containing the extracted synsets
	 * @throws Exception 
	 */
	public static ArrayList<Synset> read(String filePath) throws Exception {
		XMLRead r = new XMLRead(filePath);

		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser saxParser = spf.newSAXParser();

		XMLReader xmlReader = saxParser.getXMLReader();
		xmlReader.setContentHandler(r);
		xmlReader.parse(r.is);

		return r.synsets;
	}
}
