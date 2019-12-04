<?php

    $con = mysqli_connect('113.198.235.225', 'root', '1234', 'projecgt');


	myslqi_set_charset($con,"utf-8");

	if(mysqli_connect_errno($con)){
		echo "DB connect fail";
	}

	$userid = $_POST['id'];
	$username = $_POST['name'];
	$userPassword = $_POST['password'];
	
	$result = mysqli_query($con,"insert into member(m_name,m_id,m_pw) values ('$username','$userid','$userPassword')");

	if($result){
		echo 'success';
	}
	else {
		echo 'fail';
	}

	mysqli_close($con);
}

?>