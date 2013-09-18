package net.mitchtech.ioio;

public class Config {
	
	/* 
		struct {					
	  		char inpInvert[INPUT_COUNT];   
	  		int zeroLevel[INPUT_COUNT];    
	  		int inpSens[INPUT_COUNT];
	                                 
	  		float wGyro;		          
		} config;
	 */
	
	int INPUT_COUNT;
	char inpInvert[];    // bits 0..5 invert input
	int zeroLevel[];     // 0..2 accelerometer zero level (mV) @ 0 G
	                     // 3..5 gyro zero level (mV) @ 0 deg/s
	int inpSens[];       // 0..2 accelerometer input sensitivity (mv/g)
	                     // 3..5 gyro input sensitivity (mV/deg/ms) 
	float wGyro;		 // gyro weight/smoothing factor
	
	// default constructor
	public Config() {}
	
	// constructor that passes the input count of the analogs
	public Config(int INPUT_COUNT) {
		inpInvert = new char[INPUT_COUNT];
		zeroLevel = new int [INPUT_COUNT];
		inpSens = new int [INPUT_COUNT];
	}
}
