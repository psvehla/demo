/**
 *
 */
package au.com.redbarn.swipejobs.demo.service.worker.errors;

/**
 * An exception to throw if the app does not recognise the distance unit in the data returned from the upstream web services. Only km are supported.
 *
 * @author peter
 *
 */
public class UnsupportedDistanceUnitException extends Exception {
	private static final long serialVersionUID = -1057323490267419985L;
}
