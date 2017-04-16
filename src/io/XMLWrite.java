package io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import data.Literal;
import data.Relation;
import data.RoWordNet;
import data.Synset;

/**
 * Class used to write a RoWordNet object into an XML file. It has two writing
 * modes, default (compressed) and formatted (human-readable).
 * 
 * @author Stefan Dumitrescu
 * @author Radu Petrut
 */
public class XMLWrite {

	static XMLStreamWriter out;
	static boolean compressed;

	static public void write(RoWordNet rown, String filePath, boolean overwrite, boolean compressedOutput) throws IOException, XMLStreamException, FactoryConfigurationError {
		compressed = compressedOutput;
		if (((File) new File(filePath)).exists() && !overwrite)
			throw new IOException("File " + filePath + " already exists and the overwrite flag is set to false!");

		OutputStream outputStream = new FileOutputStream(new File(filePath));

		out = XMLOutputFactory
				.newInstance()
				.createXMLStreamWriter(new OutputStreamWriter(outputStream, "UTF8"));

		out.writeStartDocument();
		nl();
		out.writeStartElement("ROWN");
		nl();

		writeArrayList(rown.synsets);

		force_nl();
		nl();
		out.writeEndElement();
		nl();
		out.writeEndDocument();
		out.flush();
		out.close();
	}

	static private void writeHashMap(HashMap<String, Synset> hm) throws XMLStreamException {
		for (Synset s : hm.values())
			writeSynset(s);
	}

	static private void writeArrayList(ArrayList<Synset> als) throws XMLStreamException {
		for (Synset s : als)
			writeSynset(s);
	}

	static private void writeSynset(Synset s) throws XMLStreamException {
		force_nl();
		out.writeStartElement("SYNSET");

		// ID
		nl();
		tab();
		out.writeStartElement("ID");
		out.writeCharacters(s.getId());
		out.writeEndElement();

		// PWN20
		if (s.getPwn20() != null) {
			if (s.getPwn20().size() != 0) {
				for (String pwn : s.getPwn20()) {
					nl();
					tab();
					out.writeStartElement("PWN20");
					out.writeCharacters(pwn);
					out.writeEndElement();
				}
			}
		}

		// POS
		nl();
		tab();
		out.writeStartElement("POS");
		out.writeCharacters(Synset.typeToString(s.getPos()));
		out.writeEndElement();

		if (s.getNl() != null) {
			nl();
			tab();
			out.writeStartElement("NL");
			out.writeCharacters(s.getNl());
			out.writeEndElement();
		}

		// SYNONYM
		if (s.getLiterals() != null) {
			nl();
			tab();
			out.writeStartElement("SYNONYM");
			for (Literal l : s.getLiterals()) {
				nl();
				tab(2);
				out.writeStartElement("LITERAL");
				out.writeCharacters(l.getLiteral());
				out.writeStartElement("SENSE");
				out.writeCharacters(l.getSense());
				out.writeEndElement();
				out.writeEndElement();
			}
			nl();
			tab();
			out.writeEndElement();
		}

		// STAMP
		if (s.getStamp() != null) {
			nl();
			tab();
			out.writeStartElement("STAMP");
			out.writeCharacters(s.getStamp());
			out.writeEndElement();
		}

		// ILR
		if (s.getRelations() != null) {
			for (Relation r : s.getRelations()) {
				nl();
				tab();
				out.writeStartElement("ILR");
				out.writeCharacters(r.getTargetSynset());
				out.writeStartElement("TYPE");
				out.writeCharacters(r.getRelation());
				out.writeEndElement();
				if (r.getSourceLiteral() != null) {
					out.writeStartElement("SRCL");
					out.writeCharacters(r.getSourceLiteral());
					out.writeEndElement();
				}
				if (r.getTargetLiteral() != null) {
					out.writeStartElement("TRGL");
					out.writeCharacters(r.getTargetLiteral());
					out.writeEndElement();
				}
				out.writeEndElement();
			}
		}

		// DEF
		if (s.getDefinition() != null) {
			nl();
			tab();
			out.writeStartElement("DEF");
			out.writeCharacters(s.getDefinition());
			out.writeEndElement();
		}// else System.out.println(s.getId());

		// USAGE
		if (s.getUsage() != null) {
			if (s.getUsage().size() != 0) {
				for (String usage : s.getUsage()) {
					nl();
					tab();
					out.writeStartElement("USAGE");
					out.writeCharacters(usage);
					out.writeEndElement();
				}
			}
		}

		// DOMAIN
		if (s.getDomain() != null) {
			nl();
			tab();
			out.writeStartElement("DOMAIN");
			out.writeCharacters(s.getDomain());
			out.writeEndElement();
		}

		// SUMO
		if (s.getSumo() != null) {
			nl();
			tab();
			out.writeStartElement("SUMO");
			out.writeCharacters(s.getSumo());
			out.writeStartElement("TYPE");
			out.writeCharacters(s.getSumotype());
			out.writeEndElement();
			out.writeEndElement();
		}

		// SENTIWN
		if (s.getSentiwn_p() != null) {
			nl();
			tab();
			out.writeStartElement("SENTIWN");
			out.writeStartElement("P");
			out.writeCharacters(s.getSentiwn_p());
			out.writeEndElement();
			out.writeStartElement("N");
			out.writeCharacters(s.getSentiwn_n());
			out.writeEndElement();
			out.writeStartElement("O");
			out.writeCharacters(s.getSentiwn_o());
			out.writeEndElement();
			out.writeEndElement();
		}
		// close SYNSET
		nl();
		out.writeEndElement();
	}

	static private void force_nl() throws XMLStreamException {
		out.writeCharacters("\n");
	}

	static private void nl() throws XMLStreamException {
		if (compressed)
			return;
		out.writeCharacters("\n");
	}

	static private void nl(int nls) throws XMLStreamException {
		if (compressed)
			return;
		for (int i = 0; i < nls; i++)
			out.writeCharacters("\n");
	}

	static private void tab() throws XMLStreamException {
		if (compressed)
			return;
		out.writeCharacters("\t");
	}

	static private void tab(int tabs) throws XMLStreamException {
		if (compressed)
			return;
		for (int i = 0; i < tabs; i++)
			out.writeCharacters("\t");
	}
}
