
import RPi.GPIO as GPIO, time

GPIO.setmode(GPIO.BCM)

 # Raspberry pi 2
 # 3.3V, example pin 1
 # Ground, example pin 6
 # GPIO 4, example pin 7

def RCtime(RCpin):
	reading = 0;
	GPIO.setup(RCpin, GPIO.OUT)
	GPIO.output(RCpin,GPIO.LOW)
	time.sleep(0.1)

	GPIO.setup(RCpin, GPIO.IN)
	while(GPIO.input(RCpin) == GPIO.LOW):
		reading += 1
	return reading

while True:
	print RCtime(4)#GPIO 4
	time.sleep(1)
