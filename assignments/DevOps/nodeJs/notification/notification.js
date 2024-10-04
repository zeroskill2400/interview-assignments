const express = require('express');
const jwt = require('jsonwebtoken');
const app = express();

app.use(express.json());
const SECRET_KEY = 'supersecretkey';

// 로그인 및 토큰 발급
app.post('/login', (req, res) => {
    const { username, password } = req.body;
    if (username === 'admin' && password === 'password
