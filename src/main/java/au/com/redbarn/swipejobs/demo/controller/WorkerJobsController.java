/**
 *
 */
package au.com.redbarn.swipejobs.demo.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import au.com.redbarn.swipejobs.demo.model.Job;
import au.com.redbarn.swipejobs.demo.model.Worker;

/**
 * Gets jobs for workers.
 *
 * @author peter
 *
 */
@RestController
@RequestMapping("/get-jobs-for-worker")
public class WorkerJobsController {

	private static final Logger log = LoggerFactory.getLogger(WorkerJobsController.class);
	
	private static final long NUMBER_OF_JOBS_RETURNED = 3;

	private static String workersUrl;
	private static String jobsUrl;

	private static RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
	private static RestTemplate restTemplate = restTemplateBuilder.build();

	static {
		try {
			var rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			var applicationPropertiesPath = rootPath + "application.properties";
			var applicationProperties = new Properties();
			applicationProperties.load(new FileInputStream(applicationPropertiesPath));

			workersUrl = applicationProperties.getProperty("workersUrl");
			jobsUrl = applicationProperties.getProperty("jobsUrl");
		} catch (IOException e) {
			log.error("Application configuration error, could not find application properties.", e);
		}
	}

	/**
	 * Gets up to three appropriate jobs for a given worker.
	 *
	 * @param workerId The worker to find appropriate jobs for.
	 * @return Up to three appropriate jobs for the given worker.
	 */
	@GetMapping("/{workerId}")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody List<Job> getJobsForWorker(@PathVariable("workerId") String workerId) {
		try {
			var worker = getWorker(Integer.parseInt(workerId));
			var jobs = getJobs();


			Comparator<Job> jobComparator = (j1, j2) -> {

				double comparison = 0;

				try {
					comparison = (Double) NumberFormat.getCurrencyInstance().parse(j2.getBillRate()) - (Double) NumberFormat.getCurrencyInstance().parse(j1.getBillRate());
				} catch (ParseException e) {
					log.error("One of jobs " + j1.getJobId() + " or " + j2.getJobId() + " has an invalid bill rate.", e);
				}

				return (int) comparison;
			};

			var relevantJobs = jobs
					.stream()
					.filter(j -> worker.isHasDriversLicense() || !j.isDriverLicenseRequired())
					.filter(j -> {

						var hasCertificates = true;

						for (String requiredCert : j.getRequiredCertificates()) {
							if (!worker.getCertificates().contains(requiredCert)) {
								hasCertificates = false;
							}
						}

						return hasCertificates;
					})
					.filter(j -> worker.getJobSearchAddress().getMaxJobDistance() >= geoDistance(Double.parseDouble(worker.getJobSearchAddress().getLatitude()),
							                                                                     Double.parseDouble(worker.getJobSearchAddress().getLongitude()),
							                                                                     Double.parseDouble(j.getLocation().getLatitude()),
							                                                                     Double.parseDouble(j.getLocation().getLongitude())))
					.sorted(jobComparator)
					.limit(NUMBER_OF_JOBS_RETURNED)
					.collect(Collectors.toList());

			return relevantJobs;
		}
		catch (NoSuchElementException e) {
			log.error("Worker " + workerId + " not found.", e);
			return new ArrayList<>();
		}
		catch (UnsupportedDistanceUnitException e) {
			log.error("Worker " + workerId + " has an unsupported record format.", e);
			return new ArrayList<>();
		}
	}

	/**
	 * Gets a {@link Worker} for a given id.
	 *
	 * @param workerId The id of the {@link Worker} to retrieve.
	 * @return The desired {@link Worker}.
	 * @throws UnsupportedDistanceUnitException When the maximum distance to work isn't in km.
	 */
	private Worker getWorker(int workerId) throws UnsupportedDistanceUnitException {

		Worker[] workers = restTemplate.getForEntity(workersUrl, Worker[].class).getBody();
		Worker worker = Arrays.asList(workers).stream().filter(x -> workerId == x.getUserId()).filter(Worker::isActive).findAny().orElseThrow();

		if (!"km".contentEquals(worker.getJobSearchAddress().getUnit())) {
			throw new UnsupportedDistanceUnitException();
		}

		return worker;
	}

	/**
	 * Gets all the available {@link Job}s.
	 *
	 * @return A {@link List} of all the available {@link Job}s.
	 */
	private List<Job> getJobs() {
		Job[] jobs = restTemplate.getForEntity(jobsUrl, Job[].class).getBody();
		return Arrays.asList(jobs);
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
	private int geoDistance(double workerLat, double workerLong, double jobLat, double jobLong) {

		final int AVERAGE_EARTH_RADIUS = 6371;

	    double latDistance = Math.toRadians(workerLat - jobLat);
	    double longDistance = Math.toRadians(workerLong - jobLong);

	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(workerLat)) * Math.cos(Math.toRadians(jobLat)) * Math.sin(longDistance / 2) * Math.sin(longDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

	    return (int) (Math.round(AVERAGE_EARTH_RADIUS * c));
	}

	private class UnsupportedDistanceUnitException extends Exception {
		private static final long serialVersionUID = -1057323490267419985L;
	}
}
