import RPi.GPIO as GPIO, time, subprocess, json

GPIO.setmode(GPIO.BCM)

def KakuSwitch(onoff,idl,idn):
	command = "./kaku "+str(idn)+" "+str(idl);
	if(onoff == True):
		command = command + " on"
		p = subprocess.Popen(command,shell= True, cwd="/home/pi/wiringPi/examples/lights");
		print "Kaku on"
	else:
		command = command + " off"
		p = subprocess.Popen(command,shell= True, cwd="/home/pi/wiringPi/examples/lights");
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

def getLuxValue():
	jsonFile = open("/home/pi/OperatingSystems_Linux_mini_project/serverData.json","r")
	data = json.load(jsonFile)
	jsonFile.close()
	#print data
	value = data["ServerData"][0]["Luxvalue"]
	print value
	return value

def updateKakuStatus(status):
		jsonFile = open("/home/pi/OperatingSystems_Linux_mini_project/kakuData.json","r")
		data = json.load(jsonFile)
		jsonFile.close()
		#print data		
		
		for i in range(len(data["kakus"])):
			# print i
			# print data["kakus"][i]["id"]
			# print id
			# print data["kakus"][i]["id"] == id
			#print data["kakus"][i]["status"]
			if(status != data["kakus"][i]["status"]):
				KakuSwitch(status,data["kakus"][i]["idletter"],data["kakus"][i]["idnumber"])
				
			data["kakus"][i]["status"] = status			
			#print data["kakus"][i]["status"]
			#print "------"	
		
		jsonFile = open("/home/pi/OperatingSystems_Linux_mini_project/kakuData.json","w+")
		jsonFile.write(json.dumps(data));
		jsonFile.close();


#getLuxValue()
#updateKakuStatus(False);

while True:
	value = RCtime(4)
	luxValue = getLuxValue()
	print value		
	print "---------"
	
	if(value < int(luxValue)):
		updateKakuStatus(False);
	else:
		updateKakuStatus(True);
	
	time.sleep(3)
	
	
	
	

