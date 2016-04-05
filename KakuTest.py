
import subprocess,time

def KakuSwitch(onoff):
	if(onoff == True):
		p = subprocess.Popen("./kaku 18 C on",shell= True, cwd="/home/pi/wiringPi/examples/lights");
		print "Kaku on"
	else:
		p = subprocess.Popen("./kaku 18 C off",shell= True, cwd="/home/pi/wiringPi/examples/lights");
		print "Kaku off"
		
	p.wait()

while True:
	KakuSwitch(True)
	time.sleep(5)
	KakuSwitch(False)
	time.sleep(5)

