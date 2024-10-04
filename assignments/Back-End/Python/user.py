from flask import Flask, request, jsonify
import sqlite3

app = Flask(__name__)

# 데이터베이스 초기화
def init_db():
    conn = sqlite3.connect('users.db')
    cursor = conn.cursor()
    cursor.execute('''CREATE TABLE IF NOT EXISTS users
                      (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, gender TEXT, age INTEGER, phone TEXT)''')
    conn.commit()
    conn.close()

# 사용자 추가
@app.route('/users', methods=['POST'])
def add_user():
    data = request.get_json()
    conn = sqlite3.connect('users.db')
    cursor = conn.cursor()
    cursor.execute("INSERT INTO users (name, gender, age, phone) VALUES (?, ?, ?, ?)",
                   (data['name'], data['gender'], data['age'], data['phone']))
    conn.commit()
    conn.close()
    return jsonify({"message": "User added"}), 201

# 사용자 정보 가져오기
@app.route('/users/<int:user_id>', methods=['GET'])
def get_user(user_id):
    conn = sqlite3.connect('users.db')
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM users WHERE id = ?", (user_id,))
    user = cursor.fetchone()
    conn.close()
    if user:
        return jsonify({"id": user[0], "name": user[1], "gender": user[2], "age": user[3], "phone": user[4]})
    else:
        return jsonify({"message": "User not found"}), 404

# 사용자 정보 업데이트
@app.route('/users/<int:user_id>', methods=['PUT'])
def update_user(user_id):
    data = request.get_json()
    conn = sqlite3.connect('users.db')
    cursor = conn.cursor()
    cursor.execute("UPDATE users SET name = ?, gender = ?, age = ?, phone = ? WHERE id = ?",
                   (data['name'], data['gender'], data['age'], data['phone'], user_id))
    conn.commit()
    conn.close()
    return jsonify({"message": "User updated"}), 200

# 사용자 삭제
@app.route('/users/<int:user_id>', methods=['DELETE'])
def delete_user(user_id):
    conn = sqlite3.connect('users.db')
    cursor = conn.cursor()
    cursor.execute("DELETE FROM users WHERE id = ?", (user_id,))
    conn.commit()
    conn.close()
    return jsonify({"message": "User deleted"}), 200

if __name__ == '__main__':
    init_db()
    app.run(debug=True)
