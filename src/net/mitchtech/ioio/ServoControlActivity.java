package net.mitchtech.ioio;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
//import ioio.lib.util.AbstractIOIOActivity;
import ioio.lib.util.android.IOIOActivity;
import ioio.lib.android.bluetooth.*;
import net.mitchtech.ioio.servocontrol.R;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

//public class ServoControlActivity extends AbstractIOIOActivity {
public class ServoControlActivity extends IOIOActivity implements SensorEventListener{
	private final int MOTOR1 = 11;
	private final int MOTOR2 = 12;
	private final int MOTOR3 = 13;
	private final int MOTOR4 = 14;
	private final int DIRECTION1 = 3;
	private final int DIRECTION2 = 6;
	private final int DIRECTION3 = 7;
	private final int DIRECTION4 = 10;
	private final int PWM_FREQ = 100;
	private final int SENSOR1 = 35;
	
	private int SPEED = 1500;
	
	private Button bForward;
	private Button bBackward;
	private Button bLeft;
	private Button bRight;
	
	private ToggleButton tMotor1;
	private ToggleButton tMotor2;
	private ToggleButton tMotor3;
	private ToggleButton tMotor4;
	
	private SeekBar sBar;
	
	private TextView txtViewSensor1;
	
	private static float sensors[];
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		bForward = (Button) findViewById(R.id.btnForward);
		bBackward = (Button) findViewById(R.id.btnBackward);
		bLeft = (Button) findViewById(R.id.btnLeft);
		bRight = (Button) findViewById(R.id.btnRight);
		
		tMotor1 = (ToggleButton) findViewById(R.id.tgMotor1);
		tMotor2 = (ToggleButton) findViewById(R.id.tgMotor2);
		tMotor3 = (ToggleButton) findViewById(R.id.tgMotor3);
		tMotor4 = (ToggleButton) findViewById(R.id.tgMotor4);
		
		sBar = (SeekBar) findViewById(R.id.seekBar1);
		
		txtViewSensor1 = (TextView) findViewById(R.id.txtVoltage);
		sensors = new float[5];
		new IMU().start();
		
		enableUi(false);
	}

//	class IOIOThread extends AbstractIOIOActivity.IOIOThread {
	class IOIOThread extends BaseIOIOLooper{
		private PwmOutput pwmMotor1;
		private PwmOutput pwmMotor2;
		private PwmOutput pwmMotor3;
		private PwmOutput pwmMotor4;
		
		private DigitalOutput direction1;
		private DigitalOutput direction2;
		private DigitalOutput direction3;
		private DigitalOutput direction4;
		
		private AnalogInput sensor1;

		public void setup() throws ConnectionLostException {
			try {
				pwmMotor1 = ioio_.openPwmOutput(MOTOR1, PWM_FREQ);
				pwmMotor2 = ioio_.openPwmOutput(MOTOR2, PWM_FREQ);
				pwmMotor3 = ioio_.openPwmOutput(MOTOR3, PWM_FREQ);
				pwmMotor4 = ioio_.openPwmOutput(MOTOR4, PWM_FREQ);
				
				direction1 = ioio_.openDigitalOutput(DIRECTION1, true);
				direction2 = ioio_.openDigitalOutput(DIRECTION2, true);
				direction3 = ioio_.openDigitalOutput(DIRECTION3, true);
				direction4 = ioio_.openDigitalOutput(DIRECTION4, true);
				
				sensor1 = ioio_.openAnalogInput(SENSOR1);
				enableUi(true);
				
			} catch (ConnectionLostException e) {
				enableUi(false);
				throw e;
			}
		}
		
		public void setText(final String msg){
			runOnUiThread(new Runnable(){
				@Override
				public void run(){
					txtViewSensor1.setText(msg);	
				}
			});
		}

		public void loop() throws ConnectionLostException {
			SPEED = sBar.getProgress();
			try {
					setText("" + sensor1.read());
					if(bForward.isPressed() || bBackward.isPressed()){
						if(bForward.isPressed()){
							direction1.write(true);
							direction2.write(false);
							direction3.write(true);
							direction4.write(false);
						}
						else{
							direction1.write(false);
							direction2.write(true);
							direction3.write(false);
							direction4.write(true);
						}
						//pulseWidth range 0 - 1500
						if(tMotor1.isChecked()) pwmMotor1.setPulseWidth(SPEED);
						if(tMotor2.isChecked()) pwmMotor2.setPulseWidth(SPEED);
						if(tMotor3.isChecked()) pwmMotor3.setPulseWidth(SPEED);
						if(tMotor4.isChecked()) pwmMotor4.setPulseWidth(SPEED);
					}
					else if(bLeft.isPressed()){
						direction1.write(true);
						direction2.write(false);
						direction3.write(false);
						direction4.write(true);
						if(tMotor1.isChecked()) pwmMotor1.setPulseWidth(SPEED);
						if(tMotor2.isChecked()) pwmMotor2.setPulseWidth(SPEED);
						if(tMotor3.isChecked()) pwmMotor3.setPulseWidth(SPEED);
						if(tMotor4.isChecked()) pwmMotor4.setPulseWidth(SPEED);
					}
					else if(bRight.isPressed()){
						direction1.write(false);
						direction2.write(true);
						direction3.write(true);
						direction4.write(false);
						if(tMotor1.isChecked()) pwmMotor1.setPulseWidth(SPEED);
						if(tMotor2.isChecked()) pwmMotor2.setPulseWidth(SPEED);
						if(tMotor3.isChecked()) pwmMotor3.setPulseWidth(SPEED);
						if(tMotor4.isChecked()) pwmMotor4.setPulseWidth(SPEED);
					}
					else{
						pwmMotor1.setPulseWidth(0);
						pwmMotor2.setPulseWidth(0);
						pwmMotor3.setPulseWidth(0);
						pwmMotor4.setPulseWidth(0);
					}
				Thread.sleep(10);
			} catch (InterruptedException e) {
				ioio_.disconnect();
			} catch (ConnectionLostException e) {
				enableUi(false);
				throw e;
			}
		}
	}
	
	@Override
//	protected AbstractIOIOActivity.IOIOThread createIOIOThread() {
	protected IOIOLooper createIOIOLooper() {
		return new IOIOThread();
	}

	private void enableUi(final boolean enable) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				bForward.setEnabled(enable);
				bBackward.setEnabled(enable);
			}
		});
	}
	
	public void btnFordwardonTouch(View v, MotionEvent e){
		
	}
	
	public void btnBackwardonTouch(View v, MotionEvent e){
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	public static float getSensorReadings(int indx){
		return sensors[indx];
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		sensors[0] = event.values[0];
		sensors[1] = event.values[1];
		sensors[2] = event.values[2];
	}
}