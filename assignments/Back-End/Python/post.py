from flask import Flask, request, jsonify
import sqlite3
from datetime import datetime

app = Flask(__name__)

# 데이터베이스 초기화
def init_db():
    conn = sqlite3.connect('posts.db')
    cursor = conn.cursor()
    cursor.execute('''CREATE TABLE IF NOT EXISTS posts
                      (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, content TEXT, author TEXT, updated_at TEXT)''')
    conn.commit()
    conn.close()

# 게시글 추가
@app.route('/posts', methods=['POST'])
def add_post():
    data = request.get_json()
    conn = sqlite3.connect('posts.db')
    cursor = conn.cursor()
    cursor.execute("INSERT INTO posts (title, content, author, updated_at) VALUES (?, ?, ?, ?)",
                   (data['title'], data['content'], data['author'], datetime.now()))
    conn.commit()
    conn.close()
    return jsonify({"message": "Post added"}), 201

# 게시글 가져오기
@app.route('/posts/<int:post_id>', methods=['GET'])
def get_post(post_id):
    conn = sqlite3.connect('posts.db')
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM posts WHERE id = ?", (post_id,))
    post = cursor.fetchone()
    conn.close()
    if post:
        return jsonify({"id": post[0], "title": post[1], "content": post[2], "author": post[3], "updated_at": post[4]})
    else:
        return jsonify({"message": "Post not found"}), 404

# 게시글 수정
@app.route('/posts/<int:post_id>', methods=['PUT'])
def update_post(post_id):
    data = request.get_json()
    conn = sqlite3.connect('posts.db')
    cursor = conn.cursor()
    cursor.execute("UPDATE posts SET title = ?, content = ?, author = ?, updated_at = ? WHERE id = ?",
                   (data['title'], data['content'], data['author'], datetime.now(), post_id))
    conn.commit()
    conn.close()
    return jsonify({"message": "Post updated"}), 200

# 게시글 삭제
@app.route('/posts/<int:post_id>', methods=['DELETE'])
def delete_post(post_id):
    conn = sqlite3.connect('posts.db')
    cursor = conn.cursor()
    cursor.execute("DELETE FROM posts WHERE id = ?", (post_id,))
    conn.commit()
    conn.close()
    return jsonify({"message": "Post deleted"}), 200

if __name__ == '__main__':
    init_db()
    app.run(debug=True)
