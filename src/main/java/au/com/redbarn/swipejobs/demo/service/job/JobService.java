/**
 *
 */
package au.com.redbarn.swipejobs.demo.service.job;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import au.com.redbarn.swipejobs.demo.model.job.Job;

/**
 * Gets jobs from upstream web services.
 *
 * @author peter
 *
 */
@Service
public class JobService {

	private static RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
	private static RestTemplate restTemplate = restTemplateBuilder.build();

	@Value(value = "${jobs.url}")
	private String jobsUrl;

	/**
	 * Gets all the available {@link Job}s.
	 *
	 * @return A {@link List} of all the available {@link Job}s.
	 */
	public List<Job> getJobs() {
		Job[] jobs = restTemplate.getForEntity(jobsUrl, Job[].class).getBody();
		return Arrays.asList(jobs);
	}
}
