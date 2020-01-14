/**
 * 
 */
package au.com.redbarn.swipejobs.demo.geo;

/**
 * This class is responsible for geographic calculations.
 * 
 * @author peter
 *
 */
public class Geo {
	
	private Geo() {
		throw new IllegalStateException("Geo is a utility class not meant for instantiation.");
	}

	/**
	 * Calculates the distance (in km) between two geocodes.
	 * Uses the haversine formula.
	 *
	 * @param workerLat The worker's latitude.
	 * @param workerLong The worker's longitude.
	 * @param jobLat The job's latitude.
	 * @param jobLong The job's longitude.
	 *
	 * @return The distance (in km) between the two points.
	 */
	public static int geoDistance(double workerLat, double workerLong, double jobLat, double jobLong) {

		final int AVERAGE_EARTH_RADIUS = 6371;

	    double latDistance = Math.toRadians(workerLat - jobLat);
	    double longDistance = Math.toRadians(workerLong - jobLong);

	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(workerLat)) * Math.cos(Math.toRadians(jobLat)) * Math.sin(longDistance / 2) * Math.sin(longDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

	    return (int) (Math.round(AVERAGE_EARTH_RADIUS * c));
	}

}
