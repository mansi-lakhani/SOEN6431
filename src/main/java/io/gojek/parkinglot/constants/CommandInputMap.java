/**
 * 
 */
package io.gojek.parkinglot.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author vaibhav
 *
 */

/**
 * A class that represents a mapping of commands to corresponding input strings.
 * This class is used to store a collection of commands and their corresponding input strings.
 */
public class CommandInputMap
{
 private static volatile Map<String, Integer> commandsParameterMap = new HashMap<String, Integer>();
	
 static
 {
  commandsParameterMap.put(Constants.CREATE_PARKING_LOT, 1);
  commandsParameterMap.put(Constants.PARK, 2);
  commandsParameterMap.put(Constants.LEAVE, 1);
  commandsParameterMap.put(Constants.STATUS, 0);
  commandsParameterMap.put(Constants.REG_NUMBER_FOR_CARS_WITH_COLOR, 1);
  commandsParameterMap.put(Constants.SLOTS_NUMBER_FOR_CARS_WITH_COLOR, 1);
  commandsParameterMap.put(Constants.SLOTS_NUMBER_FOR_REG_NUMBER, 1);
 }
	
 /**
  * @return the commandsParameterMap
  */
 public static Map<String, Integer> getCommandsParameterMap()
 {
  return commandsParameterMap;
 }
	
 /**
  * @param command ParameterMap The map containing commands and their associated parameters.
  *
  */

 /**
  * Adds a command with the specified parameter count to the commandsParameterMap.
  *
  * @param command The command to be added.
  * @param parameterCount The count of parameters associated with the command.
  */
 public static void addCommand(String command, int parameterCount)
 {
  commandsParameterMap.put(command, parameterCount);
 }
	
}