<?
    require_once "dbDetails.php"
    // �����ͺ��̽� ���� ���ڿ�. (db��ġ, ���� �̸�, ��й�ȣ)
	$conn = mysqli_connect(HOST, USER, PASS);
		if(mysqli_connect_errno()){
		echo "���� ����";
	}
	mysqli_set_charset($conn, "utf8");
	mysqli_select_db($conn, DB);
 
   // ���� ����
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
