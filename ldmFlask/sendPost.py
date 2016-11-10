import urllib
import urllib2
import json
# url = 'http://127.0.0.1:5000/testPost'
# req = urllib2.Request(url)
# print req
# res_data = urllib2.urlopen(req)
# res = res_data.read()
# print res
topo = {'switches': ['s1','s2','s3'],
		'hosts':['h0','h1','h2','h3'],
		'topology':[
		    {"source":"h2",
		      "target":"s2"},
		    {"source":"h3",
		      "target":"s2"},
		    {"source":"s1",
		      "target":"s2"},
		    {"source":"h4",
		      "target":"s3"},
		    {"source":"h5",
		      "target":"s3"},
		    {"source":"s3",
		      "target":"s1"},
		    {"source":"s2",
		      "target":"s1"},
		    {"source":"s2",
		      "target":"s3"}
  		],
  		  "controller": {
			    "ip": "202.117.15.133",
			    "port": "6633"
  		},
  		'mapPort':{'eth0':'s1'} 
  		}
data = {'data':json.dumps(topo)}
data_urlencode = urllib.urlencode(data)
requrl = 'http://127.0.0.1:5000/testPost'
print data
print data_urlencode
req = urllib2.Request(url=requrl, data = data_urlencode)
print req
res_data = urllib2.urlopen(req)
res = res_data.read()
print res
