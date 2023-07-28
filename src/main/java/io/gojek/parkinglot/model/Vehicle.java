/**
 *
 */
package io.gojek.parkinglot.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * @author vaibhav
 *
 */

/**
 * This class represents a Vehicle and implements the Externalizable interface.
 *
 * <p>The Externalizable interface is used for controlling the serialization and
 * deserialization of the Vehicle objects
 */
public abstract class Vehicle implements Externalizable
{
 /**
  * Constructs a new Vehicle object with default values.
  * This constructor is used to create a Vehicle object with default attribute values.
  */
 public Vehicle() {
  // No-arg constructor
 }
 private String registrationNo = null;
 private String color = null;

 /**
  * Constructs a new Vehicle object with the provided registration number and color.
  *
  * @param registrationNo The registration number of the vehicle.
  * @param color The color of the vehicle.
  */
 public Vehicle(String registrationNo, String color)
 {
  this.registrationNo = registrationNo;
  this.color = color;
 }

 @Override
 public String toString()
 {
  return "[registrationNo=" + registrationNo + ", color=" + color + "]";
 }

 /**
  * @return the registrationNo
  */
 public String getRegistrationNo()
 {
  return registrationNo;
 }

 /**
  * @param registrationNo
  * the registrationNo to set
  */
 public void setRegistrationNo(String registrationNo)
 {
  this.registrationNo = registrationNo;
 }

 /**
  * @return the color
  */
 public String getColor()
 {
  return color;
 }

 /**
  * @param color
  * the color to set
  */
 public void setColor(String color)
 {
  this.color = color;
 }

 @Override
 public void writeExternal(ObjectOutput out) throws IOException
 {
  out.writeObject(getRegistrationNo());
  out.writeObject(getColor());
 }

 @Override
 public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
 {
  setRegistrationNo((String) in.readObject());
  setColor((String) in.readObject());
 }
}