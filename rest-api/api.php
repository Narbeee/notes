<?php
header('Content-Type: application/json');
$conn = new mysqli("localhost", "root", "", "db_notes");

$method = $_SERVER['REQUEST_METHOD'];
if ($method === 'POST') {
    // Logika Simpan (yang sudah berhasil Anda buat)
    $title = $_POST['title'];
    $content = $_POST['content'];
    $sql = "INSERT INTO notes (title, content) VALUES ('$title', '$content')";
    if ($conn->query($sql)) {
        echo json_encode(["message" => "Berhasil"]);
    }
} else if ($method === 'GET') {
    // Logika Ambil Data untuk fungsi getNotes()
    $sql = "SELECT * FROM notes ORDER BY id DESC";
    $result = $conn->query($sql);
    $notes = array();
    while($row = $result->fetch_assoc()) {
        $notes[] = $row;
    }
    // Mengembalikan data dalam bentuk List JSON
    echo json_encode($notes); 
}
?>