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
	#notice{
		font: 12px sans-serif;
		margin-top:20%;
		text-align:center;
	}
	.clickme {
		margin-top:1%;
	font:12px sans-serif;
    background-color: #D1D1E0;
    border-radius: 4px;
    color: #666;
    display: block;
    width:9%;
    margin-bottom: 5px;
    padding: 5px 10px;
    margin-left:0.5%;
    text-decoration: none;
	}

	.clickme:hover {
	    text-decoration: underline;
	}

	.box {
			font:15px sans-serif;
	    background-color: #ccc;
	    border-radius: 4px;
	    margin: 5px 0;
	    padding: 5px 10px;
	    margin-left:2%;
	    width: 70%;
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
${error}
<fieldset>

    <form name="login" action="/gpa" method="POST">
    	<input type="text" size=“25” placeholder="PS Username" name="username"><br>
    	<input type="password" size=“25” placeholder="PS Password" name="password"><br>
    	<input type="submit" value="Login">
    </form>
</fieldset>

	<a href="#" class="clickme"><b>About This App</b></a>
	<div class="box">
		Hi! <br />     Welcome to my "BCA GPA Calculator" app developed with a JETTY framework. <br />Here are some common questions that are asked:
		<br />
		<br />
		<b>How accurate is this application?</b>
		&nbsp;&nbsp;&nbsp;
		<br />
		This app is extremely accurate and uses a GPA calculation formula provided by guidance. It does not count classes with tildes (~) (aka non GPA classes such as gym). Every factor has been accounted for. The year GPA displayed will end up being accurate within 0.05 points.
		<br />
		<br />
		<b>I believe there is an error within your app. Where can I report it?</b>
		&nbsp;&nbsp;&nbsp;
		<br />
		If you believe that there is an error within my app, feel free to e-mail me at hwarhe@bergen.org.
		<br />
		<br />
		<b>I have a suggestion, where can I contact you?</b>
		&nbsp;&nbsp;&nbsp;
		<br />
		If you have a suggestion, feel free to message me on facebook or e-mail me at hwarhe@bergen.org.

	</div>
</center>

<div id="notice">
	This app does not save your GPA/password.
</div>
</div>

<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script type="text/javascript">
// Hide all the elements in the DOM that have a class of "box"
$('.box').hide();

// Make sure all the elements with a class of "clickme" are visible and bound
// with a click event to toggle the "box" state
$('.clickme').each(function() {
    $(this).show(0).on('click', function(e) {
        // This is only needed if your using an anchor to target the "box" elements
        e.preventDefault();
        
        // Find the next "box" element in the DOM
        $(this).next('.box').slideToggle('fast');
    });
});
</script>



<div id="footer">
<center>
<p>Developed by Kenneth Rhee</p>
</center>
</div>

</body>
</html>