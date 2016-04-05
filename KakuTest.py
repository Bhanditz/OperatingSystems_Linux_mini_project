
import subprocess,time
p = subprocess.Popen("./kaku 18 C on",shell = True, cwd="/home/pi/wiringPi/examples/lights");
p.wait()

def KakuSwitch(onoff):
	if(onoff == True):
		p = subprocess.Popen("./kaku 18 C on",shell= True, cwd="/home/pi/wiringPi/examples/lights");
		print "Kaku on"
	else:
		p = subprocess.Popen("./kaku 18 C off",shell= True, cwd="/home/pi/wiringPi/examples/lights");
		print "Kaku off"

while True:
	KakuSwitch(True)
	time.sleep(5)
	KakuSwitch(False)
	time.sleep(5)

