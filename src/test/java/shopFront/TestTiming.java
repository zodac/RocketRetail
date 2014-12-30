package shopFront;

import javax.swing.JPanel;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This is a testing class
 * and it tests that various
 * operations are completed within 
 * specified time limits.
 * 
 * @author Sheikh
 *
 */

public class TestTiming {
	/**
	 * The upper time limit for the tasks to complete.
	 */
	public static final int bigLimit = 100;
	/**
	 * The lower time limit for the tasks to complete.
	 */
	public static final int smallLimit = 50;
	
	/**
	 * This method creates a <code>JPanel</code>
	 * and initialises the driver arrays
	 * before the tests are run.
	 */
	@BeforeClass
	public static void setUpBeforeClass(){
		ShopDriver.mainPanel = new JPanel();
		ShopDriver.leftPanel = new JPanel();
		ShopDriver.initialiseArrays();
		ShopDriver.currentStaff = ShopDriver.staffMembers.get(0);
	}

	/**
	 * This test is testing that the arrays
	 * are initialised within the smallLimit.
	 */
	@Test(timeout = smallLimit)
	public void InitialiseArraysTest() {
		ShopDriver.initialiseArrays();
	}
	
	/**
	 * This test is testing that the given 
	 * amount of orders are generated
	 * within the smallLimit.
	 */
	@Test(timeout = smallLimit)
	public void GenerateOrdersTenTest() {
		InitialiseArray.generateOrders(10, false, true);
	}

	/**
	 * This test is testing that the 
	 * <code>staff()</code> method will complete within
	 * the bigLimit.
	 */
	@Test(timeout = bigLimit)
	public void StaffTest() {
		StaffTable.staff();
	}
	/**
	 * This test is testing that the 
	 * <code>product()</code> method will complete within
	 * the bigLimit.
	 */	
	@Test(timeout = bigLimit)
	public void ProductTest() {
		ProductTable.product();
	}
	/**
	 * This test is testing that the 
	 * <code>supplier()</code> method will complete within
	 * the bigLimit.
	 */
	@Test(timeout = bigLimit)
	public void SupplierTest() {
		SupplierTable.supplier();
	}
	/**
	 * This test is testing that the 
	 * <code>customer()</code> method will complete within
	 * the bigLimit.
	 */
	@Test(timeout = bigLimit)
	public void CustomerTest() {
		CustomerTable.customer();
	}

}
