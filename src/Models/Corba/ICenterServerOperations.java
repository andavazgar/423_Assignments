package Models.Corba;


/**
* Models/Corba/ICenterServerOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from CORBA.idl
* Friday, October 19, 2018 1:05:36 o'clock PM EDT
*/

public interface ICenterServerOperations 
{
  String createMRecord (String managerID, String firstName, String lastName, int employeeID, String mailID, Models.Corba.Project project, String location);
  String createERecord (String managerID, String firstName, String lastName, int employeeID, String mailID, String projectID);
  String getRecordCounts (String managerID);
  String editRecord (String managerID, String recordID, String fieldName, String newValue);
  String transferRecord (String managerID, String recordID, String remoteCenterServerName);
  void printData ();
} // interface ICenterServerOperations
