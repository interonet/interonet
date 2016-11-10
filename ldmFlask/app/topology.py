from mininet.net import Mininet
from mininet.topo import Topo
from mininet.log import lg, setLogLevel
from mininet.cli import CLI
from mininet.node import RemoteController
import commands
from ast import literal_eval

class vmTopo(Topo):
  def __init__(self, topo):
    super(vmTopo, self).__init__()
    for s in topo['switches']:
    	self.switches['s'] = self.addSwitch(s)
    for h in topo['hosts']:
    	self.hosts['h'] = self.addHost(h)
    for count in xrange(0, len(topo['topology']))
    	self.addLink(topo['topology'][count]['source'],topo['topology'][count]['target'])


def initMininetTopology(topo):
	vmMininetTopology = vmTopo(topo)
	ip = topo['controllerConf']['ip']
	port = topo['controllerConf']['port']
	c = RemoteController('c', ip=ip, port=port)
   	net = Mininet(topo=vmMininetTopology, autoSetMacs=True, xterms=False, controller=c)
    net.start()
    CLI(net)



