package de.westnordost.osmapi;

import java.io.InputStream;

import junit.framework.TestCase;
import de.westnordost.osmapi.ConnectionTestFactory.User;
import de.westnordost.osmapi.common.errors.OsmApiReadResponseException;
import de.westnordost.osmapi.common.errors.OsmAuthorizationException;
import de.westnordost.osmapi.common.errors.OsmConnectionException;

public class OsmConnectionTest extends TestCase
{
	public void testAuthorizationException()
	{
		try
		{
			OsmConnection osm = ConnectionTestFactory.createConnection(null);
			osm.makeAuthenticatedRequest("doesntMatter", "GET");
			fail();
		}
		catch(OsmAuthorizationException e) {}
	}
	
	public void testAuthorizationException2()
	{
		try
		{
			OsmConnection osm = ConnectionTestFactory.createConnection(User.UNKNOWN);
			osm.makeAuthenticatedRequest("changeset/create", "PUT", null, null);
			fail();
		}
		catch(OsmAuthorizationException e) {}
	}
	
	public void testConnectionException()
	{
		try
		{
			OsmConnection osm = new OsmConnection("http://cant.connect.to.this.server.hm", "blub", null);
			osm.makeRequest("doesntMatter", null);
			fail();
		}
		catch(OsmConnectionException e) {}
	}
	
	public void testErrorParsingApiResponse()
	{
		try
		{
			OsmConnection osm = ConnectionTestFactory.createConnection(null);
			osm.makeRequest("capabilities", new ApiResponseReader<Void>()
			{
				@Override
				public Void parse(InputStream in) throws Exception
				{
					throw new Exception();
				}
			});
			fail();
		}
		catch(OsmApiReadResponseException e) {}
	}

}
