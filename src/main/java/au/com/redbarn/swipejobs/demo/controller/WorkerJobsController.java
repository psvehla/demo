/**
 *
 */
package au.com.redbarn.swipejobs.demo.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

import au.com.redbarn.swipejobs.demo.model.Job;
import au.com.redbarn.swipejobs.demo.model.Worker;

/**
 * Gets jobs for workers.
 *
 * @author peter
 *
 */
@Controller
public class WorkerJobsController {

	private static final Logger log = LoggerFactory.getLogger(WorkerJobsController.class);

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
	@GetMapping("/get-jobs-for-worker/{workerId}")
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody String getJobsForWorker(@PathVariable("workerId") String workerId) {
		try {
			var worker = getWorker(Integer.parseInt(workerId));
			var jobs = getJobs();
			var relevantJobs = jobs.stream().collect(Collectors.toList());
			return Integer.toString(worker.getUserId());
		}
		catch (NoSuchElementException e) {
			log.error("Worker " + workerId + " not found.", e);
			return "Worker " + workerId + " not found.";
		}
	}

	/**
	 * Gets a {@link Worker} for a given id.
	 * 
	 * @param workerId The id of the {@link Worker} to retrieve.
	 * @return The desired {@link Worker}.
	 */
	private Worker getWorker(int workerId) {
		Worker[] workers = restTemplate.getForEntity(workersUrl, Worker[].class).getBody();
		return Arrays.asList(workers).stream().filter(x -> workerId == x.getUserId()).filter(Worker::isActive).findAny().orElseThrow();
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
