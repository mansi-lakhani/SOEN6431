/**
 *
 */
package io.gojek.parkinglot.service;

import java.util.Optional;

import io.gojek.parkinglot.exception.ParkingException;
import io.gojek.parkinglot.model.Vehicle;

/**
 * @author vaibhav
 *
 */

/**
 * A service interface for managing parking operations.
 * This interface provides methods for various parking-related actions, such as creating a parking lot,
 * parking a vehicle, unparking a vehicle, checking the status of the parking lot, and more.
 * All methods throw ParkingException in case of any errors during the execution.
 */
public interface ParkingService extends AbstractService {

	/**
	 * Creates a parking lot with the given level and capacity.
	 *
	 * @param level    the level of the parking lot.
	 * @param capacity the capacity (number of slots) of the parking lot.
	 * @throws ParkingException if an error occurs during the parking lot creation.
	 */
	public void createParkingLot(int level, int capacity) throws ParkingException;

	/**
	 * Parks a vehicle in the parking lot at the given level.
	 *
	 * @param level   the level of the parking lot.
	 * @param vehicle the vehicle to be parked.
	 * @return the allocated slot number for the parked vehicle, wrapped in an Optional.
	 * @throws ParkingException if an error occurs during parking the vehicle.
	 */
	public Optional<Integer> park(int level, Vehicle vehicle) throws ParkingException;

	/**
	 * Unparks (removes) a vehicle from the parking lot at the given slot number.
	 *
	 * @param level     the level of the parking lot.
	 * @param slotNumber the slot number of the vehicle to be unparked.
	 * @throws ParkingException if an error occurs during unparking the vehicle.
	 */
	public void unPark(int level, int slotNumber) throws ParkingException;

	/**
	 * Gets the status of the parking lot at the given level.
	 *
	 * @param level the level of the parking lot.
	 * @throws ParkingException if an error occurs while retrieving the parking lot status.
	 */
	public void getStatus(int level) throws ParkingException;

	/**
	 * Gets the number of available parking slots in the parking lot at the given level.
	 *
	 * @param level the level of the parking lot.
	 * @return the number of available parking slots, wrapped in an Optional.
	 * @throws ParkingException if an error occurs while retrieving the available slots count.
	 */
	public Optional<Integer> getAvailableSlotsCount(int level) throws ParkingException;

	/**
	 * Gets the registration numbers of vehicles parked with the given color in the parking lot at the given level.
	 *
	 * @param level the level of the parking lot.
	 * @param color the color of the vehicles to be searched for.
	 * @throws ParkingException if an error occurs while retrieving the registration numbers.
	 */
	public void getRegNumberForColor(int level, String color) throws ParkingException;

	/**
	 * Gets the slot numbers of vehicles parked with the given color in the parking lot at the given level.
	 *
	 * @param level the level of the parking lot.
	 * @param color the color of the vehicles to be searched for.
	 * @throws ParkingException if an error occurs while retrieving the slot numbers.
	 */
	public void getSlotNumbersFromColor(int level, String color) throws ParkingException;

	/**
	 * Gets the slot number of the vehicle with the given registration number in the parking lot at the given level.
	 *
	 * @param level          the level of the parking lot.
	 * @param registrationNo the registration number of the vehicle to be searched for.
	 * @return the slot number of the vehicle.
	 * @throws ParkingException if an error occurs while retrieving the slot number.
	 */
	public int getSlotNoFromRegistrationNo(int level, String registrationNo) throws ParkingException;

	/**
	 * Cleans up resources and performs necessary cleanup operations.
	 * This method should be called to release any resources used during parking lot operations.
	 */
	public void doCleanup();
}
