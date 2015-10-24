package com.projectvalis.reverse_rabin;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;

import junit.framework.Assert;

import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.BeforeStories;
import org.jbehave.core.annotations.BeforeStory;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;
import org.rabinfingerprint.polynomial.Polynomial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.projectvalis.util.rabin.RabinFingerprintLong_SmooshMod;


/**
 * BDD step file for the 'can we reverse a rabin hash' experiment
 * @author snerd
 *
 */
public class ReverseRabinSteps extends Steps {
	static Logger LOGGER = LoggerFactory.getLogger(ReverseRabinSteps.class);
		
	private byte[] generatedByteARR;
	private Polynomial rabinPolynomial;
	private RabinFingerprintLong_SmooshMod fingerprinter;
	
	
	
	@BeforeScenario
	public void setup() {
		rabinPolynomial = Polynomial.createIrreducible(53); 
		fingerprinter = new RabinFingerprintLong_SmooshMod(rabinPolynomial);
		
		LOGGER.info(
				"GENERATED POLYNOMIAL IS: " + rabinPolynomial.toHexString());	
	}
	
	
	
	@Given("an array of $numBytesI bytes")
	public void createByteArray(@Named("numBytesI") int numBytesI) {
		Random randy = new Random();
		generatedByteARR = new byte[numBytesI];
		
		for (int i = 0; i < numBytesI; i ++) {
			generatedByteARR[i] = (byte)randy.nextInt(256);
		}
		
		String generatedBytesS = getHexString(generatedByteARR);
		LOGGER.info("GENERATED BYTES ARE: " + generatedBytesS.toUpperCase());
	}
	
	
	@When("the byte array is fingerprinted")
	public void fingerPrintByteArray() {
		fingerprinter.pushBytes(generatedByteARR);
	}
	
	
	
	
	@Then("it is possible to retrieve $retrieveNumI of the original "
			+ "$numBytesI bytes")
	public void retrieveBytes(@Named("retrieveNumI") int retrieveNumI,
							  @Named("numBytesI") int numBytesI) {
		
		long fingerprintL = fingerprinter.getFingerprintLong();
		
		byte[] fingerprintByteARR = 
				ByteBuffer.allocate(8).putLong(fingerprintL).array();
		
		byte[] invertedfingerprintByteARR = new byte[retrieveNumI];
		
		for (int i = 0; i < retrieveNumI; i++) {
			byte invertedB = fingerprintByteARR[i];

		}
	
		
		Assert.assertTrue("INVERSION FAILED!", 
				Arrays.equals(generatedByteARR, invertedfingerprintByteARR));
		
	}
	
	
	
	/**
	 * simple utility plucked from the interwebs to convert a byte array to 
	 * a hex string (yes I am that lazy)
	 * 
	 * yoinked from: http://www.rgagnon.com/javadetails/java-0596.html
	 * 
	 * 
	 * @param b
	 * @return
	 * @throws Exception
	 */
	public String getHexString(byte[] b) {
		  String result = "";
		  for (int i=0; i < b.length; i++) {
		    result +=
		          Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		  }
		  return result;
	}
	
	
}







