<?php
   require_once "dbDetails.php";

   $conn = mysqli_connect(HOST, USER, PASS);

   if(mysqli_connect_errno()){
      echo "접속 실패";
   }

   mysqli_set_charset($conn, "utf8");
   mysqli_select_db($conn, DB);

   //$userId = $_POST['userId'];

   // 연결된 데이터베이스에 위에서 작성한 쿼리를 전송하고 
   // 결과값을 result 변수에 저장
   $query = "SELECT team.team_code,team.team_name, count(*) as members
   from team , member
   where team.team_code = member.team_code
   group by member.team_code;";
   
   $res = mysqli_query($conn, $query);
   
   $rows = array();
   $result = array();
   
   while($row = mysqli_fetch_array($res)) {
      // [" "] 안의 문자는 안드로이드에서 사용할 부분
      
      $rows["team_code"] = $row[0];
      $rows["team_name"] = $row[1];
    //  $rows["position"] = $row[2];
     // $rows["permission"] = $row[3];
     //$rows["member"] = "memeber : ";
     $rows["members"] = $row[2];
     //print_r($rows);
     //$rows["members"] = 1;
      // mysqli_num_rows($query);

      array_push($result, $rows);
   }
   // JSON Encode를 황용하여 result 배열 인코딩
   echo json_encode(array("result"=>$result),JSON_UNESCAPED_UNICODE);
   mysqli_close($conn);
?>
