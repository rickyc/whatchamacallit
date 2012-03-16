<meta name='viewport' content='width=320, user-scalable=yes'>
<link type='text/css' rel='stylesheet' href='resources/css/style.css'/>

<?php
	$html = file_get_contents('http://www.nyu.edu/registrar/calendars/academic-calendar.html');

	// error 404 check
	if(stripos($html, '<table class="regCalendar tRollover print100">') == null) {
		print '<h1>An error has occurred.</h1>';
		exit();
	}

	preg_match_all('!<table class="regCalendar tRollover print100">(.*?)</table>!ms', $html, $hidden);
	
	foreach($hidden[0] as $html)
		print str_replace('../', 'http://www.nyu.edu/registrar/', $html);
?>
