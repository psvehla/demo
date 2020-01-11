/**
 *
 */
package au.com.redbarn.swipejobs.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class represents a worker.
 *
 * @author peter
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Worker {
	private int userId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + userId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Worker other = (Worker) obj;
		if (userId != other.userId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Worker [userId=" + userId + "]";
	}
}
