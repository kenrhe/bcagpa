<!doctype html>

<html>
<head>
<meta charset="utf-8">
<title>BCA GPA Calculator By Kenneth Rhee</title>
<style type="text/css">
	#header,#footer,#content { position:absolute; right:0;left:0; }

	#header{
    	height:100px; top:0;
	font-family:verdana;
	}
	#footer{
    	height:50px; bottom:0;
	color:white;
    	background:black;
	font-family:verdana;
	}
	#content{
    	top:80px;
    	bottom:50px;
    	background:#EEEEEE;
	
	}
	#grade {
		font-family:verdana;
		background:white;
		float:left;
		width:24.8%;
		border:1px solid gray;
		display:block;
	}
</style>
</head>
<body>
<div id="header">
<center>
<h1>BCA GPA Calculator</h1>
</center>
</div>
<div id="content">
<p>&nbsp;&nbsp;<a href="/">> Logout</a></p>
<br><br><br><br>
<fieldset>
<div id="grade" style="text-align: center">
	<b>Trimester 1 GPA</b> <br>${tri1GPA}
</div>
<div id="grade" style="text-align: center">
	<b>Trimester 2 GPA</b> <br>${tri2GPA}
</div>
<div id="grade" style="text-align: center">
	<b>Trimester 3 GPA</b> <br>${tri3GPA}
</div>

<div id="grade" style="text-align: center">
	<b>Year GPA</b> <br>${yearGPA}
</div>
</fieldset>
	</form>
</div>
<div id="footer">
<center>
<p>Developed by Kenneth Rhee</p>
</center>
</div>

</body>
</html>