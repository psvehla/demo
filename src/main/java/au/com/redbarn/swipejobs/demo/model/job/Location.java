/**
 *
 */
package au.com.redbarn.swipejobs.demo.model.job;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * This class represents a job's location.
 *
 * @author peter
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Location {
	private String longitude;
	private String latitude;
}
