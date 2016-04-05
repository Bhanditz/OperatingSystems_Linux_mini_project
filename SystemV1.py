import RPi.GPIO as GPIO, time,subprocess

GPIO.setmode(GPIO.BCM)

def KakuSwitch(onoff):
	if(onoff == True):
		p = subprocess.Popen("./kaku 18 C on",shell= True, cwd="/home/pi/wiringPi/examples/lights");
		print "Kaku on"
	else:
		p = subprocess.Popen("./kaku 18 C off",shell= True, cwd="/home/pi/wiringPi/examples/lights");
		print "Kaku off"
		
	p.wait()
	
def RCtime(RCpin):
	reading = 0
	GPIO.setup(RCpin, GPIO.OUT)
	GPIO.output(RCpin,GPIO.LOW)
	time.sleep(0.1)

	GPIO.setup(RCpin, GPIO.IN)
	while(GPIO.input(RCpin) == GPIO.LOW):
		reading += 1
	return reading

onoff = False

while True:
	global onoff

	value = RCtime(4)#pin invullen
	print value
	
	if(value > 100):
		if(onoff != True):
			KakuSwitch(True)
		onoff = True
	else:	
		if(onoff != False):
			KakuSwitch(False)
		onoff = False
	
	time.sleep(1)
