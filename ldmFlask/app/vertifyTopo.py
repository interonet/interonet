import re
import os
import logging
import logging.config

logging.config.fileConfig("conf/logger.conf")
logger = logging.getLogger("root")

# topo = {'switches': ['s1','s2','s3'], 'hosts':['h0','h1','h2','h3'], 'topology':[{'source':'h2', 'target':'s2'}, {'source':'h3', 'target':'s2'}, {'source':'s1', 'target':'s2'}, {'source':'h4', 'target':'s3'}, {'source':'h5', 'target':'s3'}, {'source':'s3', 'target':'s1'}, {'source':'s2', 'target':'s1'}, {'source':'s2', 'target':'s3'} ], 'controller': {'ip': '202.117.15.123', 'port': '6633'}, 'mapPort':{'eth0':'s1'} }
class Vertify(object):
	def __init__(self,topo):
		self.__topo = topo
		self.__keyList = ['switches','hosts','topology','controller','mapPort']
		self.__ctrlList = ['ip','port']
		self.__topologyList = ['source','target']

	def vertify(self):
		logging.info("start to vertify the post string ...")
		return self.vertifyKey(self.__topo, self.__keyList) and self.vertifyNodes() and self.vertifyCtrl() and self.vertifyTopology()

	def vertifyNodes(self):
		logger.info("start to vertify the node including switch and host ...")
		switches = self.__topo['switches']
		hosts = self.__topo['hosts']
		if not (isinstance(switches,list) and isinstance(hosts, list)) :
			logger.error("the value of nodes is not list")
			return False
		logger.info("the value of nodes is list")
		for s in switches:
			reSwitch = ur'^s\d+$'
			if re.match(reSwitch,s) is None:
				logger.error("switch is illegal")
				return False
		for h in hosts:
			reHost = ur'^h\d+$'
			if re.match(reHost,h) is None:
				logger.error("host is illegal")
				return False
		logger.info("node is legal")
		return True

	def vertifyCtrl(self):
		# vertify 'controllerConf' key is list
		logger.info("start to vertify the controller ...")
		controller =  self.__topo['controller']
		if not self.vertifyKey(controller,self.__ctrlList):
			logger.error("the key of controller is illegal")
			return False
		logger.info('the key of controller is legal')
		ip = controller['ip']
		port = controller['port']
		reIP = ur'^(\d{1,3}.){3}\d{1,3}$'
		rePort = ur'^\d+$'
		if re.match(reIP,ip) and re.match(rePort,port):
			logger.info("the value of controller is legal")
			return True
		logger.error("the value of controller is illegal")
		return False


	def vertifyTopology(self):
		#  topology
		topology = self.__topo[self.__keyList[2]]
		if not isinstance(topology, list):
			logger.error("the value of topology is not list")
			return False
		for key in topology:
			if not self.vertifyKey(key, self.__topologyList):
				logger.error("the key of topology is illegal")
				return False
			else:
				reTopology = ur'^[sh]\d+$'
				if not (re.match(reTopology,key['source']) and re.match(reTopology, key['target'])):
					logger.error("the value of topology is illegal")
					logger.error('The error value is source: ' + key['source'] + ' and target: ' + key['target'] )
					return False
		logger.info("topology is legal")
		return True

	def vertifyKey(self, oneDict, oneList):
		#vertify the key is instance
		for key in oneList:
			if key not in oneDict:
				return False
		return True

