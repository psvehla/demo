/**
 *
 */
package au.com.redbarn.swipejobs.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * This class represents a worker.
 *
 * @author peter
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Worker {
	private int userId;

	@JsonProperty(value="isActive")
	private boolean isActive;
}
