from django.shortcuts import render
from django.http import HttpResponse
from django.conf import settings
import jsonrpclib

# go to login page
def loginPage(request):
    return render(request, 'login.html')

def registerPage(request):
    return render(request, 'login.html')

def logout(request):
    try:
        del request.session['username']
    except KeyError:
        pass
    return render(request, 'home.html')

def user(request):
    return render(request, 'login.html')


def login(request):
    if request.method == 'POST':
        username = request.POST['username'].strip()
        password = request.POST['password'].strip()
        server = jsonrpclib.Server(settings.JSONRPC)
        result = server.getToken(username, password)
        if result == 'Failed':
            return render(request, 'login.html', {'Failed': 'This is something wrong with the username or password, please input again'})
        else:
            request.session['username'] = username
            request.session['password'] = password
            return render(request, 'service.html')
    elif request.session.get('username', False):
        return render(request, 'service.html')
    else:
        return render(request, 'login.html')


