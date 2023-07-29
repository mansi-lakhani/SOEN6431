/**
 *
 */
package io.gojek.parkinglot.model.strategy;

/**
 * @author vaibhav
 *
 */
/**
 * An interface for defining parking strategies.
 * Classes that implement this interface are responsible for managing parking slots and selecting
 * the appropriate slot based on the parking strategy.
 */
public interface ParkingStrategy {

	/**
	 * Adds a slot to the parking strategy.
	 *
	 * @param i The slot number to be added.
	 */
	public void add(int i);

	/**
	 * Gets the selected slot based on the parking strategy.
	 *
	 * @return The selected slot number.
	 */
	public int getSlot();

	/**
	 * Removes the specified slot from the parking strategy.
	 *
	 * @param slot The slot number to be removed.
	 */
	public void removeSlot(int slot);
}

