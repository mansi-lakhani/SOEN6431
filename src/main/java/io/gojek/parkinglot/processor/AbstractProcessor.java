/**
 *
 */
package io.gojek.parkinglot.processor;

import io.gojek.parkinglot.constants.CommandInputMap;
import io.gojek.parkinglot.exception.ParkingException;
import io.gojek.parkinglot.service.AbstractService;


/**
 * @author vaibhav
 *
 */

/**
 * An interface for defining the behavior of a processor that handles parking-related actions.
 * Implementations of this interface should provide methods to set the service, execute actions,
 * and validate input strings related to parking operations.
 */
public interface AbstractProcessor {

	/**
	 * Sets the service to be used by the processor for executing parking actions.
	 *
	 * @param service the AbstractService to be set.
	 */
	public void setService(AbstractService service);

	/**
	 * Executes the specified parking action based on the input string.
	 *
	 * @param action the action to be executed.
	 * @throws ParkingException if an error occurs during the execution of the action.
	 */
	public void execute(String action) throws ParkingException;

	/**
	 * Validates the input string related to parking actions.
	 *
	 * @param inputString the input string to be validated.
	 * @return true if the input string is valid for parking actions, false otherwise.
	 */
	public default boolean validate(String inputString) {
		// Split the input string to validate command and input value
		String[] inputs = inputString.split(" ");
		int params = CommandInputMap.getCommandsParameterMap().get(inputs[0]);
		int inputLength = inputs.length;

		return isValidCommandParams(params, inputLength);
	}

	/**
	 * Validates the input string related to parking actions.
	 *@ param params the input params.
	 * @param inputLength the input length.
	 * @return true if the input string is valid for parking actions, false otherwise.
	 */
	default boolean isValidCommandParams(int params, int inputLength) {
		switch (inputLength) {
			case 1:
				return params == 0; // e.g status -> inputs = 1
			case 2:
				return params == 1; // create_parking_lot 6 -> inputs = 2
			case 3:
				return params == 2; // park KA-01-P-333 White -> inputs = 3
			default:
				return false;
		}
	}

}
