/**
 *
 */
package au.com.redbarn.swipejobs.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * This class represents a worker's job search address.
 *
 * @author peter
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class JobSearchAddress {
	private String unit;
	private int maxJobDistance;
	private String longitude;
	private String latitude;
}
