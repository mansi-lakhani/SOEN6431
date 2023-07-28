/**
 *
 */
package io.gojek.parkinglot.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.gojek.parkinglot.dao.ParkingDataManager;
import io.gojek.parkinglot.dao.ParkingLevelDataManager;
import io.gojek.parkinglot.model.Vehicle;
import io.gojek.parkinglot.model.strategy.NearestFirstParkingStrategy;

/**
 * This class is a singleton responsible for managing the data of the parking system.
 * It provides methods to handle parking lot information, vehicle data, and other related details.
 *
 * Author: Vaibhav
 */
public class MemoryParkingManager<T extends Vehicle> implements ParkingDataManager<T>
{
	private Map<Integer, ParkingLevelDataManager<T>> levelParkingMap;

	@SuppressWarnings("rawtypes")
	private static MemoryParkingManager instance = null;

	@SuppressWarnings("unchecked")
	/**
	 * Get the singleton instance of the MemoryParkingManager with the specified parking levels and capacities.
	 *
	 * @param parkingLevels A list of parking levels to be managed by the MemoryParkingManager.
	 * @param capacityList A list of capacities corresponding to each parking level.
	 * @param <T> The type of vehicles that can be parked and managed by the MemoryParkingManager.
	 * @return The singleton instance of the MemoryParkingManager.
	 */
	public static <T extends Vehicle> MemoryParkingManager<T> getInstance(List<Integer> parkingLevels,
																		  List<Integer> capacityList)
	{
		// Make sure the each of the lists are of equal size
		if (instance == null)
		{
			synchronized (MemoryParkingManager.class)
			{
				if (instance == null)
				{
					instance = new MemoryParkingManager<T>(parkingLevels, capacityList);
				}
			}
		}
		return instance;
	}

	private MemoryParkingManager(List<Integer> parkingLevels, List<Integer> capacityList)
	{
		if (levelParkingMap == null) {
			levelParkingMap = new HashMap<>();
		}
		for (int i = 0; i < parkingLevels.size(); i++)
		{
			levelParkingMap.put(parkingLevels.get(i), MemoryParkingLevelManager.getInstance(parkingLevels.get(i),
					capacityList.get(i), new NearestFirstParkingStrategy()));

		}
	}

	@Override
	public int parkCar(int level, T vehicle)
	{
		return levelParkingMap.get(level).parkCar(vehicle);
	}

	@Override
	public boolean leaveCar(int level, int slotNumber)
	{
		return levelParkingMap.get(level).leaveCar(slotNumber);
	}

	@Override
	public List<String> getStatus(int level)
	{
		return levelParkingMap.get(level).getStatus();
	}
	/**
	 * Get the count of available parking slots at the specified level.
	 *
	 * @param level The level of the parking lot for which the available slots count should be retrieved.
	 * @return The count of available parking slots at the given level.
	 */
	public int getAvailableSlotsCount(int level)
	{
		return levelParkingMap.get(level).getAvailableSlotsCount();
	}

	@Override
	public List<String> getRegNumberForColor(int level, String color)
	{
		return levelParkingMap.get(level).getRegNumberForColor(color);
	}

	@Override
	public List<Integer> getSlotNumbersFromColor(int level, String color)
	{
		return levelParkingMap.get(level).getSlotNumbersFromColor(color);
	}

	@Override
	public int getSlotNoFromRegistrationNo(int level, String registrationNo)
	{
		return levelParkingMap.get(level).getSlotNoFromRegistrationNo(registrationNo);
	}
	/**
	 * Throws a CloneNotSupportedException to indicate that cloning is not supported for this class.
	 *
	 * @return This method never returns and always throws a CloneNotSupportedException.
	 * @throws CloneNotSupportedException Always throws CloneNotSupportedException.
	 */
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
	/**
	 * This method cleans up resources and prepares for shutdown.
	 * It invokes the 'doCleanUp' method for each level's parking data manager and resets internal state.
	 */
	public void doCleanup()
	{
		for (ParkingLevelDataManager<T> levelDataManager : levelParkingMap.values())
		{
			levelDataManager.doCleanUp();
		}
		levelParkingMap = null;
		instance = null;
	}
}
