import RPi.GPIO as GPIO, time,subprocess

def KakuSwitch(onoff):
	if(onoff == True):
		p = subprocess.Popen("./kaku 18 C on",shell= True, cwd="/home/pi/wiringPi/examples/lights");
		print "Kaku on"
	else:
		p = subprocess.Popen("./kaku 18 C off",shell= True, cwd="/home/pi/wiringPi/examples/lights");
		print "Kaku off"
		
	p.wait()
	
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

	value = RCtime(4)#pin invullen
	print value
	
	if(value > 100):
		KakuSwitch(True)
	else	
		KakuSwitch(False)
	
	time.sleep(1)
