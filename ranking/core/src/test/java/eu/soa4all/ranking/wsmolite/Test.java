package eu.soa4all.ranking.wsmolite;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import junit.framework.TestCase;

import org.ontoware.rdf2go.model.Syntax;

import eu.soa4all.validation.RPC.MSMService;
import eu.soa4all.validation.RPC.Service;
import eu.soa4all.validation.ServiceTemplate.ServiceTemplate;

public class Test extends TestCase {
	
    Service muller, racer, runner, walker, weasel;
	ServiceTemplate template;
	
	@Override
	protected void setUp() throws Exception {
			  muller = new MSMService();
			  racer = new MSMService();
			  runner = new MSMService(); 
			  walker = new MSMService(); 
			  weasel = new MSMService();
			  template = new ServiceTemplate();
	}
	 
	public void testReader() {	
		try {
			
			ClassLoader l = this.getClass().getClassLoader();
			
			muller.readFrom(new FileInputStream(l.getResource("WSMullerFixed_new.rdf.n3").getFile()), Syntax.Ntriples);
//			muller.readFrom(new FileInputStream(l.getResource("WSMullerFixed_new.rdf.n3").getFile()), Syntax.Ntriples);//TODO: check why failure
			racer.readFrom(new FileInputStream(l.getResource("WSRacerFixed_new.rdf.n3").getFile()), Syntax.Ntriples);
			runner.readFrom(new FileInputStream(l.getResource("WSRunnerFixed_new.rdf.n3").getFile()), Syntax.Ntriples);
			walker.readFrom(new FileInputStream(l.getResource("WSWalkerFixed_new.rdf.n3").getFile()), Syntax.Ntriples);
			weasel.readFrom(new FileInputStream(l.getResource("WSWeaselFixed_new.rdf.n3").getFile()), Syntax.Ntriples);
//			
			template.readFrom(new FileInputStream(l.getResource("stShipping.rdf.n3").getFile()), Syntax.Ntriples);			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
