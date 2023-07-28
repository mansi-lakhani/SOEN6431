package io.gojek.parkinglot;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.IllegalFormatException;

import io.gojek.parkinglot.exception.ErrorCode;
import io.gojek.parkinglot.exception.ParkingException;
import io.gojek.parkinglot.processor.AbstractProcessor;
import io.gojek.parkinglot.processor.RequestProcessor;
import io.gojek.parkinglot.service.impl.ParkingServiceImpl;
import java.util.logging.Logger;

public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	public static void main(String[] args) {
		AbstractProcessor processor = new RequestProcessor();
		processor.setService(new ParkingServiceImpl());
		BufferedReader bufferReader = null;
		String input = null;
		try {
			System.out.println("\n\n\n\n\n");
			System.out.println("===================================================================");
			System.out.println("===================      GOJEK PARKING LOT     ====================");
			System.out.println("===================================================================");
			printUsage();
			switch (args.length) {
				case 0:
					interactiveMode();
					break;
				case 1:
					fileMode(args[0]);
					break;
				default:
					LOGGER.info("Invalid input. Usage Style: java -jar <jar_file_path> <input_file_path>");
			}
		} catch (ParkingException e) {
			e.printStackTrace();
		} finally {
			// Existing code
		}
		System.err.println("This is an error message.");
	}

	private static void interactiveMode() throws ParkingException {
		AbstractProcessor processor = new RequestProcessor();
		processor.setService(new ParkingServiceImpl());
		BufferedReader bufferReader = null;
		String input = null;
		try {
			LOGGER.info("Please Enter 'exit' to end Execution");
			LOGGER.info("Input:");
			while (true) {
				bufferReader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
				input = bufferReader.readLine().trim();
				if (input.equalsIgnoreCase("exit")) {
					break;
				} else {
					if (processor.validate(input)) {
						processor.execute(input.trim());
					} else {
						printUsage();
					}
				}
			}
		} catch (IOException e) {
			throw new ParkingException(ErrorCode.INVALID_REQUEST.getMessage(), e);
		} finally {
			try {
				if (bufferReader != null) {
					bufferReader.close();
				}
			} catch (IOException e) {

			}
		}
	}

	private static void fileMode(String filePath) throws ParkingException {
		AbstractProcessor processor = new RequestProcessor();
		processor.setService(new ParkingServiceImpl());
		BufferedReader bufferReader = null;
		String input = null;
		try {
			File inputFile = new File(filePath);
			bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8));
			int lineNo = 1;
			while ((input = bufferReader.readLine()) != null) {
				input = input.trim();
				if (processor.validate(input)) {
					processor.execute(input);
				} else {
					System.out.println("Incorrect Command Found at line: " + lineNo + " ,Input: " + input);
				}
				lineNo++;
			}
		} catch (IOException e) {
			throw new ParkingException(ErrorCode.INVALID_FILE.getMessage(), e);
		} finally {
			try {
				if (bufferReader != null) {
					bufferReader.close();
				}
			} catch (IOException e) {

			}
		}
		System.err.println("This is an error message.");
	}

	//printUsage method
	private static void printUsage() {
		StringBuffer buffer = new StringBuffer();
		buffer = buffer.append(
						"--------------Please Enter one of the below commands. {variable} to be replaced -----------------------")
				.append("\n");
		buffer = buffer.append("A) For creating parking lot of size n               ---> create_parking_lot {capacity}")
				.append("\n");
		buffer = buffer
				.append("B) To park a car                                    ---> park <<car_number>> {car_clour}")
				.append("\n");
		buffer = buffer.append("C) Remove(Unpark) car from parking                  ---> leave {slot_number}")
				.append("\n");
		buffer = buffer.append("D) Print status of parking slot                     ---> status").append("\n");
		buffer = buffer.append(
						"E) Get cars registration no for the given car color ---> registration_numbers_for_cars_with_color {car_color}")
				.append("\n");
		buffer = buffer.append(
						"F) Get slot numbers for the given car color         ---> slot_numbers_for_cars_with_color {car_color}")
				.append("\n");
		buffer = buffer.append(
						"G) Get slot number for the given car number         ---> slot_number_for_registration_number {car_number}")
				.append("\n");
		System.out.println(buffer.toString());
	}
}
