/**
 *
 */
package au.com.redbarn.swipejobs.demo.controller;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import au.com.redbarn.swipejobs.demo.geo.Geo;
import au.com.redbarn.swipejobs.demo.model.job.Job;
import au.com.redbarn.swipejobs.demo.model.worker.Worker;
import au.com.redbarn.swipejobs.demo.service.worker.WorkerService;
import au.com.redbarn.swipejobs.demo.service.worker.errors.UnsupportedDistanceUnitException;
import lombok.extern.slf4j.Slf4j;

/**
 * Gets jobs for workers.
 *
 * @author peter
 *
 */
@RestController
@RequestMapping("/get-jobs-for-worker")
@Slf4j
public class WorkerJobsController {

	@Autowired
	private WorkerService workerService;

	@Value(value = "${jobs.url}")
	private String jobsUrl;

	@Value(value = "${number.of.jobs.returned}")
	private int numberOfJobsReturned;

	private static RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
	private static RestTemplate restTemplate = restTemplateBuilder.build();

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
			Worker worker = workerService.getWorker(Integer.parseInt(workerId));
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
					.filter(j -> worker.getJobSearchAddress().getMaxJobDistance() >= Geo.geoDistance(Double.parseDouble(worker.getJobSearchAddress().getLatitude()),
							                                                                         Double.parseDouble(worker.getJobSearchAddress().getLongitude()),
							                                                                         Double.parseDouble(j.getLocation().getLatitude()),
							                                                                         Double.parseDouble(j.getLocation().getLongitude())))
					.sorted(jobComparator)
					.limit(numberOfJobsReturned)
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
	 * Gets all the available {@link Job}s.
	 *
	 * @return A {@link List} of all the available {@link Job}s.
	 */
	private List<Job> getJobs() {
		Job[] jobs = restTemplate.getForEntity(jobsUrl, Job[].class).getBody();
		return Arrays.asList(jobs);
	}
}
