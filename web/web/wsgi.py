"""
WSGI config for web project.

It exposes the WSGI callable as a module-level variable named ``application``.

For more information on this file, see
https://docs.djangoproject.com/en/1.9/howto/deployment/wsgi/
"""

import os
from multiprocessing import Process
from django.conf import settings

from django.core.wsgi import get_wsgi_application

os.environ.setdefault("DJANGO_SETTINGS_MODULE", "web.settings")

application = get_wsgi_application()

def worker():
    cmd = u'python %s/websockify.py --web=%s --target-config=%s %s' % (settings.WEBSOCKIFY_PATH, settings.WEB_PATH, settings.TARGET_PATH, settings.WEBSOCKIFY_PORT)
    print(cmd)
    os.system(cmd)

def start_websockify():
    print('start websockify ..')
    t = Process(target=worker, args=())
    t.start()
    print('websockify started')
start_websockify()
