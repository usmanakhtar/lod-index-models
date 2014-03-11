package index.models;

import index.IndexWriter;
import io.NQuad;
import io.NQuadParser;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.TreeSet;

import tools.Constants;

public class TypeSetIndexing {

	public void run(File subjectSortedInput, File indexOutput) {
		try {
			IndexWriter indexWriter = new IndexWriter(indexOutput);
			NQuadParser parserIn = new NQuadParser(subjectSortedInput);
			String currentSubjectURI = null;
			TreeSet<String> classes = new TreeSet<String>();
			PrintStream out = new PrintStream(indexOutput);
			while (parserIn.hasNext()) {
				NQuad nq = parserIn.next();
				if (currentSubjectURI == null) {
					currentSubjectURI = nq.subject;
				}
				if (! nq.subject.equals(currentSubjectURI)) {
					indexWriter.addToIndex(currentSubjectURI, classes);
					classes = new TreeSet<String>();
					currentSubjectURI = nq.subject;
				}
			 	if (nq.predicate.equals(Constants.RDF_TYPE)) {
			 		classes.add(nq.object);
			 	}
			}
			indexWriter.addToIndex(currentSubjectURI, classes);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

}
