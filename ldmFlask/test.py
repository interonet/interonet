# import re
# topo = {'switches': ['s1','s2','s3'], 'hosts':['h0','h1','h2','h3'], 'topology':[{'source':'h2', 'target':'s2'}, {'source':'h3', 'target':'s2'}, {'source':'s1', 'target':'s2'}, {'source':'h4', 'target':'s3'}, {'source':'h5', 'target':'s3'}, {'source':'s3', 'target':'s1'}, {'source':'s2', 'target':'s1'}, {'source':'s2', 'target':'s3'} ], 'controller': {'ip': '202.117.15.123', 'port': '6633'}, 'mapPort':{'eth0':'s1'} }
# class Vertify(object):
# 	def __init__(self,topo):
# 		self.__topo = topo
# 		self.__keyList = ['switches','hosts','topology','controller','mapPort']
# 		self.__ctrlList = ['ip','port']
# 		self.__topologyList = ['source','target']

# 	def vertify(self):
# 		return self.vertifyKey(self.__topo, self.__keyList) and self.vertifyNodes() and self.vertifyCtrl() and self.vertifyTopology()

# 	def vertifyNodes(self):
# 		switches = self.__topo['switches']
# 		hosts = self.__topo['hosts']
# 		if not (isinstance(switches,list) and isinstance(hosts, list)) :
# 			return False
# 		for s in switches:
# 			reSwitch = ur'^s\d+$'
# 			if re.match(reSwitch,s) is None:
# 				return False
# 		for h in hosts:
# 			reHost = ur'^h\d+$'
# 			if re.match(reHost,h) is None:
# 				return False
# 		return True

# 	def vertifyCtrl(self):
# 		# vertify 'controllerConf' key is list
# 		#wait to be expand
# 		controller =  self.__topo['controller']
# 		if not self.vertifyKey(controller,self.__ctrlList):
# 			return False
# 		ip = controller['ip']
# 		port = controller['port']
# 		reIP = ur'^(\d{1,3}.){3}\d{1,3}$'
# 		rePort = ur'^\d+$'
# 		return re.match(reIP,ip) and re.match(rePort,port)

# 	def vertifyTopology(self):
# 		#  topology
# 		topology = self.__topo[self.__keyList[2]]
# 		if not isinstance(topology, list):
# 			return False
# 		for key in topology:
# 			if not self.vertifyKey(key, self.__topologyList):
# 				return False
# 		return True

# 	def vertifyKey(self, oneDict, oneList):
# 		#vertify the key is instance
# 		for key in oneList:
# 			if key not in oneDict:
# 				return False
# 		return True


# a = Vertify(topo)
# print a.vertify()


# # reIP = ur'^(\d{1,3}.){3}\d{1,3}$'
# # a = '202.117.1580.'
# # print re.match(reIP,a) is None 

# import logging
# import logging.config

# logging.config.fileConfig("./logger.conf")
# logger = logging.getLogger("root")

# logger.debug('This is debug message')
# logger.info('This is info message')
# logger.warning('This is warning message')



# import multiprocessing
# import time

# def worker(interval):
# 	print '1'
# 	time.sleep(5)
# 	print '2'
# def test():
# 	p = multiprocessing.Process(target=worker,args=(3,))
# 	p.start()
# 	return '3'

# a = test();
# print a 

a = '10M'
try:
	b = int(a)
	print b
except ValueError:
	print "error"







