task_management_app/
│
├── static/
│   ├── style.css
│   └── script.js
│
├── templates/
│   ├── index.html
│   └── task.html
│
├── app.py
└── tasks.db






from flask import Flask, render_template, request, redirect, url_for
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///tasks.db'
db = SQLAlchemy(app)

class Task(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(100), nullable=False)
    description = db.Column(db.String(200))
    done = db.Column(db.Boolean, default=False)

@app.route('/')
def index():
    tasks = Task.query.all()
    return render_template('index.html', tasks=tasks)

@app.route('/create', methods=['POST'])
def create():
    title = request.form['title']
    description = request.form['description']
    new_task = Task(title=title, description=description)
    db.session.add(new_task)
    db.session.commit()
    return redirect(url_for('index'))

@app.route('/<int:id>/update', methods=['POST'])
def update(id):
    task = Task.query.get_or_404(id)
    task.title = request.form['title']
    task.description = request.form['description']
    task.done = 'done' in request.form
    db.session.commit()
    return redirect(url_for('index'))

@app.route('/<int:id>/delete')
def delete(id):
    task = Task.query.get_or_404(id)
    db.session.delete(task)
    db.session.commit()
    return redirect(url_for('index'))

if __name__ == '__main__':
    db.create_all()
    app.run(debug=True)






<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Task Management App</title>
    <link rel="stylesheet" href="{{ url_for('static', filename='style.css') }}">
</head>
<body>
    <h1>Task Management App</h1>
    <ul>
        {% for task in tasks %}
            <li>
                <form action="{{ url_for('update', id=task.id) }}" method="post">
                    <input type="text" name="title" value="{{ task.title }}">
                    <input type="text" name="description" value="{{ task.description }}">
                    <input type="checkbox" name="done" {% if task.done %} cbody {
    font-family: Arial, sans-serif;
}

form {
    margin-bottom: 10px;
}

ul {
    list-style-type: none;
    padding: 0;
}

li {
    margin-bottom: 10px;
}

input[type="checkbox"] {
    margin-right: 5px;
}hecked {% endif %}>
                    <button type="submit">Update</button>
                </form>
                <a href="{{ url_for('delete', id=task.id) }}">Delete</a>
            </li>
        {% endfor %}
    </ul>
    <form action="/create" method="post">
        <input type="text" name="title" placeholder="Title">
        <input type="text" name="description" placeholder="Description">
        <button type="submit">Create Task</button>
    </form>
</body>
</html>
