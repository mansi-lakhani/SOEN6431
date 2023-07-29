package io.gojek.parkinglot;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import io.gojek.parkinglot.exception.ErrorCode;
import io.gojek.parkinglot.exception.ParkingException;
import io.gojek.parkinglot.model.Car;
import io.gojek.parkinglot.service.ParkingService;
import io.gojek.parkinglot.service.impl.ParkingServiceImpl;

/**
 * Unit test for simple App.
 */
public class MainTest
{
	private int							parkingLevel;
	private final ByteArrayOutputStream	outContent	= new ByteArrayOutputStream();

	/**
	 * thrown variable is used for testing to indicate the expected exception during test cases.
	 * This variable is initialized to 'ExpectedException.none()' to indicate no expected exceptions by default.
	 */
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	/**
	 * Initialize the parking system for testing purposes.
	 * This method sets the parking level to 1 and redirects the standard output to the 'outContent' stream
	 * to capture console outputs during testing.
	 */
	@Before
	public void init()
	{
		parkingLevel = 1;
		System.setOut(new PrintStream(outContent));
	}

	/**
	 * Clean up the resources used during testing.
	 * This method will be automatically executed after each test case.
	 * It resets the standard output to the default value (null), allowing the console output to resume
	 * its normal behavior after testing is complete.
	 */
	@After
	public void cleanUp()
	{
		System.setOut(null);
	}

	/**
	 * Test method to create a parking lot with the specified capacity.
	 *
	 * @throws Exception if an exception occurs during the test.
	 */
	@Test
	public void createParkingLot() throws Exception
	{
		ParkingService instance = new ParkingServiceImpl();
		instance.createParkingLot(parkingLevel, 65);
		assertTrue("createdparkinglotwith65slots".equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.doCleanup();
	}

	/**
	 * Test method to verify that creating a parking lot with an existing level results in an exception.
	 * @throws Exception if an exception occurs during the test.
	 */
	@Test
	public void alreadyExistParkingLot() throws Exception
	{
		ParkingService instance = new ParkingServiceImpl();
		instance.createParkingLot(parkingLevel, 65);
		assertTrue("createdparkinglotwith65slots".equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_ALREADY_EXIST.getMessage()));
		instance.createParkingLot(parkingLevel, 65);
		instance.doCleanup();
	}

	/**
	 * Test method to verify the parking capacity of a parking lot.
	 *
	 * @throws Exception if an exception occurs during the test.
	 */
	@Test
	public void testParkingCapacity() throws Exception
	{
		ParkingService instance = new ParkingServiceImpl();
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 11);
		instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		instance.park(parkingLevel, new Car("KA-01-BB-0001", "Black"));
		assertEquals(3, instance.getAvailableSlotsCount(parkingLevel));
		instance.doCleanup();
	}

	/**
	 * Test method to check the status of an empty parking lot.
	 *
	 * @throws Exception if an exception occurs during the test.
	 */
	@Test
	public void testEmptyParkingLot() throws Exception
	{
		ParkingService instance = new ParkingServiceImpl();
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.getStatus(parkingLevel);
		assertTrue("Sorry,CarParkingDoesnotExist".equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.createParkingLot(parkingLevel, 6);
		instance.getStatus(parkingLevel);
		assertTrue(
				"Sorry,CarParkingDoesnotExist\ncreatedparkinglotwith6slots\nSlotNo.\tRegistrationNo.\tColor\nSorry,parkinglotisempty."
						.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.doCleanup();
	}


	/**
	 * Test for a parking service that manages parking lots and cars.
	 *
	 * @throws Exception if an exception occurs during the test.
	 */
	@Test
	public void testParkingLotIsFull() throws Exception {
		ParkingService instance = new ParkingServiceImpl();
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 2);
		instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		instance.park(parkingLevel, new Car("KA-01-BB-0001", "Black"));

		assertTrue("createdparkinglotwith2slots\\nAllocatedslotnumber:1\nAllocatedslotnumber:2\nSorry,parkinglotisfull"
				.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.doCleanup();
	}

	/**
	 * Test method to verify the nearest slot allotment for parking a car.
	 *
	 * @throws Exception if an exception occurs during the test.
	 */
	@Test
	public void testNearestSlotAllotment() throws Exception
	{
		ParkingService instance = new ParkingServiceImpl();
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 5);
		instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		instance.getSlotNoFromRegistrationNo(parkingLevel, "KA-01-HH-1234");
		instance.getSlotNoFromRegistrationNo(parkingLevel, "KA-01-HH-9999");
		assertTrue("createdparkinglotwith5slots\nAllocatedslotnumber:1\nAllocatedslotnumber:2"
				.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.doCleanup();
	}
	/**
	 * Test method to verify the leave method.
	 *
	 * @throws Exception if an exception occurs during the test.
	 */
	@Test
	public void leave() throws Exception
	{
		ParkingService instance = new ParkingServiceImpl();
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.unPark(parkingLevel, 2);
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 6);
		instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		instance.park(parkingLevel, new Car("KA-01-BB-0001", "Black"));
		instance.unPark(parkingLevel, 4);
		assertTrue(
				"Sorry,CarParkingDoesnotExist\ncreatedparkinglotwith6slots\nAllocatedslotnumber:1\nAllocatedslotnumber:2\nAllocatedslotnumber:3\nSlotnumber4isfree"
						.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.doCleanup();
	}

	/**
	 * Test method to verify the behavior when attempting to park a vehicle that is already present in the parking lot.
	 *
	 * @throws Exception if an exception occurs during the test.
	 */
	@Test
	public void testWhenVehicleAlreadyPresent() throws Exception
	{
		ParkingService instance = new ParkingServiceImpl();
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 3);
		instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		assertTrue(
				"Sorry,CarParkingDoesnotExist\ncreatedparkinglotwith3slots\nAllocatedslotnumber:1\nSorry,vehicleisalreadyparked."
						.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.doCleanup();
	}

	/**
	 * Test method to verify the behavior when attempting to pick a vehicle that has already been picked.
	 *
	 * @throws Exception if an exception occurs during the test.
	 */
	@Test
	public void testWhenVehicleAlreadyPicked() throws Exception
	{
		ParkingService instance = new ParkingServiceImpl();
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 99);
		instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		instance.unPark(parkingLevel, 1);
		instance.unPark(parkingLevel, 1);
		assertTrue(
				"Sorry,CarParkingDoesnotExist\ncreatedparkinglotwith99slots\nAllocatedslotnumber:1\nAllocatedslotnumber:2\nSlotnumberisEmptyAlready."
						.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.doCleanup();
	}

	/**
	 * Test method to check the status of the parking lot
	 * @throws Exception if an exception occurs during the test.
	 */
	@Test
	public void testStatus() throws Exception
	{
		ParkingService instance = new ParkingServiceImpl();
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.getStatus(parkingLevel);
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 8);
		instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		instance.getStatus(parkingLevel);
		assertTrue(
				"Sorry,CarParkingDoesnotExist\ncreatedparkinglotwith8slots\nAllocatedslotnumber:1\nAllocatedslotnumber:2\nSlotNo.\tRegistrationNo.\tColor\n1\tKA-01-HH-1234\tWhite\n2\tKA-01-HH-9999\tWhite"
						.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.doCleanup();

	}

	/**
	 * Test method to get the slot using the registration number
	 * @throws Exception if an exception occurs during the test.
	 */
	@Test
	public void testGetSlotsByRegNo() throws Exception
	{
		ParkingService instance = new ParkingServiceImpl();
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.getSlotNoFromRegistrationNo(parkingLevel, "KA-01-HH-1234");
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 10);
		instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		instance.getSlotNoFromRegistrationNo(parkingLevel, "KA-01-HH-1234");
		assertEquals("Sorry,CarParkingDoesnotExist\n" + "Createdparkinglotwith6slots\n" + "\n"
						+ "Allocatedslotnumber:1\n" + "\n" + "Allocatedslotnumber:2\n1",
				outContent.toString().trim().replace(" ", ""));
		instance.getSlotNoFromRegistrationNo(parkingLevel, "KA-01-HH-1235");
		assertEquals("Sorry,CarParkingDoesnotExist\n" + "Createdparkinglotwith10slots\n" + "\n"
						+ "Allocatedslotnumber:1\n" + "\n" + "Allocatedslotnumber:2\n1\nNotFound",
				outContent.toString().trim().replace(" ", ""));
		instance.doCleanup();
	}

	/**
	 * Test method to get the slot using the colour.
	 * @throws Exception if an exception occurs during the test.
	 */
	@Test
	public void testGetSlotsByColor() throws Exception
	{
		ParkingService instance = new ParkingServiceImpl();
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.getRegNumberForColor(parkingLevel, "white");
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 7);
		instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		instance.getStatus(parkingLevel);
		instance.getRegNumberForColor(parkingLevel, "Cyan");
		assertEquals(
				"Sorry,CarParkingDoesnotExist\n" + "Createdparkinglotwith7slots\n" + "\n" + "Allocatedslotnumber:1\n"
						+ "\n" + "Allocatedslotnumber:2\nKA-01-HH-1234,KA-01-HH-9999",
				outContent.toString().trim().replace(" ", ""));
		instance.getRegNumberForColor(parkingLevel, "Red");
		assertEquals(
				"Sorry,CarParkingDoesnotExist\n" + "Createdparkinglotwith6slots\n" + "\n" + "Allocatedslotnumber:1\n"
						+ "\n" + "Allocatedslotnumber:2\n" + "KA-01-HH-1234,KA-01-HH-9999,Notfound",
				outContent.toString().trim().replace(" ", ""));
		instance.doCleanup();

	}
}
