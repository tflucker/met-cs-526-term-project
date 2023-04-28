package tflucker.project;

public class Process {

	private Integer id;

	private int priority;

	private int arrivalTime;

	private int duration;

	public Process() {
	}

	public Process(int processId, int priority, int duration, int arrivalTime) {
		super();
		this.id = processId;
		this.priority = priority;
		this.arrivalTime = arrivalTime;
		this.duration = duration;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return "Process [id=" + id + ", priority=" + priority + ", arrivalTime=" + arrivalTime + ", duration="
				+ duration + "]";
	}

}
