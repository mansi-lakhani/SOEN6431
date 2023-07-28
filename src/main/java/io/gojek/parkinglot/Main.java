package io.gojek.parkinglot;
import io.gojek.parkinglot.exception.ParkingException;
import io.gojek.parkinglot.processor.AbstractProcessor;
import io.gojek.parkinglot.processor.RequestProcessor;
import io.gojek.parkinglot.service.impl.ParkingServiceImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * The Main class of the application.
 */
public class Main {
 private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
 /**
  * The main method is the entry point of the application.
  */
 public static void main(String[] args) {
  AbstractProcessor processor = new RequestProcessor();
  processor.setService(new ParkingServiceImpl());

  BufferedReader bufferReader = null;
  String encoding = "UTF-8";

  try {
   displayWelcomeMessage();
   processUserInput(args, processor, encoding);
  } catch (ParkingException | IOException e) {
   LOGGER.warning(e.getMessage());
  } finally {
   try {
    if (bufferReader != null) {
     bufferReader.close();
    }
   } catch (IOException e) {
    LOGGER.warning(e.getMessage());
   }
  }
 }

 private static void displayWelcomeMessage() {
  LOGGER.info("\n\n\n\n\n");
  LOGGER.info("===================================================================");
  LOGGER.info("=================== GOJEK PARKING LOT ====================");
  LOGGER.info("===================================================================");
  printUsage();
 }

 private static void processUserInput(String[] args, AbstractProcessor processor, String encoding)
   throws ParkingException, IOException {
  switch (args.length) {
   case 0:
    processInteractiveInput(processor, encoding);
    break;
   case 1:
    processFileInput(processor, args[0], encoding);
    break;
   default:
    LOGGER.info("Invalid input. Usage Style: java -jar <jar_file_path> <input_file_path>");
  }
 }

 private static void processInteractiveInput(AbstractProcessor processor, String encoding)
   throws ParkingException, IOException {
  LOGGER.info("Please Enter 'exit' to end Execution");
  LOGGER.info("Input:");

  BufferedReader bufferReader = new BufferedReader(new InputStreamReader(System.in, encoding));
  String input;

  while (true) {
   input = bufferReader.readLine().trim();
   if (input.equalsIgnoreCase("exit")) {
    break;
   } else {
    if (processor.validate(input)) {
     try {
      processor.execute(input.trim());
     } catch (ParkingException e) {
      LOGGER.warning(e.getMessage());
     }
    } else {
     printUsage();
    }
   }
  }
 }

 private static void processFileInput(AbstractProcessor processor, String filePath, String encoding)
   throws ParkingException, IOException {
  File inputFile = new File(filePath);
  BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), encoding));
  String input;
  int lineNo = 1;

  while ((input = bufferReader.readLine()) != null) {
   input = input.trim();
   if (processor.validate(input)) {
    try {
     processor.execute(input);
    } catch (ParkingException e) {
     LOGGER.warning(e.getMessage());
    }
   } else {
    LOGGER.info("Incorrect Command Found at line: " + lineNo + " ,Input: " + input);
   }
   lineNo++;
  }
 }

 private static void printUsage() {
  StringBuffer buffer = new StringBuffer();
  buffer.append(
      "--------------Please Enter one of the below commands. {variable} to be replaced -----------------------")
    .append("\n");
  buffer.append("A) For creating parking lot of size n ---> create_parking_lot {capacity}")
    .append("\n");
  buffer.append("B) To park a car ---> park <<car_number>> {car_clour}")
    .append("\n");
  buffer.append("C) Remove(Unpark) car from parking ---> leave {slot_number}")
    .append("\n");
  buffer.append("D) Print status of parking slot ---> status").append("\n");
  buffer.append(
      "E) Get cars registration no for the given car color ---> registration_numbers_for_cars_with_color {car_color}")
    .append("\n");
  buffer.append(
      "F) Get slot numbers for the given car color ---> slot_numbers_for_cars_with_color {car_color}")
    .append("\n");
  buffer.append(
      "G) Get slot number for the given car number ---> slot_number_for_registration_number {car_number}")
    .append("\n");
  LOGGER.info(buffer.toString());
 }
}