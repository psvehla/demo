/**
 *
 */
package au.com.redbarn.swipejobs.demo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * This class represents a job.
 *
 * @author peter
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Job {
	private int jobId;
	private boolean driverLicenseRequired;
	private List<String> requiredCertificates;
	private Location location;
}
