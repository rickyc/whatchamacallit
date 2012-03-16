<?php

	// helper parser function
	function substring($string,$r1,$r2,$include) {
		$r1_loc = strpos($string,$r1);
		$r2_loc = strpos($string,$r2);
		$n_loc = $include ? $r1_loc + strlen($r1) : $r1_loc;
		$n_len = $include ? $r2_loc - $r1_loc - strlen($r1) : $r2_loc - $r1_loc + strlen($r2);
		return substr($string,$n_loc,$n_len);
	}

	// $time is in format 12:00pm
	function getTime($time) {
		$meridiem = substr($time,5,2);
		$hour = intval(substr($time,0,2));
		if($hour == 12 && $meridiem == "am") $hour = 0;
		else if($hour == 12 && $meridiem == "pm") $hour = 12;
		else if($meridiem == "pm") $hour += 12;	

		return strtotime($hour . ":" . substr($time,3,2));
	}

?>
