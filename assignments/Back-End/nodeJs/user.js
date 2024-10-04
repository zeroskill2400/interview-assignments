const express = require('express');
const app = express();
const sqlite3 = require('sqlite3').verbose();
const db = new sqlite3.Database(':memory:');

app.use(express.json());

// 데이터베이스 초기화
db.serialize(() => {
    db.run("CREATE TABLE users (id INTEGER PRIMARY KEY, name TEXT, gender TEXT, age INTEGER, phone TEXT)");
});

// 사용자 추가
app.post('/users', (req, res) => {
    const { name, gender, age, phone } = req.body;
    db.run("INSERT INTO users (name, gender, age, phone) VALUES (?, ?, ?, ?)", [name, gender, age, phone], function(err) {
        if (err) {
            return res.status(500).json({ message: "Error inserting user" });
        }
        res.status(201).json({ id: this.lastID });
    });
});

// 사용자 정보 가져오기
app.get('/users/:id', (req, res) => {
    db.get("SELECT * FROM users WHERE id = ?", [req.params.id], (err, row) => {
        if (err || !row) {
            return res.status(404).json({ message: "User not found" });
        }
        res.json(row);
    });
});

// 사용자 정보 업데이트
app.put('/users/:id', (req, res) => {
    const { name, gender, age, phone } = req.body;
    db.run("UPDATE users SET name = ?, gender = ?, age = ?, phone = ? WHERE id = ?", [name, gender, age, phone, req.params.id], function(err) {
        if (err || this.changes === 0) {
            return res.status(404).json({ message: "User not found or not updated" });
        }
        res.json({ message: "User updated" });
    });
});

// 사용자 삭제
app.delete('/users/:id', (req, res) => {
    db.run("DELETE FROM users WHERE id = ?", [req.params.id], function(err) {
        if (err || this.changes === 0) {
            return res.status(404).json({ message: "User not found or not deleted" });
        }
        res.json({ message: "User deleted" });
    });
});

app.listen(3000, () => {
    console.log('User service running on port 3000');
});
