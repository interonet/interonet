"""web URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.9/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.conf.urls import url, include
    2. Add a URL to urlpatterns:  url(r'^blog/', include('blog.urls'))
"""
from django.conf.urls import url
from django.contrib import admin
from statistic import views as statistic_views
from userManager import views as userManager_views
from service import views as service_views
urlpatterns = [
    #jump to login page
    url(r'login\.html', userManager_views.loginPage, name='loginPage'),
    # verify username and password
    url(r'service$', userManager_views.login, name='login'),
    # jump to register page
    url(r'register\.html', userManager_views.registerPage, name='registerPage'),
    # jump to user page(to be extend)
    url(r'user\.html', userManager_views.user, name='user'),
    # logout
    url(r'logout', userManager_views.logout, name='logout'),
    # user order

    url(r'test', service_views.test, name='test'),

    url(r'service/submit$', service_views.submit, name='submit'),
    url(r'service/checkSlice/(\w{8}\-\w{4}\-\w{4}\-\w{4}\-\w{12}$)', service_views.checkSlice, name='checkSlice'),
    url(r'service/useSlice/(\w{8}\-\w{4}\-\w{4}\-\w{4}\-\w{12}$)', service_views.useSlice, name='useSlice'),
    # service (/service/home service/create /service/check)
    url(r'service/([A-Za-z]+$)', service_views.service, name='service'),
    # home page
    url(r'^$', statistic_views.home, name='home'),
    url(r'^admin/', admin.site.urls),
]
