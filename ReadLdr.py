#! /usr/bin/python
import RPi.GPIO as GPIO, time,sys

GPIO.setmode(GPIO.BCM)

 # Raspberry pi 2
 # 3.3V, example pin 1
 # Ground, example pin 6
 # GPIO 4, example pin 7
 # Resistance 
 # http://www.raspberrypi-spy.co.uk/2012/08/reading-analogue-sensors-with-one-gpio-pin/

def RCtime(RCpin):
	reading = 0;
	GPIO.setup(RCpin, GPIO.OUT)
	GPIO.output(RCpin,GPIO.LOW)
	time.sleep(0.1)

	GPIO.setup(RCpin, GPIO.IN)
	while(GPIO.input(RCpin) == GPIO.LOW):
		reading += 1
	return reading
	
print "ik ben bezig"
print RCtime(4)
