/**
 *
 */
package au.com.redbarn.swipejobs.demo.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

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
	
	static {
		try {
			var rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			var applicationPropertiesPath = rootPath + "application.properties";
			var applicationProperties = new Properties();
			applicationProperties.load(new FileInputStream(applicationPropertiesPath));
			workersUrl = applicationProperties.getProperty("workersUrl");
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
		var worker = getWorker(Integer.parseInt(workerId));
		return Integer.toString(worker.getUserId());
	}

	private Worker getWorker(int workerId) {
		var restTemplateBuilder = new RestTemplateBuilder();
		var restTemplate = restTemplateBuilder.build();
		ResponseEntity<Worker[]> response = restTemplate.getForEntity(workersUrl, Worker[].class);
		Worker[] workers = response.getBody();

		return Arrays.asList(workers).stream().filter(x -> workerId == x.getUserId()).findAny().orElseThrow();
	}
}
