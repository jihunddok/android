<?php

    require_once "dbDetails.php";
    $conn = mysqli_connect(HOST, USER, PASS);

    if(mysqli_connect_errno($conn)){
        echo "접속 실패";
    }


    mysqli_set_charset($conn, "utf8");
    mysqli_select_db($conn, DB);

    $userId = $_POST['userId'];
    $query = "select m_id,m_pw from member where m_id = $userId;";
    $res = mysqli_query($conn, $query);
    $rows = array();
    $result = array();

    while($row = mysqli_fetch_array($res)){
        $rows["id"] = $row[0];
        $rows["pass"] = $row[1];

        array_push($result, $rows);
    }
    echo json_encode(array("results"=>$result));
    mysqli_close($conn);
?>
