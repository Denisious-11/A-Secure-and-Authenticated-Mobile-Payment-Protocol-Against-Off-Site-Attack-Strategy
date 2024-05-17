"""backend URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.11/topics/http/urls/
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
from backend_app.views import *

urlpatterns = [
    url(r'^admin/', admin.site.urls),

    ##########
    url(r'^$',show_index),
    url(r'^show_index', show_index, name="show_index"),
    url(r'^logout',logout,name="logout"),
    url(r'^register',register,name="register"),
    url(r'^find_login',find_login,name="find_login"),
    url(r'^add_purchase',add_purchase,name="add_purchase"),
    url(r'^get_purchase_request',get_purchase_request,name="get_purchase_request"),
    url(r'^approve',approve,name="approve"),
    url(r'^get_accepted_purchases',get_accepted_purchases,name="get_accepted_purchases"),
    url(r'^generate_qr_code',generate_qr_code,name="generate_qr_code"),
    url(r'^verify_decode',verify_decode,name="verify_decode"),
    url(r'^get_payment_status',get_payment_status,name="get_payment_status"),
    url(r'^fetch_user_payment_status',fetch_user_payment_status,name="fetch_user_payment_status"),
    

    
]
