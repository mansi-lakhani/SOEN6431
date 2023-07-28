/**
 *
 */
package io.gojek.parkinglot.service.impl;

import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.StringJoiner;
import java.util.logging.Logger;

import io.gojek.parkinglot.constants.Constants;
import io.gojek.parkinglot.dao.ParkingDataManager;
import io.gojek.parkinglot.dao.impl.MemoryParkingManager;
import io.gojek.parkinglot.exception.ErrorCode;
import io.gojek.parkinglot.exception.ParkingException;
import io.gojek.parkinglot.model.Vehicle;
import io.gojek.parkinglot.model.strategy.NearestFirstParkingStrategy;
import io.gojek.parkinglot.model.strategy.ParkingStrategy;
import io.gojek.parkinglot.service.ParkingService;

/**
 *
 * This class has to be made singleton and used as service to be injected in
 * RequestProcessor
 *
 * @author vaibhav
 *
 */
public class ParkingServiceImpl implements ParkingService
{
	private ParkingDataManager<Vehicle> dataManager = null;

	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private static final Logger LOGGER = Logger.getLogger(ParkingServiceImpl.class.getName());
	@Override
	public void createParkingLot(int level, int capacity) throws ParkingException
	{
		if (dataManager != null) {
			throw new ParkingException(ErrorCode.PARKING_ALREADY_EXIST.getMessage());
		}
		List<Integer> parkingLevels = new ArrayList<>();
		List<Integer> capacityList = new ArrayList<>();
		List<ParkingStrategy> parkingStrategies = new ArrayList<>();
		parkingLevels.add(level);
		capacityList.add(capacity);
		parkingStrategies.add(new NearestFirstParkingStrategy());
		this.dataManager = MemoryParkingManager.getInstance(parkingLevels, capacityList);
		LOGGER.info("Created parking lot with " + capacity + " slots");
	}

	@Override
	public Optional<Integer> park(int level, Vehicle vehicle) throws ParkingException
	{
		Optional<Integer> value = Optional.empty();
		lock.writeLock().lock();
		validateParkingLot();
		try
		{
			value = Optional.of(dataManager.parkCar(level, vehicle));
			if (value.get() == Constants.NOT_AVAILABLE) {
				LOGGER.info("Sorry, parking lot is full");
			}
			else if (value.get() == Constants.VEHICLE_ALREADY_EXIST) {
				LOGGER.info("Sorry, vehicle is already parked.");
			}
			else {
				LOGGER.info("Allocated slot number: " + value.get());
			}
		}
		catch (IllegalArgumentException e)
		{
			throw new ParkingException(ErrorCode.PROCESSING_ERROR.getMessage(), e);
		}
		finally
		{
			lock.writeLock().unlock();
		}
		return value;
	}

	private void validateParkingLot() throws ParkingException
	{
		if (dataManager == null)
		{
			throw new ParkingException(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage());
		}
	}

	@Override
	public void unPark(int level, int slotNumber) throws ParkingException
	{
		lock.writeLock().lock();
		validateParkingLot();
		try
		{

			if (dataManager.leaveCar(level, slotNumber)) {
				LOGGER.info("Slot number " + slotNumber + " is free");
			}
			else {
				LOGGER.info("Slot number is Empty Already.");
			}
		}
		catch (IllegalFormatException e)
		{
			throw new ParkingException(ErrorCode.INVALID_VALUE.getMessage().replace("{variable}", "slot_number"), e);
		}
		finally
		{
			lock.writeLock().unlock();
		}
	}

	@Override
	public void getStatus(int level) throws ParkingException
	{
		lock.readLock().lock();
		validateParkingLot();
		try
		{
			LOGGER.info("Slot No.\tRegistration No.\tColor");
			List<String> statusList = dataManager.getStatus(level);
			if (statusList.size() == 0) {
				LOGGER.info("Sorry, parking lot is empty.");
			}
			else
			{
				for (String statusSting : statusList)
				{
					LOGGER.info(statusSting);
				}
			}
		}
		catch (IndexOutOfBoundsException e)
		{
			throw new ParkingException(ErrorCode.PROCESSING_ERROR.getMessage(), e);
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
	/**
	 * Get the count of available parking slots at the specified level.
	 *
	 * @param level The level for which to get the available slots count.
	 * @return An Integer Optional containing the count of available parking slots, or empty if the level is invalid.
	 * @throws ParkingException If an error occurs while getting the available slots count.
	 */
	public Optional<Integer> getAvailableSlotsCount(int level) throws ParkingException
	{
		lock.readLock().lock();
		Optional<Integer> value = Optional.empty();
		validateParkingLot();
		try
		{
			value = Optional.of(dataManager.getAvailableSlotsCount(level));
		}
		catch (IllegalFormatException e)
		{
			throw new ParkingException(ErrorCode.PROCESSING_ERROR.getMessage(), e);
		}
		finally
		{
			lock.readLock().unlock();
		}
		return value;
	}

	@Override
	public void getRegNumberForColor(int level, String color) throws ParkingException
	{
		lock.readLock().lock();
		validateParkingLot();
		try
		{
			List<String> registrationList = dataManager.getRegNumberForColor(level, color);
			if (registrationList.size() == 0) {
				LOGGER.info("Not Found");
			}
			else {
				LOGGER.info(String.join(",", registrationList));
			}
		}
		catch (IndexOutOfBoundsException e)
		{
			throw new ParkingException(ErrorCode.PROCESSING_ERROR.getMessage(), e);
		}
		finally
		{
			lock.readLock().unlock();
		}
	}

	@Override
	public void getSlotNumbersFromColor(int level, String color) throws ParkingException
	{
		lock.readLock().lock();
		validateParkingLot();
		try
		{
			List<Integer> slotList = dataManager.getSlotNumbersFromColor(level, color);
			if (slotList.size() == 0) {
				LOGGER.info("Not Found");
			}
			StringJoiner joiner = new StringJoiner(",");
			for (Integer slot : slotList)
			{
				joiner.add(slot + "");
			}
			LOGGER.info(joiner.toString());
		}
		catch (IllegalFormatException e)
		{
			throw new ParkingException(ErrorCode.PROCESSING_ERROR.getMessage(), e);
		}
		finally
		{
			lock.readLock().unlock();
		}
	}

	@Override
	public int getSlotNoFromRegistrationNo(int level, String registrationNo) throws ParkingException
	{
		int value = -1;
		lock.readLock().lock();
		validateParkingLot();
		try
		{
			value = dataManager.getSlotNoFromRegistrationNo(level, registrationNo);
			if (value != -1) {
				LOGGER.info(String.valueOf(value));
			} else {
				LOGGER.info("Not Found");
			}

		}
		catch (IllegalFormatException e)
		{
			throw new ParkingException(ErrorCode.PROCESSING_ERROR.getMessage(), e);
		}
		finally
		{
			lock.readLock().unlock();
		}
		return value;
	}

	@Override
	public void doCleanup()
	{
		if (dataManager != null) {
			dataManager.doCleanup();
		}
	}
}
