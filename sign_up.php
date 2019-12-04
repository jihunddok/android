<?
    require_once "dbDetails.php"
    // 데이터베이스 접속 문자열. (db위치, 유저 이름, 비밀번호)
	$conn = mysqli_connect(HOST, USER, PASS);
		if(mysqli_connect_errno()){
		echo "접속 실패";
	}
	mysqli_set_charset($conn, "utf8");
	mysqli_select_db($conn, DB);
 
   // 세션 시작
   session_start();
 
   $id = $_POST[u_id];
   $pw = $_POST[u_pw]; 
   $name = $_POST[u_name]
 
   $query = "INSERT INTO USERS(m_id, m_pw, m_name) VALUES('$id', '$pw','$name')";
 
   $res = mysqli_query($conn, $query);
 
   if(!$res)
        die("mysql query error");
   else
        echo "insert success"
 
?>
