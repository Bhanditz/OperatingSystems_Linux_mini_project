var express = require('express');
var app = express();
var fs = require("fs");

var errorMessage = {"Error":{"Message":"ID is not known"}};
var errorMessage1 = {"Error":{"Message":"status is not a true of false"}};

app.get('/addKaku/:id/:id_letter/:id_number/:location', function (req, res) {
	
	var id = req.params.id;
	var letter = req.params.id_letter;
	var number = req.params.id_number;
	var loc = req.params.location;
	
	console.log(id + " " + letter + " " + number + " "+loc);
	
   // First read existing users.
    fs.readFile( __dirname + "/" + "kakuData.json", 'utf8', function (err, data) {	   
       if(err)
	   {
		   console.log(err);
	   }
	   
	   data = JSON.parse( data );
	   var found = false;
	   
	   for(var i =0; i<data["kakus"].length; i++)
	   {
		   if(data["kakus"][i]["id"] == id)
		   {
			   found = true;
			   break;
		   }
	   }
	   
	   console.log("KAKU " + data["Kaku"+id]);
	   
	   if(!found && !isNaN(id) && !isNaN(number))
	   {
		   console.log("Adding data" + id);
		   
			data["kakus"][data["kakus"].length] = {	"id":id, "idletter":letter,"idnumber":number,"status":"false","location":loc};
			console.log( data );
			data = JSON.stringify(data);
			fs.writeFile(__dirname + "/" + "kakuData.json", data, function(err){
			   if(err)
			   {
					console.log(err); 
			   }		   
		   });
	   }
	   else{data = JSON.stringify(data);}   
       res.end( data );
    });
})

app.get('/updateKaku/:id/:newLetter/:newNumber/:newStatus/:newLocation',function (req, res){
	var id = req.params.id;
	var newL = req.params.newLetter;
	var newN = req.params.newNumber;
	var newS = req.params.newStatus;
	var newLoc = req.params.newLocation;
	
	console.log(id +" "+newL+" "+newN+" "+newS+" "+newL);
	
	fs.readFile( __dirname + "/" + "kakuData.json", 'utf8', function (err, data){
		if(err)
		{
		   console.log(err);
		}
		data = JSON.parse(data);
		
	   var found = false,correctStatus;
	   
	   var index = -1;
	   
	   for(var i =0; i<data["kakus"].length; i++)
	   {
		   if(data["kakus"][i]["id"] == id)
		   {
			   found = true;
			   index = i;
			   break;
		   }
	   }
		
		correctStatus = newS == "true" || newS == "false";
		
		if(found && !isNaN(id) && !isNaN(newN) && correctStatus)
		{
			console.log("update kaku: "+id);
			data["kakus"][index]["idletter"] = newL;
			data["kakus"][index]["idnumber"] = newN;
			data["kakus"][index]["status"] = newS;
			data["kakus"][index]["location"] = newLoc;
			console.log("upated kaku: "+id)
			
			data = JSON.stringify(data);
			fs.writeFile(__dirname + "/" + "kakuData.json", data, function(err){
			   if(err)
			   {
					console.log(err); 
			   }		   
		   });
		}
		else
		{
			if(!found)
			{
				data = errorMessage;
			}
			else if(!found)
			{
				data = errorMessage1
			}	
			data = JSON.stringify(data);			
		}
		
		console.log(data);
		
		res.end(data);
	});
})	

app.get('/getKakus', function (req, res) {
   fs.readFile( __dirname + "/" + "kakuData.json", 'utf8', function (err, data) {
       console.log( data );
       res.end( data );
   });
})

app.get('/getKaku/:id', function (req, res) 
{
	var id = req.params.id;
	
	fs.readFile(__dirname + "/" +"kakuData.json", 'utf8', function(err,data)
	{
		data = JSON.parse(data);
		
		var found = false;
	   
	   for(var i =0; i<data["kakus"].length; i++)
	   {
		   if(data["kakus"][i]["id"] == id)
		   {
			   data = data["kakus"][i];
			   found = true;
			   break;
		   }
	   }
	   
	   if(!found)
	   {
		   data = errorMessage;
	   }
		
		console.log(data);
		data = JSON.stringify(data);
		res.end(data);
	})
})

var server = app.listen(8081, function () {

  var host = server.address().address
  var port = server.address().port

  console.log("Example app listening at http://%s:%s", host, port)

})