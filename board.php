<?php

	require_once "dbDetails.php";

	$conn = mysqli_connect(HOST, USER, PASS);
	$team_code = isset($_POST['team_code']) ? $_POST['team_code'] : '';
	$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
	if(mysqli_connect_errno()){
		echo "접속 실패";
	}
	//$team_code = 1;
	mysqli_set_charset($conn, "utf8");
	mysqli_select_db($conn, DB);

	//$userId = $_POST['userId'];

	// 연결된 데이터베이스에 위에서 작성한 쿼리를 전송하고
	// 결과값을 result 변수에 저장
	$query = "SELECT team.team_name,board.board_code, board.board_name
	from team,board
	where team.team_code = board.team_code and team.team_code = $team_code";
	$res = mysqli_query($conn, $query);

	$rows = array();
	$results = array();

	while($row = mysqli_fetch_array($res)) {
		// [" "] 안의 문자는 안드로이드에서 사용할 부분
		$rows["team_name"] = $row[0];
		$rows["board_code"] = $row[1];
		$rows["board_name"] = $row[2];
		//$rows["team_code"] = $row[2];
		// $rows에 들어간 데이터들을 $result 배열에 add
		array_push($results, $rows);
	}
	// JSON Encode를 활용하여 result 배열 인코딩
	echo json_encode(array("results"=>$results), JSON_UNESCAPED_UNICODE);
	mysqli_close($conn);
?>
