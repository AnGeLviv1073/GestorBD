from flask import Flask, request, jsonify
import mysql.connector
from flask_cors import CORS

app = Flask(__name__)
CORS(app)  # Permitir peticiones desde cualquier origen

# ruta para verificar conexion y mostrar tablas
@app.route('/conectar', methods=['POST'])
def conectar():
    data = request.json
    try:
        conn = mysql.connector.connect(
            host='127.0.0.1',  # Usa localhost o IP local según red
            user=data['usuario'],
            password=data['password'],
            database=data['database']
        )
        cursor = conn.cursor()
        cursor.execute("SHOW TABLES")
        tablas = [fila[0] for fila in cursor.fetchall()]
        return jsonify({"status": "ok", "tablas": tablas})
    except Exception as e:
        print("ERROR en /conectar:", e)
        return jsonify({"status": "error", "mensaje": str(e)})

# Ruta para mostrar registros de una tabla específica
@app.route('/tabla_datos', methods=['POST'])
def tabla_datos():
    data = request.json
    try:
        conn = mysql.connector.connect(
            host='127.0.0.1',
            user=data['usuario'],
            password=data['password'],
            database=data['database']
        )
        cursor = conn.cursor()
        cursor.execute(f"SELECT * FROM `{data['tabla']}`")
        filas = cursor.fetchall()
        return jsonify({"filas": filas})
    except Exception as e:
        print("ERROR en /tabla_datos:", e)
        return jsonify({"error": str(e)})

# Ruta para ejecutar una consulta personalizada
@app.route('/consulta', methods=['POST'])
def ejecutar_consulta():
    data = request.json
    try:
        conn = mysql.connector.connect(
            host='127.0.0.1',
            user=data['usuario'],
            password=data['password'],
            database=data['database']
        )
        cursor = conn.cursor()
        cursor.execute(data['consulta'])
        filas = cursor.fetchall()
        return jsonify({"filas": filas})
    except Exception as e:
        print("ERROR en /consulta:", e)
        return jsonify({"error": str(e)})

# Iniciar el servidor Flask
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
