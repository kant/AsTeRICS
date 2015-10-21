package eu.asterics.mw.services;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestResourceRegistry {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetResource() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetResourceInputStream() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetAREBaseURI() {
		URI newURI;
		try {
			newURI = new URI("C:\\Program Files (x86)\\AsTeRICS\\ARE");
			ResourceRegistry.setAREBaseURI(newURI);
			testGetAREBaseURI();
			testToRelative();
			testToAbsolute();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetAREBaseURI() {
		System.out.println("baseURI: "+ResourceRegistry.getAREBaseURI());		
	}

	@Test
	public void testToRelative() {
		URI absolute=ResourceRegistry.getAREBaseURI().resolve(ResourceRegistry.MODELS_FOLDER);
		System.out.println("absolute: "+absolute+" relative: "+ResourceRegistry.toRelative(absolute.toString()));
	}

	@Test
	public void testToAbsolute() {
		System.out.println("relative: "+ResourceRegistry.MODELS_FOLDER+" absolute: "+ResourceRegistry.toAbsolute(ResourceRegistry.MODELS_FOLDER));
	}

}
