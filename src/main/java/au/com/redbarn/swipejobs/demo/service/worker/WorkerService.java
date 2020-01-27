/**
 *
 */
package au.com.redbarn.swipejobs.demo.service.worker;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import au.com.redbarn.swipejobs.demo.model.worker.Worker;
import au.com.redbarn.swipejobs.demo.service.worker.errors.UnsupportedDistanceUnitException;

/**
 * @author peter
 *
 */
@Service
public class WorkerService {

	private static RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
	private static RestTemplate restTemplate = restTemplateBuilder.build();

	@Value(value = "${workers.url}")
	private String workersUrl;

	/**
	 * Gets a {@link Worker} for a given id.
	 *
	 * @param workerId The id of the {@link Worker} to retrieve.
	 * @return The desired {@link Worker}.
	 * @throws UnsupportedDistanceUnitException When the maximum distance to work isn't in km.
	 */
	public Worker getWorker(int workerId) throws UnsupportedDistanceUnitException {

		Worker[] workers = restTemplate.getForEntity(workersUrl, Worker[].class).getBody();
		Worker worker = Arrays.asList(workers).stream().filter(x -> workerId == x.getUserId()).filter(Worker::isActive).findAny().orElseThrow();

		if (!"km".contentEquals(worker.getJobSearchAddress().getUnit())) {
			throw new UnsupportedDistanceUnitException();
		}

		return worker;
	}
}
