package index.models;

import index.IndexWriter;
import io.NQuad;
import io.NQuadParser;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.TreeSet;

import tools.Constants;

public class IncomingPropertySetIndexing {

	public void run(File objectSortedInput, File indexOutput) {
		try {
			IndexWriter indexWriter = new IndexWriter(indexOutput);
			NQuadParser parserIn = new NQuadParser(objectSortedInput);
			String currentObjectURI = null;
			TreeSet<String> properties = new TreeSet<String>();
			PrintStream out = new PrintStream(indexOutput);
			while (parserIn.hasNext()) {
				NQuad nq = parserIn.next();
				if (currentObjectURI == null) {
					currentObjectURI = nq.object;
				}
				if (! nq.subject.equals(currentObjectURI)) {
					indexWriter.addToIndex(currentObjectURI, properties);
					properties = new TreeSet<String>();
					currentObjectURI = nq.object;
				}
			 	if (! nq.predicate.equals(Constants.RDF_TYPE)) {
			 		properties.add(nq.predicate);
			 	}
			}
			indexWriter.addToIndex(currentObjectURI, properties);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
