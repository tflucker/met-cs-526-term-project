package tflucker.project;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import tflucker.project.Process;

public class ProcessScheduling {

	public static void main(String[] args) throws IOException {

		// define location of input file
		String inputFile = "C:\\Users\\Tim Flucker\\OneDrive\\Graduate School Documents\\Courses\\MET CS 526 - Data Structures and Algorithms\\Term Project\\term-project-input.txt";

		// used when instantiating the PriorityQueue, allows for multiple Process
		// objects to be added
		Comparator<Process> processComparator = new Comparator<Process>() {
			@Override
			public int compare(Process o1, Process o2) {
				return o1.getId() - o2.getId();
			}
		};

		List<Process> processList = new ArrayList<>();
		int maxWaitTime = 30;

		// read contents of input file line-by-line, create Process object and add to
		// List
		for (String line : Files.readAllLines(new File(inputFile).toPath())) {
			// split each line into its component data for easier access
			int[] info = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();
			// create process object from line data
			Process p = new Process(info[0], info[1], info[2], info[3]);

			System.out.println(p);

			processList.add(p);

		}

		System.out.println(System.lineSeparator() + "Maximum wait time = " + maxWaitTime + System.lineSeparator());

		int currentTime = 0;
		int processWaitTime = 0;
		int totalWaitTime = 0;
		List<Integer> waitTimeList = new ArrayList<>();

		boolean isRunning = false;
		Process runningProcess = null;
		Process nextProcess = null;
		PriorityQueue<Process> pQueue = new PriorityQueue<Process>(processComparator);

		while (!processList.isEmpty()) {
			currentTime++;

			nextProcess = processList.stream()
					.min((p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime())).get();
			if (nextProcess.getArrivalTime() <= currentTime) {
				pQueue.add(nextProcess);
				// remove process processList and add to priorityQueue
				processList.remove(nextProcess);

			}

			// if no process is currently running and the priority queue has elements
			if (!isRunning && pQueue.size() >= 1) {

				runningProcess = initiateProcess(pQueue, currentTime, totalWaitTime);
				processWaitTime = currentTime - runningProcess.getArrivalTime();
				waitTimeList.add(processWaitTime);
				totalWaitTime += processWaitTime;

				// set flag to make sure not other processes run while this process is running
				isRunning = true;

			}

			if (isRunning) {

				int processFinishedTime = runningProcess.getDuration() + runningProcess.getArrivalTime()
						+ processWaitTime;

				if (currentTime == processFinishedTime) {
					System.out.println(MessageFormat.format("Process {0} finished at time {1}", runningProcess.getId(),
							currentTime) + System.lineSeparator());
					isRunning = false;

					if (!pQueue.isEmpty()) {
						System.out.println("Update Priority: ");

						for (Process p : pQueue) {

							if ((currentTime - p.getArrivalTime()) > maxWaitTime) {
								int pwt = currentTime - p.getArrivalTime();
								System.out.println(
										MessageFormat.format("PID = {0}, waitTime = {1}, current priority = {2}",
												p.getId(), pwt, p.getPriority()));
								p.setPriority(p.getPriority() - 1);
								System.out.println(MessageFormat.format("PID = {0}, new priority = {1}", p.getId(),
										p.getPriority()));
							}
						}
						runningProcess = initiateProcess(pQueue, currentTime, totalWaitTime);
						processWaitTime = currentTime - runningProcess.getArrivalTime();
						waitTimeList.add(processWaitTime);
						totalWaitTime += processWaitTime;
						isRunning = true;
					}

				}

			}

		}

		System.out.println(System.lineSeparator() + "D becomes empty at time " + currentTime + System.lineSeparator());

		// runs remaining processes in pQueue after the processList has been emptied
		while (!pQueue.isEmpty() || isRunning) {
			currentTime++;

			// if no process is currently running and the priority queue has elements
			if (!isRunning && pQueue.size() >= 1) {

				runningProcess = initiateProcess(pQueue, currentTime, totalWaitTime);
				processWaitTime = currentTime - runningProcess.getArrivalTime();
				waitTimeList.add(processWaitTime);
				totalWaitTime += processWaitTime;

				// set flag to make sure not other processes run while this process is running
				isRunning = true;

			}

			if (isRunning) {

				int processFinishedTime = runningProcess.getDuration() + runningProcess.getArrivalTime()
						+ processWaitTime;

				if (currentTime == processFinishedTime) {
					System.out.println(MessageFormat.format("Process {0} finished at time {1}", runningProcess.getId(),
							currentTime) + System.lineSeparator());
					isRunning = false;

					if (!pQueue.isEmpty()) {
						System.out.println("Update Priority: ");

						for (Process p : pQueue) {

							if ((currentTime - p.getArrivalTime()) > maxWaitTime) {
								int pwt = currentTime - p.getArrivalTime();
								System.out.println(
										MessageFormat.format("PID = {0}, waitTime = {1}, current priority = {2}",
												p.getId(), pwt, p.getPriority()));
								p.setPriority(p.getPriority() - 1);
								System.out.println(MessageFormat.format("PID = {0}, new priority = {1}", p.getId(),
										p.getPriority()));
							}
						}
						runningProcess = initiateProcess(pQueue, currentTime, totalWaitTime);
						processWaitTime = currentTime - runningProcess.getArrivalTime();
						waitTimeList.add(processWaitTime);
						totalWaitTime += processWaitTime;
						isRunning = true;
					}

				}

			}
		}

		System.out.println("current time at end: " + currentTime);
		// after logic is done, check if pQueue is empty, if true then change loop
		// condition to exit loop, else increment currentTime
		System.out.println("Total wait time = " + totalWaitTime);
		System.out.println(
				"Average wait time = " + waitTimeList.stream().mapToDouble(time -> time).average().orElse(0.0));

	}
  
	public static Process initiateProcess(PriorityQueue<Process> pQueue, int currentTime, int totalWaitTime) {
		// get process with least priority
		Process runningProcess = pQueue.stream().min((p1, p2) -> Integer.compare(p1.getPriority(), p2.getPriority()))
				.get();

		int processRunningTime = (currentTime - runningProcess.getArrivalTime());
		// print metrics about the process that is now running
		System.out.println(System.lineSeparator() + MessageFormat.format(
				"Process removed from queue is: id = {0}, at time {1}, wait time = {2}, Total waitTime = {3}",
				runningProcess.getId(), currentTime, processRunningTime, (totalWaitTime + processRunningTime)));

		// print the running Process object
		System.out.println(runningProcess);

		// remove from queue
		pQueue.remove(runningProcess);

		return runningProcess;
	}
}
