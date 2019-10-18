/*******************************************************************************
 *        __                  _     _ _   
 *     /\ \ \__ _ _ __   ___ | |__ (_) |_ 
 *    /  \/ / _` | '_ \ / _ \| '_ \| | __|
 *   / /\  / (_| | | | | (_) | |_) | | |_ 
 *   \_\ \/ \__,_|_| |_|\___/|_.__/|_|\__|
 * 
 * *****************************************************************************
 * 
 *  Author: Willi Menapace <willi.menapace@gmail.com>
 * 
 ******************************************************************************/

package it.webapp.spatial;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import java.io.IOException;


/**
 * Geocoding helper class
 */
public class GeocodingManager {

	private static String apiKey = null;
	
	public static void init(String apiKeyParam) {
		if(apiKey != null) {
			throw new IllegalStateException("Geocoder is already initialized");
		}
		
		apiKey = apiKeyParam;
	}
	
	/**
	 * Gets the geographical coordinates of a certain address
	 * 
	 * @param street The street name
	 * @param streetNumber The street number
	 * @param zip The zip code
	 * @param city The city name
	 * @param district The district name
	 * @param country The country name
	 * @return latitude and longitude of the requested address, null if not found
	 */
	public static LatLng getLatLngFromAddress(String street, String streetNumber, String zip, String city, String district, String country) {
		if(apiKey == null) {
			throw new IllegalStateException("Geocoder must be initialized before use");
		}
		
		LatLng result = null;
		
		try {
			//Formats the address
			StringBuilder addressBuilder = new StringBuilder();
			addressBuilder.append(street);
			addressBuilder.append(" ");
			addressBuilder.append(streetNumber);
			addressBuilder.append(", ");
			addressBuilder.append(zip);
			addressBuilder.append(" ");
			addressBuilder.append(city);
			addressBuilder.append(" ");
			addressBuilder.append(district);
			addressBuilder.append(", ");
			addressBuilder.append(country);
			String address = addressBuilder.toString();
			
			//Tries geocoding
			GeoApiContext context = new GeoApiContext().setApiKey(apiKey);
			GeocodingResult[] results =  GeocodingApi.geocode(context, address).await();

			//In case of success sets the coordinates
			if(results.length > 0) {
				result = results[0].geometry.location;
			}
		} catch(ApiException | IOException | InterruptedException e) {
			result = null;
		}
		return result;
	}
	
}
