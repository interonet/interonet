from django.shortcuts import render
from django.http import JsonResponse
from django.http import HttpResponse
import jsonrpclib
from django.conf import settings
import json
import dateutil.parser as parser
from django.shortcuts import redirect

# Create your views here.

def service(request, state):
    if request.session.get('username', False):
        if state == 'create':
            return render(request, 'create.html')
        elif state == 'check':
            username = request.session['username']
            password = request.session['password']
            server = jsonrpclib.Server(settings.JSONRPC)
            authToken = server.getToken(username, password)
            result = json.loads(server.getSlicePool(authToken))
            info = []
            for i in range(len(result)):
                info.append({'id': result[i]['id'], 'startTime': str(parser.parse(result[i]['beginTime'], ignoretz=True)), 'endTime': str(parser.parse(result[i]['endTime'], ignoretz=True)), 'status': result[i]['status'], 'switchNum': result[i]['switchesNum'], 'hostNum': result[i]['vmsNum']})
            return render(request, 'check.html', {'info': info})
        else:
            return render(request, 'service.html')
    else:
        return render(request, 'login.html')

def submit(request):
    if request.method == 'POST':
        startTime = request.POST['startTime']
        endTime = request.POST['endTime']
        IP = request.POST['IP']
        Port = request.POST['Port']
        switchNum = request.POST['switchNum']
        hostNum = request.POST['hostNum']
        switchConf = request.POST['switchConf']
        topology = request.POST['topology']
        order = dict()
        order['num'] = {'switchesNum': switchNum, 'vmsNum': hostNum}
        order['time'] = {'begin': parser.parse(startTime).isoformat(), 'end': parser.parse(endTime).isoformat()}
        order['topology'] = json.loads(topology)
        order['switchConf'] = json.loads(switchConf)
        order['controllerConf'] = {'ip': IP, 'port': Port}
        order['customSwitchConf'] = {'s0': {'root-fs': 'http://202.117.15.79/ons_bak/backup.tar.xz', 'system-bit': 'http://202.117.15.79/ons_bak/system.bit', 'uImage': 'http://202.117.15.79/ons_bak/uImage', 'device-tree': 'http://202.117.15.79/ons_bak/devicetree.dtb'}}
        username = request.session['username']
        password = request.session['password']
        server = jsonrpclib.Server(settings.JSONRPC)
        authToken = server.getToken(username, password)
        result = server.submitSlice(authToken, json.dumps(order))
        if result == "Failed" or result == "Exception":
            return HttpResponse("Failed")
        else:
            return HttpResponse("Success")

def checkSlice(request, sliceID):
    username = request.session['username']
    password = request.session['password']
    server = jsonrpclib.Server(settings.JSONRPC)
    authToken = server.getToken(username, password)
    result = json.loads(server.getSlice(authToken, sliceID))
    info = dict()
    info['id'] = result['id']
    info['startTime'] = str(parser.parse(result['beginTime'], ignoretz=True))
    info['endTime'] = str(parser.parse(result['endTime'], ignoretz=True))
    info['IP'] = result['controllerIP']
    info['Port'] = result['controllerPort']
    info['switchNum'] = result['switchesNum']
    info['hostNum'] = result['vmsNum']
    info['topology'] = json.dumps(result['topology'])
    return render(request, 'checkSlice.html', {'info': info})

def useSlice(request, sliceID):
    username = request.session['username']
    password = request.session['password']
    server = jsonrpclib.Server(settings.JSONRPC)
    authToken = server.getToken(username, password)
    result = json.loads(server.getSlice(authToken, sliceID))
    info = dict()
    info['id'] = result['id']
    info['startTime'] = str(parser.parse(result['beginTime'], ignoretz=True))
    info['endTime'] = str(parser.parse(result['endTime'], ignoretz=True))
    info['IP'] = result['controllerIP']
    info['Port'] = result['controllerPort']
    info['switchNum'] = result['switchesNum']
    info['hostNum'] = result['vmsNum']
    info['topology'] = json.dumps(result['topology'])
    info['switchMap'] = dict()
    info['hostMap'] = dict()
    info['sockifyIP'] = settings.WEBSOCKIFY_IP
    info['sockifyPort'] = settings.WEBSOCKIFY_PORT
    for switchVirId, switchPhyId in result['userSW2domSW'].items():
        info['switchMap'][switchVirId] = 's' + str(switchPhyId)
    for hostVirId, hostPhyId in result['userVM2domVM'].items():
        info['hostMap'][hostVirId] = 'h' + str(hostPhyId)
    # info['switchMap'] = json.dumps(info['switchMap'])
    # info['hostMap'] = json.dumps(info['hostMap'])
    return render(request, 'useSlice.html', {'info': info})

def deleteSlice(request):
    if request.method == 'POST':
        username = request.session['username']
        password = request.session['password']
        server = jsonrpclib.Server(settings.JSONRPC)
        authToken = server.getToken(username, password)
        sliceID = request.POST['sliceID']
        result = server.tryToTerminateSlice(authToken, sliceID)
        return HttpResponse(result)

def test(request):
    return render(request, 'vnc_auto.html')

