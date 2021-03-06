package de.unikoblenz.west.ldim.index.models;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.TreeSet;

import de.unikoblenz.west.ldim.index.IndexWriter;
import de.unikoblenz.west.ldim.io.NQuad;
import de.unikoblenz.west.ldim.io.NQuadParser;
import de.unikoblenz.west.ldim.tools.Constants;

public class ExtendedCharacteristicSetIndexing {

	public void run(File subjectSortedInput, File indexOutput) {
		try {
			IndexWriter indexWriter = new IndexWriter(indexOutput);
			NQuadParser parserIn = new NQuadParser(subjectSortedInput);
			String currentSubjectURI = null;
			TreeSet<String> signature = new TreeSet<String>();
			PrintStream out = new PrintStream(indexOutput);
			while (parserIn.hasNext()) {
				NQuad nq = parserIn.next();
				if (currentSubjectURI == null) {
					currentSubjectURI = nq.subject;
				}
				if (! nq.subject.equals(currentSubjectURI)) {
					indexWriter.addToIndex(currentSubjectURI, signature);
					signature = new TreeSet<String>();
					currentSubjectURI = nq.subject;
				}
			 	if (nq.predicate.equals(Constants.RDF_TYPE)) {
			 		signature.add("T-"+nq.object);
			 	}
			 	if (! nq.predicate.equals(Constants.RDF_TYPE)) {
			 		signature.add("P-"+nq.predicate);
			 	}
			}
			indexWriter.addToIndex(currentSubjectURI, signature);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
