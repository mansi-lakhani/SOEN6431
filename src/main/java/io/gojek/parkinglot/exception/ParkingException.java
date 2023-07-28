/**
 * 
 */
package io.gojek.parkinglot.exception;

/**
 * @author vaibhav
 *
 */
public class ParkingException extends Exception
{
 private static final long serialVersionUID = -3552275262672621625L;
	
 private String errorCode = null; // this will hold system defined error code
 private Object[] errorParameters = null; // this will hold parameters for error code/message
	
 /**
   * @param message The error message that describes the reason for the exception.
  * @param throwable The throwable to be wrapped in this CustomException.
  */
 public ParkingException(String message, Throwable throwable)
 {
  super(message, throwable);
 }
	
 /**
   * @param message The error message that describes the reason for the exception.
  */
 public ParkingException(String message)
 {
  super(message);
 }
	
 /**
  * Constructs a new ParkingException with the specified throwable as the cause.
  *
  * @param throwable The throwable to be wrapped in this ParkingException.
  */
 public ParkingException(Throwable throwable)
 {
  super(throwable);
 }
	
 /**
  * Constructs a new ParkingException with the specified error code, message, and additional error parameters.
  *
  * @param errorCode The error code associated with this CustomException.
  * @param message The error message that describes the reason for this CustomException.
  * @param errorParameters The additional error parameters that provide context to the exception.
  */
 public ParkingException(String errorCode, String message, Object[] errorParameters)
 {
  super(message);
  this.setErrorCode(errorCode);
  this.setErrorParameters(errorParameters);
 }
	
 /**
  * Constructs a new ParkingException with the specified error code, message, and wrapped throwable.
  *
  * @param errorCode The error code associated with this ParkingException.
  * @param message The error message that describes the reason for this ParkingException.
  * @param throwable The throwable to be wrapped in this ParkingException.
  */
 public ParkingException(String errorCode, String message, Throwable throwable)
 {
  super(message, throwable);
  this.setErrorCode(errorCode);
 }
	
 /**
  * Constructs a new ParkingException with the specified error code, message, and additional error parameters.
  *
  * @param errorCode The error code associated with this CustomException.
  * @param message The error message that describes the reason for this CustomException.
  * @param errorParameters The additional error parameters that provide context to the exception.
  */
 public ParkingException(String errorCode, String message, Object[] errorParameters, Throwable throwable)
 {
  super(message, throwable);
  this.setErrorCode(errorCode);
  this.setErrorParameters(errorParameters);
 }
	
 public String getErrorCode()
 {
  return errorCode;
 }
	
 public void setErrorCode(String errorCode)
 {
  this.errorCode = errorCode;
 }
	
 public Object[] getErrorParameters()
 {
  return errorParameters;
 }
	
 public void setErrorParameters(Object[] errorParameters)
 {
  this.errorParameters = errorParameters;
 }
}
