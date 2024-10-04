const express = require('express');
const app = express();
const sqlite3 = require('sqlite3').verbose();
const db = new sqlite3.Database(':memory:');

app.use(express.json());

// 데이터베이스 초기화
db.serialize(() => {
    db.run("CREATE TABLE posts (id INTEGER PRIMARY KEY, title TEXT, content TEXT, author TEXT, updated_at TEXT)");
});

// 게시글 추가
app.post('/posts', (req, res) => {
    const { title, content, author } = req.body;
    const updatedAt = new Date().toISOString();
    db.run("INSERT INTO posts (title, content, author, updated_at) VALUES (?, ?, ?, ?)", [title, content, author, updatedAt], function(err) {
        if (err) {
            return res.status(500).json({ message: "Error adding post" });
        }
        res.status(201).json({ id: this.lastID });
    });
});

// 게시글 가져오기
app.get('/posts/:id', (req, res) => {
    db.get("SELECT * FROM posts WHERE id = ?", [req.params.id], (err, row) => {
        if (err || !row) {
            return res.status(404).json({ message: "Post not found" });
        }
        res.json(row);
    });
});

// 게시글 수정
app.put('/posts/:id', (req, res) => {
    const { title, content, author } = req.body;
    const updatedAt = new Date().toISOString();
    db.run("UPDATE posts SET title = ?, content = ?, author = ?, updated_at = ? WHERE id = ?", [title, content, author, updatedAt, req.params.id], function(err) {
        if (err || this.changes === 0) {
            return res.status(404).json({ message: "Post not found or not updated" });
        }
        res.json({ message: "Post updated" });
    });
});

// 게시글 삭제
app.delete('/posts/:id', (req, res) => {
    db.run("DELETE FROM posts WHERE id = ?", [req.params.id], function(err) {
        if (err || this.changes === 0) {
            return res.status(404).json({ message: "Post not found or not deleted" });
        }
        res.json({ message: "Post deleted" });
    });
});

app.listen(3001, () => {
    console.log('Post service running on port 3001');
});
