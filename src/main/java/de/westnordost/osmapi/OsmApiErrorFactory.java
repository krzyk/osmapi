package de.westnordost.osmapi;

import java.net.HttpURLConnection;

import de.westnordost.osmapi.errors.OsmApiException;
import de.westnordost.osmapi.errors.OsmAuthorizationException;
import de.westnordost.osmapi.errors.OsmBadUserInputException;
import de.westnordost.osmapi.errors.OsmConflictException;
import de.westnordost.osmapi.errors.OsmConnectionException;
import de.westnordost.osmapi.errors.OsmNotFoundException;
import de.westnordost.osmapi.errors.OsmServiceUnavailableException;

/** Static factory that creates the OsmApiExceptions from the HTTP response code and message */
public class OsmApiErrorFactory
{
	public static RuntimeException createError(int error, String response, String description)
	{
		switch (error)
		{
			// 5xx error codes...
			case HttpURLConnection.HTTP_UNAVAILABLE:
				return new OsmServiceUnavailableException(error, response, description);

			// 4xx error codes...
			//case HttpURLConnection.HTTP_UNAUTHORIZED:
				// "need to be logged in to make this request..."
				// this should not be thrown if the implementation of the api is correct...
			case HttpURLConnection.HTTP_NOT_FOUND:
			case HttpURLConnection.HTTP_GONE:
				return new OsmNotFoundException(error, response, description);
			case HttpURLConnection.HTTP_FORBIDDEN:
				return new OsmAuthorizationException(error, response, description);
			case HttpURLConnection.HTTP_CONFLICT:
				return new OsmConflictException(error, response, description);
			case HttpURLConnection.HTTP_BAD_REQUEST:
				return new OsmBadUserInputException(error, response, description);

			default:
				return createGenericError(error, response, description);
		}
	}

	private static RuntimeException createGenericError(int error, String response, String description)
	{
		if(error >= 400 && error < 500 )
		{
			// "The 4xx class of status code is intended for situations in which the client seems to have erred"
			return new OsmApiException(error, response, description);
		}
		// "The server failed to fulfill an apparently valid request"
		return new OsmConnectionException(error, response, description);
	}
}
