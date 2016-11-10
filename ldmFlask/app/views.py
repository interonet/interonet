from app import app
from flask import request
import json
# import multiprocessing
# import time
# from topology import initMininetTopology
# from vertifyTopo import Vertify


@app.route('/')
@app.route('/index')
def index():
	return "Hello World"

# @app.route('/testPost', methods=['GET','POST'])
# def testPost():
# 	if request.method == 'POST':
# 		topo = json.loads(request.form['data'])
# 		if Vertify(topo).vertify():
# 			p = multiprocessing.Process(target=initMininetTopology,args=(topo,))
# 			p.start()
# 			return "success"
# 		return 'failure'


@app.route('/testGDM', methods=['GET','POST'])
def testGDM():
	if request.method == 'POST':
		print "test"
		topo = request.form['data']
		print topo
		return "success"

