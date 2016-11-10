from app import app
from flask import request
import json
import multiprocessing
import time
from topology import initMininetTopology
from vertifyTopo import Vertify


@app.route('/start/mn', methods=['GET','POST'])
def start_mn():
	if request.method == 'POST':
		topo = json.loads(request.form['data'])
		if Vertify(topo).vertify():
			p = multiprocessing.Process(target=initMininetTopology,args=(topo,))
			p.start()
			return "success"
		return 'failure'


