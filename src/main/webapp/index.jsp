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
</style>
</head>
<body>
<div id="header">
<center>
<h1>BCA GPA Calculator</h1>
</center>
</div>
<div id="content">
<center>
<br><br><br><br><br><br>
<center>${error}</center>
<fieldset>

    <form name="login" action="/gpa" method="POST">
    	<input type="text" size=“25” placeholder="PS Username" name="username"><br>
    	<input type="password" size=“25” placeholder="PS Password" name="password"><br>
    	<input type="submit" value="Login">
</fieldset>
	</form>
</center>
</div>
<div id="footer">
<center>
<p>Developed by Kenneth Rhee</p>
</center>
</div>

</body>
</html>