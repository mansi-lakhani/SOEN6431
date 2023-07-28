/**
 *
 */
package io.gojek.parkinglot.exception;

/**
 * @author vaibhav
 *
 */

/**
 * Enum used to define error codes and messages for different error scenarios in the parking system.
 */
public enum ErrorCode
{
	PARKING_ALREADY_EXIST("Sorry Parking Already Created, It CAN NOT be again recreated."), PARKING_NOT_EXIST_ERROR(
		"Sorry, Car Parking Does not Exist"), INVALID_VALUE("{variable} value is incorrect"), INVALID_FILE(
		"Invalid File"), PROCESSING_ERROR("Processing Error "), INVALID_REQUEST("Invalid Request");

	private String message = "";

	/**
	 * Constructs an error message.
	 *
	 * @param message The error message that describes the reason for this Exception.
	 */
	private ErrorCode(String message)
	{
		this.message = message;
	}

	public String getMessage()
	{
		return message;
	}
}
